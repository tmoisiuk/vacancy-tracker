package org.tmoisiuk.hhl.kafka

import org.apache.kafka.clients.producer.{MockProducer, Producer, ProducerRecord}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.tmoisiuk.hhl.config.KafkaConfig

class KafkaClientTest extends AnyFunSuite with Matchers with BeforeAndAfterAll with BeforeAndAfterEach {

  private val topic = "test"
  private val config = KafkaConfig(topic, Nil, "", "")

  private var producer: MockProducer[String, String] = _
  private var client: KafkaClient = _

  private val vacancyKey = "123"
  private val vacancyValue = """{"name":"java"}"""
  private val record = new ProducerRecord[String, String](topic, vacancyKey, vacancyValue)

  class KafkaClientUnderTest extends KafkaClient(config) {
    override def getProducer: Producer[String, String] = producer
  }

  override def beforeEach(): Unit = {
    producer = new MockProducer[String, String](true, null, null)
    client = new KafkaClientUnderTest
  }

  test("Kafka Client should produce a single record") {
    client.sendData(vacancyKey, vacancyValue)
    assert(producer.history().contains(record))
    assert(producer.history().size() == 1)
  }

  test("Kafka Client should produce more than 1 record") {
    client.sendData(vacancyKey, vacancyValue)
    client.sendData(vacancyKey, vacancyValue)
    assert(producer.history().contains(record))
    assert(producer.history().size() == 2)
  }

  override def afterEach: Unit = {
    producer.close()
  }
}