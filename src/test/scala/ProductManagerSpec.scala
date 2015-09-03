package com.jandritsch.productsearch

import org.scalatest._
import org.json4s._
import org.json4s.native.JsonMethods._
import java.io.{File, BufferedWriter, FileWriter}

class ProductManagerSpec extends FunSpec with BeforeAndAfter {

  val dataStore = new InMemoryDataStore()
  var productManager:ProductManager = _

  before {
    dataStore.writeObject(
      parse(
        """
        { 
          "products": [
            { "name": "product1", "price": 3.99, "quantity": 2 },
            { "name": "product2", "price": 1.99, "quantity": 5 }
          ]
        }
        """
      )
    )
    productManager = new ProductManager(dataStore)
  }

  describe("#findProductByName") {
    it("accepts a product name and returns a Some containing the found product") {
      // setup
      var savedProducts = dataStore.read
      val entries = (savedProducts \ "products").children
      val product2 = entries(1)

      // act
      val result = productManager.findProductByName("product2")

      // assert
      assert(result === Some(product2))
    }

    it("is case-insensitive") {
      // setup
      var savedProducts = dataStore.read
      val entries = (savedProducts \ "products").children
      val product2 = entries(1)

      // act
      val result = productManager.findProductByName("PrOdUcT2")

      // assert
      assert(result === Some(product2))
    }

    it("returns None if the product was not found") {
      // setup

      // act
      val result = productManager.findProductByName("does not exist")

      // assert
      assert(result === None)
    }
  }

  describe("#addProduct") {
    it("accepts a name, price, and quantity and writes it to the products file") {
      // setup

      // act
      productManager.addProduct("product3", 5.99, 7)

      // assert
      var savedProducts = dataStore.read
      val entries = (savedProducts \ "products").children
      val entry1 = entries(0)
      val entry2 = entries(1)
      val entry3 = entries(2)

      assert(entries.length === 3)

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
      // setup
      val productManager = new ProductManager(dataStore)
      var savedProducts = dataStore.read
      val entries = (savedProducts \ "products").children
      val product2 = entries(1)

      // act
      val (name, price, quantity) = productManager.getProductInfo(product2)

      // assert
      assert(name === "product2")
      assert(price === "1.99")
      assert(quantity === "5")
    }
  }
}
