#!/usr/bin/env bash


cd /Users/tmoisiuk/IdeaProjects/vacancy-tracker/docker

docker-compose up kafka-cluster

~/kafka_2.12-2.2.1/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic 'hh' "test 1"

~/kafka_2.12-2.2.1/bin/kafka-topics.sh --create --topic hh --partitions 3 --replication-factor 1 --zookeeper 127.0.0.1:2181

~/kafka_2.12-2.2.1/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic hh --from-beginning

cp /Users/tmoisiuk/IdeaProjects/vacancy-tracker/hh-loader/target/hh-loader-1.0-SNAPSHOT-jar-with-dependencies.jar /Users/tmoisiuk/IdeaProjects/vacancy-tracker/docker/hh-loader/hh-loader.jar
cp /Users/tmoisiuk/IdeaProjects/vacancy-tracker/flink-loader/target/flink-loader-1.0-SNAPSHOT-jar-with-dependencies.jar /Users/tmoisiuk/IdeaProjects/vacancy-tracker/docker/flink-loader/flink-loader.jar
