package org.tmoisiuk.hhl.kafka

import java.util.Properties

import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord, RecordMetadata}
import org.tmoisiuk.hhl.config.KafkaConfig

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

/**
  * Starts kafka producer to send messages
  */
class KafkaClient(config: KafkaConfig) extends LazyLogging {

  private[kafka] def props: Properties = {
    val props = new Properties()
    props.put("bootstrap.servers", config.bootstrapServers.mkString(","))
    props.put("key.serializer", config.keySerializer)
    props.put("value.serializer", config.valueSerializer)
    props
  }

  private[kafka] def getProducer: Producer[String, String] = new KafkaProducer[String, String](props)

  private[kafka] val kafkaProducer = getProducer

  /**
    * Sends json record to kafka
    *
    * @param key   record key
    * @param value record value
    */
  def sendData(key: String, value: String): Unit = {
    val record = new ProducerRecord[String, String](config.topic, key, value)
    processMessage(kafkaProducer.send(record))
  }

  private[kafka] def processMessage(future: java.util.concurrent.Future[RecordMetadata]): Unit = {
    implicit val ec: ExecutionContextExecutor = ExecutionContext.global

    scala.concurrent.Future {
      future.get()
    } onComplete { recordMetadata =>
      logger.info(s"Record sent to Kafka, record metadata: $recordMetadata")
    }
  }

  /**
    * Closes Kafka Producer
    */
  def closeKafkaProducer(): Unit = kafkaProducer.close()
}


