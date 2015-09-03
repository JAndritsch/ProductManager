package com.jandritsch.productsearch

import org.scalatest._
import scala.collection.mutable.MutableList
import java.io.{File, BufferedWriter, FileWriter}

class MockUserInput extends UserInput {
  var shouldAddProduct:String = ""
  var newProductPrice:String = ""
  var newProductQuantity:String = ""

  def getShouldAddProduct:String = shouldAddProduct
  def getNewProductPrice:String = newProductPrice
  def getNewProductQuantity:String = newProductQuantity
}

class MockUserOutput extends UserOutput {
  var entries:MutableList[String] = new MutableList()

  def display(text:String, newLine:Boolean) = {
    entries += text
  }
}

class ProductWorkflowSpec extends FunSpec with BeforeAndAfter {

  val productsFilePath = "src/test/resources/products-workflow.json"
  val historyFilePath = "src/test/resources/history-workflow.json"

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
      it("adds an entry to the history showing that the product was found"){
        // setup
        val enteredProducts = Iterator("product1")

        // act
        productWorkflow.run(enteredProducts)

        // assert
        val savedHistory = FileUtils.readFileAsJson(historyFilePath)
        val savedProducts = FileUtils.readFileAsJson(productsFilePath)
        val entries = (savedHistory \ "history").children
        val entry1 = entries(0)

        assert(entries.length === 1)
        assert((entry1 \ "name").values === "product1")
        assert((entry1 \ "found").values === true)
      }

      it("outputs the information of the product that was found") {
        // setup
        val enteredProducts = Iterator("product1")

        // act
        productWorkflow.run(enteredProducts)

        // assert
        assert(userOutput.entries.contains("Name: product1"))
        assert(userOutput.entries.contains("Price: $3.99"))
        assert(userOutput.entries.contains("Quantity on hand: 2"))
      }
    }

    describe("when the product was not found") {

      it("adds an entry to the history showing that the product was not found") {
        // setup
        val enteredProducts = Iterator("product3")
        userInput.shouldAddProduct = "No"

        // act
        productWorkflow.run(enteredProducts)

        // assert
        val savedHistory = FileUtils.readFileAsJson(historyFilePath)
        val savedProducts = FileUtils.readFileAsJson(productsFilePath)
        val entries = (savedHistory \ "history").children
        val entry1 = entries(0)

        assert(entries.length === 1)
        assert((entry1 \ "name").values === "product3")
        assert((entry1 \ "found").values === false)
      }

      it("asks the user if they would like to add the product") {
        // setup
        val enteredProducts = Iterator("product3")
        userInput.shouldAddProduct = "No"

        // act
        productWorkflow.run(enteredProducts)

        // assert
        assert(userOutput.entries.contains("That product was not found. Would you like to add it: "))
      }

      describe("and the user does not want to add the product") {
        it("does not ask the user to enter in any data for the product") {
          // setup
          val enteredProducts = Iterator("product3")
          userInput.shouldAddProduct = "No"

          // act
          productWorkflow.run(enteredProducts)

          // assert
          assert(!userOutput.entries.contains("Enter the price for 'product3': "))
          assert(!userOutput.entries.contains("Enter the quantity for 'product3': "))
          assert(!userOutput.entries.contains("Your product has been added!"))
        }

        it("does not add the product") {
          // setup
          val enteredProducts = Iterator("product3")
          userInput.shouldAddProduct = "No"

          // act
          productWorkflow.run(enteredProducts)

          // assert
          assert(productManager.findProductByName("product3") === None)
        }
      }

      describe("and the user wants to add the product") {
        it("asks the user to enter the new product information") {
          // setup
          val enteredProducts = Iterator("product3")
          userInput.shouldAddProduct = "Yes"
          userInput.newProductPrice = "14.99"
          userInput.newProductQuantity = "27"

          // act
          productWorkflow.run(enteredProducts)

          // assert
          assert(userOutput.entries.contains("Enter the price for 'product3': "))
          assert(userOutput.entries.contains("Enter the quantity for 'product3': "))
        }

        it("adds the product to the list") {
          // setup
          val enteredProducts = Iterator("product3")
          userInput.shouldAddProduct = "Y"
          userInput.newProductPrice = "14.99"
          userInput.newProductQuantity = "27"

          // act
          productWorkflow.run(enteredProducts)

          // assert
          val product = productManager.findProductByName("product3")
          val (name, price, quantity) = productManager.getProductInfo(product.get)
          assert(price === "14.99")
          assert(quantity === "27")
        }

        it("notifies the user that the product has been added") {
          // setup
          val enteredProducts = Iterator("product3")
          userInput.shouldAddProduct = "y"
          userInput.newProductPrice = "14.99"
          userInput.newProductQuantity = "27"

          // act
          productWorkflow.run(enteredProducts)

          // assert
          assert(userOutput.entries.contains("Your product has been added!"))
        }
      }
    }

  }

}

