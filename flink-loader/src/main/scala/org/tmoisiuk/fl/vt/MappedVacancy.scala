package org.tmoisiuk.fl.vt

import java.net.URL
import java.sql.Date

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