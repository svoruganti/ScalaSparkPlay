package Impl

import javax.inject.Inject

import Model.{Business, Inspection, Violation}
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.types._
import traits.{AppSparkSession, BusinessService}

class BusinessServiceImpl @Inject() (appSparkSession: AppSparkSession) extends BusinessService{

  val spark = appSparkSession.getSession()
  import spark.implicits._

  override def getAllBusinesses: Dataset[Business] = {
    businessDataset
  }

  private val businessDataset : Dataset[Business] = {
    spark.read.option("header", true).schema(businessStructure).csv("public/files/businesses_plus.csv").as[Business]
  }

  private val inspectionDataset : Dataset[Inspection] = {
    spark.read.option("header", true).schema(inspectionStructure).csv("public/files/businesses_plus.csv").map(row => Inspection(row.getAs[String](0), row.getAs[String](1), row.getAs[String](2), row.getAs[String](3)))
  }

  private val violationDataset : Dataset[Violation] = {
    spark.read.option("header", true).schema(violationStructure).csv("public/files/violations_plus.csv").as[Violation]
  }

  override def getBusinessCountByCity: Dataset[(String, Long)] = {
    businessDataset.filter(x => x.city != null).groupByKey(_.city).count()
  }

  override def getInpectionsCountByBusiness: Dataset[(String, Long)] = {
    businessDataset.joinWith(inspectionDataset, businessDataset("business_id") === inspectionDataset("business_id"))
        .map(x => (x._1.name, x._2.iType)).groupByKey(_._1).mapGroups((x, y) => (x, y.length))
  }

  override def getViolationsCountByBusinessGroupedRiskType: Dataset[(String, String, Long)] = {
    val violationsByBusiness = violationDataset.groupByKey(x => (x.business_id, x.risk_category)).mapGroups((x, y) => (x._1, x._2, y.length.toLong));
    return violationsByBusiness;
  }

  val businessStructure = StructType(Array(StructField("business_id", StringType, true), StructField("name", StringType, true), StructField("address", StringType, true), StructField("city", StringType, true)))
  val inspectionStructure = StructType(Array(StructField("business_id", StringType, true), StructField("score", StringType, true), StructField("date", StringType, true), StructField("iType", StringType, true)))
  val violationStructure = StructType(Array(StructField("business_id", StringType, true), StructField("date", StringType, true), StructField("violationTyeId", StringType, true), StructField("risk_category", StringType, true), StructField("description", StringType, true)))
}

case class ViolationBusinessRiskType(business_id:String, risk_type:String)
