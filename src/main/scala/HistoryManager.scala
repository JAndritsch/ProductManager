package com.jandritsch.productsearch

import org.json4s._
import org.json4s.native.JsonMethods._
import java.util.Date
import java.text.SimpleDateFormat

class HistoryManager(val historyFilePath:String) {

  var history = FileUtils.readFileAsJson(historyFilePath)

  def addHistory(term:String, found:Boolean) = {
    val entry = makeHistory(term, found)
    history = history.merge(
      JObject(List(JField("history", JArray(List(entry)))))
    )
    FileUtils.writeToFile(historyFilePath, history)
  }

  private def makeHistory(term:String, found:Boolean):JObject = {
    val df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'")
    JObject(
      List(
        JField("name", JString(term)),
        JField("found", JBool(found)),
        JField("searchedAt", JString(df.format(new Date())))
      )
    )
  }

}
