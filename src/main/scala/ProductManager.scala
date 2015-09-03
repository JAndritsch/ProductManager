package com.jandritsch.productsearch

import org.json4s._
import org.json4s.native.JsonMethods._

class ProductManager(val dataStore:DataStore) {

  var products = dataStore.read

  def findProductByName(name:String):Option[JValue] = {
    (products \ "products").children.find((p) => 
      (p \ "name").values.toString.toLowerCase == name.toLowerCase
    )
  }

  def addProduct(name:String, price:Double, quantity:Int) = {
    val product = makeProduct(name, price, quantity)
    products = products.merge(
      JObject(List(JField("products", JArray(List(product)))))
    )
    dataStore.writeObject(products)
  }

  def getProductInfo(product:JValue):(String,String,String) = {
    (
      (product \ "name").values.toString,
      (product \ "price").values.toString,
      (product \ "quantity").values.toString
    )
  }

  private def makeProduct(name:String, price:Double, quantity:Int):JObject = {
    JObject(
      List(
        JField("name", JString(name)),
        JField("price", JDouble(price)),
        JField("quantity", JInt(quantity))
      )
    )
  }

}
