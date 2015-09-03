package com.jandritsch.productsearch

import scala.collection.mutable.MutableList

class MockUserOutput extends UserOutput {
  var entries:MutableList[String] = new MutableList()

  def display(text:String, newLine:Boolean) = {
    entries += text
  }
}

