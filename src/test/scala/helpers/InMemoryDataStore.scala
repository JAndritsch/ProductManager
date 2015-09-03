package com.jandritsch.productsearch

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization.write
import org.json4s.DefaultFormats

class InMemoryDataStore extends DataStore {

  var contents:String = ""

  def read:JValue = {
    parse(contents)
  }

  def writeObject(collection:JValue) = {
    implicit val formats = DefaultFormats
    contents = write(collection)
  }

}
