package org.tmoisiuk.fl.flink

import org.tmoisiuk.fl.config.KafkaConfig
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

trait StreamProvider {
  def stream: DataStream[String]
}

class KafkaStreamProvider(env: StreamExecutionEnvironment, config: KafkaConfig) extends StreamProvider {

  override def stream: DataStream[String] =
    env
      .addSource(
        new FlinkKafkaConsumer[String](
          config.topic,
          new SimpleStringSchema(),
          config.toProperties)
      )
}
