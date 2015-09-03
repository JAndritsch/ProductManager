package com.jandritsch.productsearch

class MockUserInput extends UserInput {
  var shouldAddProduct:String = ""
  var newProductPrice:String = ""
  var newProductQuantity:String = ""

  def getShouldAddProduct:String = shouldAddProduct
  def getNewProductPrice:String = newProductPrice
  def getNewProductQuantity:String = newProductQuantity
}

