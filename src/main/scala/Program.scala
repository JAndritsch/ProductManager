package com.jandritsch.productsearch

import scala.io.Source.stdin

object Program {
  def main(args: Array[String]) {
    val productManager = new ProductManager("src/main/resources/products.json")
    val historyManager = new HistoryManager("src/main/resources/history.json")
    val userInput = new DefaultUserInput()
    val productWorkflow = new ProductWorkflow(
      productManager,
      historyManager,
      userInput,
      true
    )

    print("Enter the product name: ")
    productWorkflow.run(stdin.getLines)
  }
}
