package com.databricks.apps.logs.chapter1

import com.databricks.apps.logs.{OrderingUtils, ApacheAccessLog}
import org.apache.spark.{SparkContext, SparkConf}

/**
 * The LogAnalyzer takes in an apache access log file and
 * computes some statistics on them.
 *
 * Example command to run:
 * % spark-submit
 *   --class "com.databricks.apps.logs.chapter1.LogAnalyzer"
 *   --master local[4]
 *   target/scala-2.10/spark-logs-analyzer_2.10-1.0.jar
 *   ../../data/apache.access.log
 */
object LogAnalyzer {

  def main(args: Array[String]){

    val sparkConf = new SparkConf().setAppName("Log Analyzer in Scala")

    val sc = new SparkContext(sparkConf)

    val logFile = args(0)

    val accessLogs = sc.textFile(logFile).map(ApacheAccessLog.parseLogLine).cache()

    val contentSizes = accessLogs.map(log => log.contentSize).cache()

    println("Content size Avg: %s, Min: %s, Max: %s".format(
      contentSizes.reduce(_ + _) / contentSizes.count,
      contentSizes.min, contentSizes.max))

    val responseCodeToCount = accessLogs
      .map(log => (log.responseCode, 1))
      .reduceByKey(_ + _)
      .take(100)

    println(s"""Response code counts: ${responseCodeToCount.mkString("[", ",", "]")}""")

    val ipAddresses = accessLogs
      .map(log => (log.ipAddress, 1))
      .reduceByKey(_ + _)
      .filter(_._2 > 10)
      .map(_._1)
      .take(100)

    println(s"""IPAddresses > 10 times: ${ipAddresses.mkString("[", ",", "]")}""")

    val topEndpoints = accessLogs
      .map(log=>(log.endpoint, 1))
      .reduceByKey(_ + _)
      .top(10)(OrderingUtils.SecondValueOrdering)

    println(s"""Top Endpoints: ${topEndpoints.mkString("[", ",", "]")} """)


    sc.stop()

  }

}
