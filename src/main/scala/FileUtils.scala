package com.jandritsch.productsearch

import scala.io.Source
import java.io._
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization.write
import org.json4s.DefaultFormats

object FileUtils {

  def readFileAsJson(filePath:String):JValue = {
    val file = Source.fromFile(filePath)
    var json = parse(file.mkString)
    file.close
    json
  }

  def writeToFile(filePath:String, collection:JValue) {
    val writer = new BufferedWriter(
      new OutputStreamWriter(
        new FileOutputStream(filePath)
      )
    )
    implicit val formats = DefaultFormats
    val json = write(collection)
    writer.write(json)
    writer.close
  }

}
