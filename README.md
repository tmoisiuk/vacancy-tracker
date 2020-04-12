**Vacancy tracker**

Head Hunter vacancy tracking platform

**Modules**

`hh-loader` loads HeadHunter data, converts and upload to Kafka

`flink-loader` loads data from Kafka topic and then stores it in Postgres DB



**Configuration**

Conf files in HOCON format are used in flink-loader and hh-loader applications.
Please see https://github.com/lightbend/config/blob/master/HOCON.md

`hh-loader` config example:

```hocon

load: {
  period: 30 //batch load interval in minutes
}

kafka: {
  topic: "test" //kafka topic 
  bootstrap-servers: ["localhost:9092"] //bootstrap servers
  key-serializer: "org.apache.kafka.common.serialization.StringSerializer"
  value-serializer: "org.apache.kafka.common.serialization.StringSerializer"
}

search: {

  text: "java", // query text (required)
  period: 23, // period in days between 1 and 30 (required)
  vacancyLimit: 100, // vacancy limit (Required, MAX 100)
  profField: "1.420" // area ID, see https://api.hh.ru/specializations (Optional)
  salaryLowLimit: 100, // Salary lower limit (optional
  salaryHighLimit: 500, // Salary higher limit (optional)
  salaryCurrency: "USD", // salary currency as ISO 4217 code (Optional, Works only if salaries parameters are set. Default RUR)
  experience: "noExperience", // experience (Optional, options: "noExperience", "between1And3", "between3And6", "moreThan6")
  onlyWithSalary: false, // show only vacancies with salary (Optional)
  schedule: "fullDay", // Schedule (Optional, options: "fullDay", "shift", "flexible", "remote")
  employment: "part", // Employment (Optional, options: "full", "part", "project", "volunteer", "probation"
}
```
`flink-loader` config example:

```hocon
kafka: {
  topic: "hh" //kafka topic
  group-id: "flink-loader" //consumer group
  bootstrap-servers: ["localhost:9092"], //bootstrap servers
  zookeeper-connect: "localhost:2181", //zookepeer
  auto-offset-reset: "earliest" 
}

jdbc: { //jdbc properties
  driver: "org.postgresql.Driver"
  url: "jdbc:postgresql://127.0.0.1:5432/vt_db"
  username: "postgres"
  password: "password"
  batch-interval: 100
}
```

**Next steps to implement**

1. Configurability:

    - Remove loaders configuration files and sql queries to external directory or environment variables
    - Create more configurable vacancy search query
    - Set up data quality checking mechanism. Currently Flink loader skip data which cannot be deserialized 
    - Support other serialization options 
    
2. Security:

    - Remove secrets from configuration
    - Change 'network: host' in Docker compose

3. HH API limitations:

    - Historical load is absent
    - Currently HH API allows to fetch only last month results for free
    - HH API has a restriction for search results amount

4. Data Base:

    - Service fields as 'created' or 'last_modified' could be added in 
    'vacancies' table if needed
    - Some fields such as 'schedule_id' should refer to related table with
    schedule description etc.
    - Currently we ignore any changes in vacancies (only first state is stored). 
     Could be changed if needed
