package postgres

import java.sql.Types

import com.typesafe.scalalogging.LazyLogging
import org.tmoisiuk.fl.config.JdbcConfig
import org.apache.flink.api.java.io.jdbc.JDBCOutputFormat
import org.apache.flink.streaming.api.datastream.DataStreamSink
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.types.Row
import org.tmoisiuk.fl.vt.MappedVacancy
import org.apache.flink.streaming.api.scala._

class PostgresVacancySink(config: JdbcConfig) extends LazyLogging {

  def write(vacancies: DataStream[MappedVacancy], sqlQuery: String): DataStreamSink[Row] = {

    val queryTypes = Array(
      Types.INTEGER,
      Types.VARCHAR,
      Types.VARCHAR,
      Types.VARCHAR,
      Types.VARCHAR,
      Types.BOOLEAN,
      Types.VARCHAR,
      Types.VARCHAR,
      Types.VARCHAR,
      Types.VARCHAR,
      Types.VARCHAR,
      Types.VARCHAR,
      Types.DOUBLE,
      Types.DOUBLE,
      Types.VARCHAR,
      Types.INTEGER,
      Types.INTEGER,
      Types.VARCHAR,
      Types.BOOLEAN,
      Types.VARCHAR,
      Types.DATE,
      Types.VARCHAR
    )

    val jdbcOutput = JDBCOutputFormat.buildJDBCOutputFormat()
      .setDrivername(config.driver)
      .setDBUrl(config.url)
      .setUsername(config.username)
      .setPassword(config.password)
      .setQuery(sqlQuery)
      .setBatchInterval(config.batchInterval)
      .setSqlTypes(queryTypes)
      .finish()

    asRows(vacancies).writeUsingOutputFormat(jdbcOutput)
  }

  private[postgres] def asRows(rows: DataStream[MappedVacancy]): DataStream[Row] =

    rows.map(vacancy => {
      val row = new Row(22)
      row.setField(0, vacancy.id)
      row.setField(1, vacancy.name)
      row.setField(2, vacancy.url.toString)
      row.setField(3, vacancy.description)
      row.setField(4, vacancy.schedule.id)
      row.setField(5, vacancy.acceptHandicapped)
      row.setField(6, vacancy.experience.id)
      row.setField(7, vacancy.address.building)
      row.setField(8, vacancy.address.city)
      row.setField(9, vacancy.address.street)
      row.setField(10, vacancy.address.description)
      row.setField(11, vacancy.address.raw)
      row.setField(12, vacancy.address.lat)
      row.setField(13, vacancy.address.lng)
      row.setField(14, vacancy.employment.id)
      row.setField(15, vacancy.salary.from)
      row.setField(16, vacancy.salary.to)
      row.setField(17, vacancy.salary.currency)
      row.setField(18, vacancy.archived)
      row.setField(19, vacancy.area.id)
      row.setField(20, vacancy.createdAt)
      row.setField(21, vacancy.employer.url.toString)

      row
    })
}