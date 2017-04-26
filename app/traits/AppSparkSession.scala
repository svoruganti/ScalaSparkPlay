package traits

import org.apache.spark.sql.SparkSession

trait AppSparkSession {
  def getSession() : SparkSession
}
