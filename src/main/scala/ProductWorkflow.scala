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
        promptUser("That product was not found. Would you like to add it: ", false)
        val input = userInput.getUserInput.toLowerCase
        val addIt = (input == "yes" || input == "y")
        productWasNotfound(name, addIt)
      } else {
        productWasFound(name, product.get)
      }
      promptUser("Enter the product name: ", false)
    }
  }

  def productWasNotfound(name:String, shouldAdd:Boolean) = {
    historyManager.addHistory(name, false)
    if (shouldAdd) {
      promptUser("Enter the price for '" + name + "': ", false)
      val price:Double = userInput.getUserInput.toDouble
      promptUser("Enter the quantity for '" + name + "': ", false)
      val quantity:Int = userInput.getUserInput.toInt
      productManager.addProduct(name, price, quantity)
      promptUser("Your product has been added!", true)
    }
  }

  def productWasFound(name:String, product:JValue) {
    historyManager.addHistory(name, true)
    displayProductInfo(productManager.getProductInfo(product))
  }

  def displayProductInfo(productInfo:(String,String,String)) = {
    val (name, price, qty) = productInfo
    promptUser("Name: " + name, true)
    promptUser("Price: $" + price, true)
    promptUser("Quantity on hand:" + qty, true)
  }

  private def promptUser(text:String, newLine:Boolean) = {
    if (usePrompt) {
      if (newLine) {
        println(text)
      } else {
        print(text)
      }
    }
  }

}
