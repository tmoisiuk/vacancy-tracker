package org.tmoisiuk.hhl.vt

import java.net.URL
import java.sql.Date

import ru.yaal.project.hhapi.dictionary.entry.IDictionaryEntry
import ru.yaal.project.hhapi.vacancy.Vacancy

case class Address(building: String,
                   city: String,
                   street: String,
                   description: String,
                   raw: String,
                   lat: Double,
                   lng: Double)

case class Salary(from: Int, to: Int, currency: String)

case class EmployerInVacancy(url: URL,
                             alternateUrl: URL)

case class SearchField(id: String, name: String)

object SearchField {
  def apply(searchEntry: IDictionaryEntry): SearchField =
    SearchField(searchEntry.getId, searchEntry.getName)
}

case class MappedVacancy(id: Int,
                         name: String,
                         url: URL,
                         description: String,
                         schedule: SearchField,
                         acceptHandicapped: Boolean,
                         experience: SearchField,
                         address: Address,
                         employment: SearchField,
                         salary: Salary,
                         archived: Boolean,
                         area: SearchField,
                         createdAt: Date,
                         employer: EmployerInVacancy)

object MappedVacancy {

  def from(vacancy: Vacancy): MappedVacancy = MappedVacancy(
    id = vacancy.getId.toInt,
    name = vacancy.getName,
    url = vacancy.getUrl,
    description = vacancy.getDescription,
    schedule = SearchField(vacancy.getSchedule),
    acceptHandicapped = vacancy.getAcceptHandicapped,
    experience = SearchField(vacancy.getExperience),
    address = Address(
      vacancy.getAddress.getBuilding,
      vacancy.getAddress.getCity,
      vacancy.getAddress.getStreet,
      vacancy.getAddress.getDescription,
      vacancy.getAddress.getRaw,
      vacancy.getAddress.getLat,
      vacancy.getAddress.getLng
    ),
    employment = SearchField(vacancy.getEmployment),
    salary = Salary(
      vacancy.getSalary.getFrom,
      vacancy.getSalary.getTo,
      vacancy.getSalary.getCurrency.toString
    ),
    archived = vacancy.getArchived,
    area = SearchField(vacancy.getArea),
    createdAt = new Date(vacancy.getCreatedAt.getTime),
    employer = EmployerInVacancy(
      vacancy.getEmployer.getUrl,
      vacancy.getEmployer.getAlternateUrl
    )
  )
}


