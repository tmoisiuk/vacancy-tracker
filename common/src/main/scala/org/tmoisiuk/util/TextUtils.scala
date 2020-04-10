package org.tmoisiuk.util

object TextUtils {
  def removeQuotesAndEscape(str: String): String =
    str.replaceAll("\"(.+)\"", "$1")
      .replaceAll("\\\\", "")
}
