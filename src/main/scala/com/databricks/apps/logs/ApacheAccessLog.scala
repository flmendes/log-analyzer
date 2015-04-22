package com.databricks.apps.logs

import oi.thekraken.grok.api.Grok
import org.json4s.{DefaultFormats, Formats}

import scala.util.Try

/**
 * Created by flavio on 4/21/15.
 */
case class ApacheAccessLog(ipAddress: String, clientIdentd: String,
                           userId: String, dateTime: String, method: String,
                           endpoint: String, protocol: String,
                           responseCode: Int, contentSize: Long)

object ApacheAccessLog {

  protected implicit val jsonFormats: Formats = DefaultFormats

  def parseLogLine(log: String): ApacheAccessLog = {
    val grok = Grok.create("./patterns/patterns")
    grok.compile("%{COMMONAPACHELOG}")

    val gm = grok.`match`(log)

    gm.captures()

    val m = gm.toMap

    val ip = Try(m.get("clientip").toString)
    val client = Try(m.get("ident").toString)
    val user = Try(m.get("auth").toString)
    val timestamp = Try(m.get("timestamp").toString)
    val method = Try(m.get("verb").toString)
    val endpoint = Try(m.get("request").toString)
    val httpVersion = Try(m.get("httpversion").toString)
    val responseCode = Try(m.get("response").toString.toInt)

    val contentSize = Try (m.get("bytes").toString.toLong)


    ApacheAccessLog(ip.getOrElse("127.0.0.1"), client.getOrElse(""), user.getOrElse(""), timestamp.getOrElse(""),
      method.getOrElse("GET"), endpoint.getOrElse(""), httpVersion.getOrElse("1.0"), responseCode.getOrElse(200),
      contentSize.getOrElse(0l) )


  }

}
