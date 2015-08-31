package com.jandritsch.productsearch
import scala.io.Source
import scala.io.Source.stdin
import org.json4s._
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization.write
import java.io._
import java.util.Date
import java.text.SimpleDateFormat

object Program {
  def main(args: Array[String]) {

    val productsFilePath = "src/main/resources/products.json"
    val productsFile = Source.fromFile(productsFilePath)
    var products = parse(productsFile.mkString)
    productsFile.close

    val historyFilePath = "src/main/resources/history.json"
    val historyFile = Source.fromFile(historyFilePath)
    var history = parse(historyFile.mkString)
    historyFile.close

    print("Enter the product name: ")
    for (name <- stdin.getLines) {
      val product = findProduct(products, name)
      if (product == None) {
        val newHistory = makeHistory(name, false)
        history = addHistory(newHistory, history)
        writeToFile(historyFilePath, history)
        print("Couldn't find that product. Would you like to add it: ")
        if (readLine.toLowerCase == "yes") {
          print("Enter the price for '" + name + "': ")
          val price = readLine.toDouble
          print("Enter the quantity for '" + name + "': ")
          val quantity = readLine.toInt
          val newProduct = makeProduct(name, price, quantity)
          products = addProduct(newProduct, products)
          writeToFile(productsFilePath, products)
          println("Your product has been added!")
        }
      } else {
        val newHistory = makeHistory(name, true)
        history = addHistory(newHistory, history)
        writeToFile(historyFilePath, history)
        displayProductInfo(product.get)
      }
      print("Enter the product name: ")
    }
  }

  def findProduct(products:JValue, name:String):Option[JValue] = {
    (products \ "products").children.find((p) => 
      (p \ "name").values.toString.toLowerCase == name.toLowerCase
    )
  }

  def displayProductInfo(product:JValue) {
    println("Name: " + (product \ "name").values)
    println("Price: $" + (product \ "price").values)
    println("Quantity on hand:" + (product \ "quantity").values)
  }

  def makeProduct(name:String, price:Double, quantity:Int):JObject = {
    JObject(
      List(
        JField("name", JString(name)),
        JField("price", JDouble(price)),
        JField("quantity", JInt(quantity))
      )
    )
  }

  def makeHistory(term:String, found:Boolean):JObject = {
    val df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'")
    JObject(
      List(
        JField("name", JString(term)),
        JField("found", JBool(found)),
        JField("searchedAt", JString(df.format(new Date())))
      )
    )
  }

  def addHistory(newHistory:JObject, history:JValue):JValue = {
    history.merge(
      JObject(List(JField("history", JArray(List(newHistory)))))
    )
  }

  def addProduct(product:JObject, products:JValue):JValue = {
    products.merge(
      JObject(List(JField("products", JArray(List(product)))))
    )
  }

  def writeToFile(filePath:String, collection:JValue) {
    val writer = new BufferedWriter(
      new OutputStreamWriter(
        new FileOutputStream(filePath)
      )
    )
    implicit val formats = DefaultFormats
    val json = write(collection)
    writer.write(json)
    writer.close
  }

}
