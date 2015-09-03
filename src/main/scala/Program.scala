package com.jandritsch.productsearch

import scala.io.Source.stdin

object Program {
  def main(args: Array[String]) {
    val productManager = new ProductManager(new FileDataStore("src/main/resources/products.json"))
    val historyManager = new HistoryManager(new FileDataStore("src/main/resources/history.json"))
    val userInput = new DefaultUserInput()
    val userOutput = new DefaultUserOutput()
    val productWorkflow = new ProductWorkflow(
      productManager,
      historyManager,
      userInput,
      userOutput
    )

    print("Enter the product name: ")
    productWorkflow.run(stdin.getLines)
  }
}
