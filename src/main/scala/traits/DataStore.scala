package com.jandritsch.productsearch

import org.json4s.JValue

trait DataStore {
  def read:JValue
  def writeObject(collection:JValue)
}
