package com.jandritsch.productsearch

import org.json4s._

class ProductWorkflow(
  val productManager:ProductManager,
  val historyManager:HistoryManager,
  val userInput:UserInput,
  val userOutput:UserOutput
) {

  def run(productNames:Iterator[String]) {
    for (name <- productNames) {
      val product = productManager.findProductByName(name)
      if (product == None) {
        userOutput.display("That product was not found. Would you like to add it: ", false)
        val input = userInput.getShouldAddProduct.toLowerCase
        val addIt = (input == "yes" || input == "y")
        productWasNotfound(name, addIt)
      } else {
        productWasFound(name, product.get)
      }
      userOutput.display("Enter the product name: ", false)
    }
  }

  private def productWasNotfound(name:String, shouldAdd:Boolean) = {
    historyManager.addHistory(name, false)
    if (shouldAdd) {
      userOutput.display("Enter the price for '" + name + "': ", false)
      val price:Double = userInput.getNewProductPrice.toDouble
      userOutput.display("Enter the quantity for '" + name + "': ", false)
      val quantity:Int = userInput.getNewProductQuantity.toInt
      productManager.addProduct(name, price, quantity)
      userOutput.display("Your product has been added!", true)
    }
  }

  private def productWasFound(name:String, product:JValue) {
    historyManager.addHistory(name, true)
    displayProductInfo(productManager.getProductInfo(product))
  }

  private def displayProductInfo(productInfo:(String,String,String)) = {
    val (name, price, qty) = productInfo
    userOutput.display("Name: " + name, true)
    userOutput.display("Price: $" + price, true)
    userOutput.display("Quantity on hand:" + qty, true)
  }

}
