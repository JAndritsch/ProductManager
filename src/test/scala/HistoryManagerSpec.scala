package com.jandritsch.productsearch

import org.scalatest._
import org.json4s._
import org.json4s.native.JsonMethods._
import java.io.{File, BufferedWriter, FileWriter}

class HistoryManagerSpec extends FunSpec with BeforeAndAfter {

  val dataStore = new InMemoryDataStore()

  before {
    dataStore.writeObject(parse("""{ "history": [] }"""))
  }

  describe("#addHistory") {
    it("accepts a search term and whether or not it was found and writes it to history") {
      // setup
      val historyManager = new HistoryManager(dataStore)

      // act
      historyManager.addHistory("product1", false)
      historyManager.addHistory("product2", true)

      // assert
      var savedHistory = dataStore.read
      val entries = (savedHistory \ "history").children
      val entry1 = entries(0)
      val entry2 = entries(1)

      assert(entries.length === 2)
      assert((entry1 \ "name").values === "product1")
      assert((entry1 \ "found").values === false)
      assert((entry1 \ "searchedAt").values !== None)
      assert((entry2 \ "name").values === "product2")
      assert((entry2 \ "found").values === true)
      assert((entry2 \ "searchedAt").values !== None)
    }

  }
}
