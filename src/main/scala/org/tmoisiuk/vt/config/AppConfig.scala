package org.tmoisiuk.vt.config

import com.typesafe.config.ConfigFactory
import pureconfig.generic.auto._
import scala.io.Source

case class AppConfig(load: LoadConfig,
                     kafka: KafkaConfig,
                     search: SearchConfig)

case class KafkaConfig(
                        topic: String,
                        bootstrapServers: Seq[String],
                        keySerializer: String,
                        valueSerializer: String,
                      )


//todo move coments to readme

case class LoadConfig(period: Int)

case class SearchConfig(
                         text: String, // query text (required)
                         period: Int, // period in days between 1 and 30 (required)
                         vacancyLimit: Int, // vacancy limit (Required, MAX 100)
                         profField: Option[String] = None, // area ID, see https://api.hh.ru/specializations (Optional)
                         salaryLowLimit: Option[Int] = None, // Salary lower limit (optional
                         salaryHighLimit: Option[Int] = None, // Salary higher limit (optional)
                         salaryCurrency: Option[String] = None, // salary currency as ISO 4217 code (Optional, Works only if salaries parameters are set. Default RUR)
                         experience: Option[String] = None, // experience (Optional, options: "noExperience", "between1And3", "between3And6", "moreThan6")
                         onlyWithSalary: Option[Boolean] = None, // show only vacancies with salary (Optional)
                         schedule: Option[String] = None, // Schedule (Optional, options: "fullDay", "shift", "flexible", "remote")
                         employment: Option[String] = None, // Employment (Optional, options: "full", "part", "project", "volunteer", "probation"
                       )

object AppConfig {

  def apply(reference: String = ""): AppConfig = {

    val conf = if (reference.nonEmpty) ConfigFactory.parseString(getTextFileContent(reference))
    else ConfigFactory.load("pure.conf")

    pureconfig.loadConfigOrThrow[AppConfig](conf)
  }

  private def getTextFileContent(path: String): String = {
    val resourceInputStream = getClass.getResourceAsStream(path)
    if (resourceInputStream == null) {
      throw new NullPointerException("Can't find the resource in classpath: " + path)
    }
    val source = Source.fromInputStream(resourceInputStream)("UTF-8")
    val string = source.mkString
    source.close()
    string
  }
}
