package org.tmoisiuk.vt.hh_api

import org.scalamock.scalatest.MockFactory
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.tmoisiuk.vt.config.SearchConfig
import ru.yaal.project.hhapi.search.parameter.{Period, Text}
import ru.yaal.project.hhapi.search.{ISearchParameter, SearchParamNames}
import ru.yaal.project.hhapi.vacancy.{AbstractSearch, VacancyList, VacancySearch}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class HeadHunterClientTest extends AnyFunSuite with Matchers with MockFactory {

  test("HH Client should create Search from search config") {

    class VacancySearchUnderTest extends VacancySearch {
      val params: ListBuffer[ISearchParameter] = mutable.ListBuffer[ISearchParameter]()

      override def addParameter(searchParameter: ISearchParameter): AbstractSearch[VacancyList] = {
        params += searchParameter
        this
      }
    }

    val text = "java"
    val period = 2
    val vacancyLimit = 100
    val searchConfig = SearchConfig(text, period, vacancyLimit)

    val vacancySearchUnderTest = new VacancySearchUnderTest

    HeadHunterClient.getSearch(searchConfig, vacancySearchUnderTest)

    vacancySearchUnderTest.params.size shouldEqual 2

    val actualText = vacancySearchUnderTest
      .params
      .head
      .asInstanceOf[Text]
      .getSearchParameters
      .getParameterMap.get(SearchParamNames.TEXT)
      .get(0)

    actualText shouldEqual text

    val actualPeriod = vacancySearchUnderTest
      .params(1)
      .asInstanceOf[Period]
      .getPeriod

    actualPeriod shouldEqual period
  }
}
