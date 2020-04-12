package org.tmoisiuk.fl.config

import org.scalatest.funsuite.AnyFunSuite

class AppConfigTest extends AnyFunSuite {

  test("AppConfig creation test") {

    val actual = AppConfig("/test.conf")

    val expected = AppConfig(
      KafkaConfig(
        "hh",
        "flink-loader",
        Seq("localhost:9092"),
        "localhost:2181",
        "earliest"
      ),
      JdbcConfig(
        "org.postgresql.Driver",
        "jdbc:postgresql://127.0.0.1:5432/vt_db",
        "postgres",
        "password",
        100
      )
    )

    assert(actual == expected)
  }
}

