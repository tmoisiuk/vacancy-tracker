package org.tmoisiuk.fl

import config.AppConfig
import org.apache.flink.streaming.api.scala._
import org.tmoisiuk.fl.flink.KafkaStreamProvider
import org.tmoisiuk.util.JsonOperations._
import org.tmoisiuk.vt.MappedVacancy

import scala.util.Try


object Main extends App {

  val config = AppConfig()

  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
  val streamProvider = new KafkaStreamProvider(env, config.kafka)

  val vacancies: DataStream[MappedVacancy] = getVacancies(streamProvider.stream, filterMalformed)

  show(vacancies)

  env.execute("Flink Kafka Example")


  /** Filters invalid records
    *
    * @param input input records
    * @return Successfully mapped records
    */

  def filterMalformed(input: DataStream[Try[MappedVacancy]]): DataStream[MappedVacancy] =
    input.flatMap(_.toOption)

  def getVacancies(stream: DataStream[String],
                   handleMalformed: DataStream[Try[MappedVacancy]] => DataStream[MappedVacancy]):
  DataStream[MappedVacancy] = handleMalformed(stream.map(str => Try(str.as[MappedVacancy])))


  def saveToPostgres(stream: DataStream[MappedVacancy]) = ???

  def show(stream: DataStream[MappedVacancy]) = stream.print()
}
