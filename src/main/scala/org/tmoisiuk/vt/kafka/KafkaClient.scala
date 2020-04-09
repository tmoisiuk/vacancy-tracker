package org.tmoisiuk.vt.kafka

import java.util.Properties
import java.util.concurrent.Future

import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord, RecordMetadata}
import org.tmoisiuk.vt.config.KafkaConfig

/**
  * Starting kafka producer to send messages
  */
class KafkaClient(config: KafkaConfig) extends LazyLogging {

  private[kafka] def props: Properties = {
    val props = new Properties()
    props.put("bootstrap.servers", config.bootstrapServers)
    props.put("key.serializer", config.keySerializer)
    props.put("value.serializer", config.valueSerializer)
    props
  }

  private[kafka] def getProducer: Producer[String, String] = new KafkaProducer[String, String](props)

  private[kafka] val kafkaProducer = getProducer

  /**
    * Sending json to kafka
    *
    * @param key   record key
    * @param value recoed value
    */
  def sendData(key: String, value: String): Unit = {
    val record = new ProducerRecord[String, String](config.topic, key, value)
    processMessage(kafkaProducer.send(record))
  }

  private[kafka] def processMessage(messageMetadata: Future[RecordMetadata]): Unit = {
    if (!messageMetadata.isDone) {
      logger.info(s"Kafka message for topic (${messageMetadata.get.topic}) sent successfully")
    } else {
      logger.info(s"Error while sending to Kafka")
    }
  }

  /**
    * Close Kafka Producer
    */
  def closeKafkaProducer(): Unit = kafkaProducer.close()
}


