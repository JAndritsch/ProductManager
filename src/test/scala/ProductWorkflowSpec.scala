package com.jandritsch.productsearch

import org.scalatest._
import java.io.{File, BufferedWriter, FileWriter}
import java.util.ArrayList

class MockUserInput extends UserInput {
  var shouldAddProduct:String = ""
  var newProductPrice:String = ""
  var newProductQuantity:String = ""

  def getShouldAddProduct:String = shouldAddProduct
  def getNewProductPrice:String = newProductPrice
  def getNewProductQuantity:String = newProductQuantity
}

class MockUserOutput extends UserOutput {
  var displayed:ArrayList[String] = new ArrayList()

  def display(text:String, newLine:Boolean) = {
    displayed.add(text)
  }
}

class ProductWorkflowSpec extends FunSpec with BeforeAndAfter {

  val productsFilePath = "src/test/resources/products.json"
  val historyFilePath = "src/test/resources/history.json"

  var productManager:ProductManager = _
  var historyManager:HistoryManager = _
  var userInput:MockUserInput = _
  var userOutput:MockUserOutput = _
  var productWorkflow:ProductWorkflow = _

  before {
    // Set up sample products file
    var file = new File(productsFilePath)
    var bw = new BufferedWriter(new FileWriter(file))
    bw.write(
      """
      { 
        "products": [
          { "name": "product1", "price": 3.99, "quantity": 2 },
          { "name": "product2", "price": 1.99, "quantity": 5 }
        ]
      }
      """
      )
    bw.close()
    productManager = new ProductManager(productsFilePath)

    // Set up sample history file
    file = new File(historyFilePath)
    bw = new BufferedWriter(new FileWriter(file))
    bw.write(
      """
      { 
        "history": [
        ]
      }
      """
      )
    bw.close()
    historyManager = new HistoryManager(historyFilePath)

    userInput = new MockUserInput()
    userOutput = new MockUserOutput()

    // Instantiate our class under test
    productWorkflow = new ProductWorkflow(
      productManager,
      historyManager,
      userInput,
      userOutput 
    )
  }

  describe("#run") {

    describe("when the product was found") {
      it(""){
      }
    }

  }

}

