package com.jandritsch.productsearch

class DefaultUserOutput extends UserOutput {
  def display(text:String, newLine:Boolean) = {
    if (newLine) {
      println(text)
    } else {
      print(text)
    }
  }
}
