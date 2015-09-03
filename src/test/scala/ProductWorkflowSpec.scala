package com.jandritsch.productsearch

import org.scalatest.FunSpec

class MockUserInput extends UserInput {
  var input:String = ""
  def getUserInput:String = input
}

class ProductWorkflowSpec extends FunSpec {

  val productManager = new ProductManager("src/test/resources/products.json")
  val historyManager = new HistoryManager("src/test/resources/history.json")
  val userInput = new MockUserInput()
  val productWorkflow = new ProductWorkflow(
    productManager,
    historyManager,
    userInput,
    false
  )

  describe("run") {
    it(""){
      //productWorkflow.run(stdin.getLines)
    }
  }
}

