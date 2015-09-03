package com.jandritsch.productsearch

class DefaultUserInput extends UserInput {
  def getShouldAddProduct:String = readLine
  def getNewProductPrice:String = readLine
  def getNewProductQuantity:String = readLine
}
