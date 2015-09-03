package com.jandritsch.productsearch

import org.json4s._

class ProductWorkflow(
  val productManager:ProductManager,
  val historyManager:HistoryManager,
  val userInput:UserInput,
  val usePrompt:Boolean
) {

  def run(productNames:Iterator[String]) {
    for (name <- productNames) {
      val product = productManager.findProductByName(name)
      if (product == None) {
        productWasNotfound(name, shouldAddProduct)
      } else {
        productWasFound(name, product.get)
      }
      promptUser("Enter the product name: ")
    }
  }

  def productWasNotfound(name:String, shouldAdd:Boolean) = {
    historyManager.addHistory(name, false)
    if (shouldAdd) {
      promptUser("Enter the price for '" + name + "': ")
      val price:Double = getNewProductPrice
      promptUser("Enter the quantity for '" + name + "': ")
      val quantity:Int = getNewProductQuantity
      productManager.addProduct(name, price, quantity)
      promptUser("Your product has been added!")
    }
  }

  def productWasFound(name:String, product:JValue) {
    historyManager.addHistory(name, true)
    displayProductInfo(productManager.getProductInfo(product))
  }

  def displayProductInfo(productInfo:(String,String,String)) = {
    val (name, price, qty) = productInfo
    promptUser("Name: " + name)
    promptUser("Price: $" + price)
    promptUser("Quantity on hand:" + qty)
  }

  def shouldAddProduct:Boolean = {
    val input = userInput.getUserInput.toLowerCase
    input == "yes" || input == "y"
  }

  def getNewProductQuantity:Int =
    userInput.getUserInput.toInt

  def getNewProductPrice:Double =
    userInput.getUserInput.toDouble

  private def promptUser(text:String) = {
    if (usePrompt) {
      println(text)
    }
  }

}
