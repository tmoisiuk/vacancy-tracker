package org.tmoisiuk.vt.hh_api

import org.tmoisiuk.vt.config.SearchConfig
import ru.yaal.project.hhapi.dictionary.Constants
import ru.yaal.project.hhapi.dictionary.entry.entries.proffield.ProfField
import ru.yaal.project.hhapi.search.parameter.{Period, Text}
import ru.yaal.project.hhapi.vacancy.{AbstractSearch, Salary, Vacancy, VacancySearch}

import scala.collection.JavaConverters._

object HeadHunterClient {

  def searchVacancies(config: SearchConfig): Iterable[Vacancy] = getSearch(config).search.asScala

  private[hh_api] def getSearch(config: SearchConfig) = {

    val search = new VacancySearch(AbstractSearch.MAX_VACANCIES_LIMIT)

      .addParameter(new Text(config.text, Constants.VacancySearchFields.VACANCY_NAME))
      .addParameter(new Period(config.period))
      .addParameter(
        new Salary(
          new Integer(config.salaryLowLimit.getOrElse(0)),
          new Integer(config.salaryHighLimit.getOrElse(Integer.MAX_VALUE)),
          config.salaryCurrency.map(curr => Constants.Currency.CURRENCIES.getById(curr)).getOrElse(Constants.Currency.RUR))
      )
    config.employment.map(employment => search.addParameter(Constants.Employment.EMPLOYMENTS.getById(employment)))
    config.experience.map(exp => search.addParameter(Constants.Experience.EXPERIENCES.getById(exp)))
    config.onlyWithSalary.map {
      case true => search.addParameter(Constants.OnlyWithSalary.ON)
      case false => search.addParameter(Constants.OnlyWithSalary.OFF)
    }
    config.profField.map(field => search.addParameter(ProfField.PROF_FIELDS.getById(field)))
    config.schedule.map(schedule => search.addParameter(Constants.Schedule.SCHEDULES.getById(schedule)))

    search
  }
}
