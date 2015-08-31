package com.jandritsch.productsearch

import scala.io.Source.stdin
import org.json4s._

object Program {
  def main(args: Array[String]) {

    val productsFilePath = "src/main/resources/products.json"
    var products = FileUtils.readFileAsJson(productsFilePath)
    val historyFilePath = "src/main/resources/history.json"
    var history = FileUtils.readFileAsJson(historyFilePath)

    print("Enter the product name: ")
    for (name <- stdin.getLines) {
      val product = Product.findProduct(products, name)
      if (product == None) {
        val newHistory = History.makeHistory(name, false)
        history = History.addHistory(newHistory, history)
        FileUtils.writeToFile(historyFilePath, history)
        print("Couldn't find that product. Would you like to add it: ")
        if (readLine.toLowerCase == "yes") {
          print("Enter the price for '" + name + "': ")
          val price = readLine.toDouble
          print("Enter the quantity for '" + name + "': ")
          val quantity = readLine.toInt
          val newProduct = Product.makeProduct(name, price, quantity)
          products = Product.addProduct(newProduct, products)
          FileUtils.writeToFile(productsFilePath, products)
          println("Your product has been added!")
        }
      } else {
        val newHistory = History.makeHistory(name, true)
        history = History.addHistory(newHistory, history)
        FileUtils.writeToFile(historyFilePath, history)
        Product.displayProductInfo(product.get)
      }
      print("Enter the product name: ")
    }
  }

}
