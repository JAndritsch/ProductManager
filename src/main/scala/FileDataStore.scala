package com.jandritsch.productsearch

import scala.io.Source
import java.io._
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization.write
import org.json4s.DefaultFormats

class FileDataStore(val filePath:String) extends DataStore {

  def read:JValue = {
    val file = Source.fromFile(filePath)
    var json = parse(file.mkString)
    file.close
    json
  }

  def writeObject(collection:JValue) = {
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
