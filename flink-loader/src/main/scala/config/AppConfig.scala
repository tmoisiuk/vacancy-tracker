package config

import java.util.Properties

import com.typesafe.config.ConfigFactory
import pureconfig.generic.auto._
import org.tmoisiuk.util.TextUtils._

case class AppConfig(kafka: KafkaConfig)

case class KafkaConfig(topic: String,
                       groupId: String,
                       bootstrapServers: Seq[String],
                       zookeeperConnect: String,
                       autoOffsetReset: String) {

  def toProperties: Properties = {
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", bootstrapServers.mkString(","))
    properties.setProperty("zookeeper.connect", zookeeperConnect)
    properties.setProperty("group.id", groupId)
    properties.setProperty("auto.offset.reset", autoOffsetReset)
    properties
  }
}

object AppConfig {

  def apply(reference: String = ""): AppConfig = {

    val conf = if (reference.nonEmpty) ConfigFactory.parseString(getTextFileContent(reference))
    else ConfigFactory.load("pure.conf")

    pureconfig.loadConfigOrThrow[AppConfig](conf)
  }
}
