package org.tmoisiuk.fl

import com.typesafe.scalalogging.LazyLogging
import org.tmoisiuk.fl.config.AppConfig
import org.apache.flink.streaming.api.datastream.DataStreamSink
import org.apache.flink.streaming.api.scala._
import org.tmoisiuk.fl.flink.KafkaStreamProvider
import org.tmoisiuk.fl.util.JsonOperations._
import org.tmoisiuk.fl.util.TextUtils.getTextFileContent
import org.tmoisiuk.fl.vt.MappedVacancy
import postgres.PostgresVacancySink

import scala.util.Try

/**
  * Flink Runner
  * Creates configuration, extracts data from Kafka topic, loads to DB in streaming manner
  */
object Main extends App with LazyLogging {

  val config = AppConfig()
  logger.info(s"configuration: $config")

  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
  val streamProvider = new KafkaStreamProvider(env, config.kafka)
  val sinkProvider = new PostgresVacancySink(config.jdbc)

  val vacancies: DataStream[MappedVacancy] = getVacancies(streamProvider.stream, filterMalformed)
  val sinkQuery = getTextFileContent("/upsert_query.sql")

  logger.info(s"Sink query: $sinkQuery")

  saveToPostgres(vacancies)

  env.execute("Kafka-Postgres-Pipeline")


  /** Filters invalid records
    *
    * @param input input records
    * @return Successfully mapped records
    */

  def filterMalformed(input: DataStream[Try[MappedVacancy]]): DataStream[MappedVacancy] =
    input.flatMap(_.toOption)

  /**
    * Vacancies converter
    *
    * @param stream          DataStream of json strings
    * @param handleMalformed function for invalid records handling
    * @return DataStream of [[org.tmoisiuk.fl.vt.MappedVacancy]]
    *
    **/
  def getVacancies(stream: DataStream[String],
                   handleMalformed: DataStream[Try[MappedVacancy]] => DataStream[MappedVacancy]):
  DataStream[MappedVacancy] = handleMalformed(stream.map(str => Try(str.as[MappedVacancy])))

  def saveToPostgres(stream: DataStream[MappedVacancy]): Unit = sinkProvider.write(stream, sinkQuery)

  def show(stream: DataStream[MappedVacancy]): DataStreamSink[MappedVacancy] = stream.print()
}
