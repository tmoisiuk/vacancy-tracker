package org.tmoisiuk.hhl.config

import com.typesafe.config.ConfigFactory
import org.tmoisiuk.hhl.util.TextUtils._
import pureconfig.generic.auto._

case class AppConfig(load: LoadConfig,
                     kafka: KafkaConfig,
                     search: SearchConfig)

case class KafkaConfig(
                        topic: String,
                        bootstrapServers: Seq[String],
                        keySerializer: String,
                        valueSerializer: String,
                      )

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
}
