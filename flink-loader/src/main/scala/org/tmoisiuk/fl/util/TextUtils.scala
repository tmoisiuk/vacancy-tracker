package org.tmoisiuk.fl.util

import scala.io.Source

object TextUtils {
  def getTextFileContent(path: String): String = {
    val resourceInputStream = getClass.getResourceAsStream(path)
    if (resourceInputStream == null) {
      throw new NullPointerException("Can't find the resource in classpath: " + path)
    }
    val source = Source.fromInputStream(resourceInputStream)("UTF-8")
    val string = source.mkString
    source.close()
    string
  }
}
