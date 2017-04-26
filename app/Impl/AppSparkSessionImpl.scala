package Impl

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}
import traits.AppSparkSession

class AppSparkSessionImpl extends AppSparkSession{
  override def getSession(): SparkSession = {
    SparkSession.builder().appName("ScalaSparkPlay").master("local[*]").getOrCreate()
  }
}
