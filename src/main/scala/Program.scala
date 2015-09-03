package com.jandritsch.productsearch

import scala.io.Source.stdin
import org.json4s._

object Program {
  def main(args: Array[String]) {

    val productManager = new ProductManager()
    val historyManager = new HistoryManager()

    print("Enter the product name: ")
    for (name <- stdin.getLines) {
      val product = productManager.findProductByName(name)
      if (product == None) {
        historyManager.addHistory(name, false)
        print("Couldn't find that product. Would you like to add it: ")
        if (readLine.toLowerCase == "yes") {
          print("Enter the price for '" + name + "': ")
          val price = readLine.toDouble
          print("Enter the quantity for '" + name + "': ")
          val quantity = readLine.toInt
          productManager.addProduct(name, price, quantity)
          println("Your product has been added!")
        }
      } else {
        historyManager.addHistory(name, true)
        displayProductInfo(productManager.getProductInfo(product.get))
      }
      print("Enter the product name: ")
    }
  }

  def displayProductInfo(productInfo:(String,String,String)) = {
    val (name, price, qty) = productInfo
    println("Name: " + name)
    println("Price: $" + price)
    println("Quantity on hand:" + qty)
  }


}
