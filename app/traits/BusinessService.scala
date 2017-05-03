package traits

import Model.Business
import org.apache.spark.sql.{DataFrame, Dataset}

trait BusinessService {
  def getAllBusinesses : Dataset[Business]
  def getBusinessCountByCity : Dataset[(String, Long)]
  def getInpectionsCountByBusiness : Dataset[(String, Long)]
  def getViolationsCountByBusinessGroupedRiskType : Dataset[(String, String, Long)]
}
