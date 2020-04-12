package org.tmoisiuk.hhl

import java.util.concurrent.{Executors, TimeUnit}

import com.typesafe.scalalogging.LazyLogging
import org.apache.commons.lang3.exception.ExceptionUtils
import org.tmoisiuk.hhl.config.AppConfig
import org.tmoisiuk.hhl.hh_api.HeadHunterClient
import org.tmoisiuk.hhl.kafka.KafkaClient
import org.tmoisiuk.hhl.util.JsonOperations._
import org.tmoisiuk.hhl.vt.MappedVacancy
import ru.yaal.project.hhapi.vacancy.Vacancy

import scala.util.{Failure, Success, Try}

/**
  * Head hunter API Runner
  * Creates configuration, searches for vacancies and send the data to Kafka
  */
object Main extends App with LazyLogging {

  val config = AppConfig()
  logger.info(s"configuration: $config")

  val kafkaClient = new KafkaClient(config.kafka)
  sys.addShutdownHook(() -> kafkaClient.closeKafkaProducer())

  val load = new Runnable {
    override def run(): Unit = loadData(
      () => HeadHunterClient.searchVacancies(config.search),
      saveToKafka
    )
  }

  Executors
    .newScheduledThreadPool(1)
    .scheduleAtFixedRate(load, 0, config.load.period, TimeUnit.MINUTES)

  def loadData(vacancies: () => Iterable[Vacancy],
               save: Iterable[Vacancy] => Unit): Unit = Try(save(vacancies())) match {
    case Success(_) => logger.info("Head Hunter vacancies were uploaded to Kafka Topic")
    case Failure(exception) => logger.info(s"Error while loading occurred: ${ExceptionUtils.getStackTrace(exception)}")
  }

  def saveToKafka(vacancies: Iterable[Vacancy]): Unit = vacancies.foreach(
    vacancy => kafkaClient.sendData(vacancy.getId, MappedVacancy.from(vacancy).toJson)
  )
}
