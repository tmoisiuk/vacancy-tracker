package org.tmoisiuk.vt.config

import org.scalatest.funsuite.AnyFunSuite

class AppConfigTest extends AnyFunSuite {

  test("AppConfig creation test") {

    val actual = AppConfig("/test.conf")

    val expected = AppConfig(
      LoadConfig(period = 30),
      KafkaConfig(
        "test",
        Seq("localhost:9092"),
        "org.apache.kafka.common.serialization.StringSerializer",
        "org.apache.kafka.common.serialization.StringSerializer"
      ),
      SearchConfig(text = "java", period = 30, vacancyLimit = 100, schedule = Some("fullDay")),
    )

    assert(actual == expected)
  }
}