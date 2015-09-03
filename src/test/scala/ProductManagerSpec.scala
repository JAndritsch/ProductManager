package com.jandritsch.productsearch

import org.scalatest._
import java.io.{File, BufferedWriter, FileWriter}

class ProductManagerSpec extends FunSpec with BeforeAndAfter {

  val productsFilePath = "src/test/resources/products.json"
  var productManager:ProductManager = _

  before {
    // Set up sample products file
    val file = new File(productsFilePath)
    val bw = new BufferedWriter(new FileWriter(file))
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
  }

  describe("#findProductByName") {
    it("accepts a product name and returns a Some containing the found product") {
      var savedProducts = FileUtils.readFileAsJson(productsFilePath)
      val entries = (savedProducts \ "products").children
      val product2 = entries(1)
      assert(productManager.findProductByName("product2") === Some(product2))
    }

    it("is case-insensitive") {
      var savedProducts = FileUtils.readFileAsJson(productsFilePath)
      val entries = (savedProducts \ "products").children
      val product2 = entries(1)
      assert(productManager.findProductByName("PrOdUcT2") === Some(product2))
    }

    it("returns None if the product was not found") {
      assert(productManager.findProductByName("does not exist") === None)
    }
  }

  describe("#addProduct") {
    it("accepts a name, price, and quantity and writes it to the products file") {
      productManager.addProduct("product3", 5.99, 7)
      var savedProducts = FileUtils.readFileAsJson(productsFilePath)
      val entries = (savedProducts \ "products").children
      assert(entries.length === 3)

      val entry1 = entries(0)
      val entry2 = entries(1)
      val entry3 = entries(2)

      assert((entry1 \ "name").values === "product1")
      assert((entry1 \ "price").values === 3.99)
      assert((entry1 \ "quantity").values === 2)

      assert((entry2 \ "name").values === "product2")
      assert((entry2 \ "price").values === 1.99)
      assert((entry2 \ "quantity").values === 5)

      assert((entry3 \ "name").values === "product3")
      assert((entry3 \ "price").values === 5.99)
      assert((entry3 \ "quantity").values === 7)
    }
  }

  describe("#getProductInfo") {
    it("accepts a product and returns a tuple containing its name, price, and quantity") {
      val productManager = new ProductManager(productsFilePath)
      var savedProducts = FileUtils.readFileAsJson(productsFilePath)
      val entries = (savedProducts \ "products").children
      val product2 = entries(1)

      val (name, price, quantity) = productManager.getProductInfo(product2)
      assert(name === "product2")
      assert(price === "1.99")
      assert(quantity === "5")
    }
  }
}
