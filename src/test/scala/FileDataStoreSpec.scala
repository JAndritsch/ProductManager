package com.jandritsch.productsearch

import scala.io.Source
import java.io._
import org.scalatest._
import org.json4s._
import org.json4s.native.JsonMethods._
import java.io.{File, BufferedWriter, FileWriter}

class FileDataStoreSpec extends FunSpec with BeforeAndAfter {

  val testFilePath = "src/test/resources/data-store-test.json"
  val fileDataStore = new FileDataStore(testFilePath)

  def setupTestFile(filePath:String, content:String) = {
    val writer = new BufferedWriter(
      new OutputStreamWriter(
        new FileOutputStream(filePath)
      )
    )
    writer.write(content)
    writer.close
  }

  def deleteTestFile(filePath:String) = {
    val file = new File(filePath)
    if (file.exists) {
      file.delete()
    }
  }

  after { deleteTestFile(testFilePath) }

  describe("#read") {
    it("reads a file's contents and returns a parsed JValue") {
      // setup
      setupTestFile(testFilePath, """{"stuff":"things"}""")

      // act
      val output = fileDataStore.read

      // assert
      assert((output \ "stuff").values === "things")
    }
  }

  describe("#writeObject") {
    it("accepts a JValue and writes it to the file") {
      // setup
      setupTestFile(testFilePath, "previously stored data")
      val contentToWrite = """{"stuff":"things"}"""
      val json:JValue = parse(contentToWrite)

      // act
      fileDataStore.writeObject(json)
      val contentWritten:String = Source.fromFile(testFilePath).mkString

      // assert
      assert(contentToWrite === contentWritten)
    }
  }
}
