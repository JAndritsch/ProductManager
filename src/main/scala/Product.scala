package com.jandritsch.productsearch

import org.json4s._
import org.json4s.native.JsonMethods._

object Product {

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

  def addProduct(product:JObject, products:JValue):JValue = {
    products.merge(
      JObject(List(JField("products", JArray(List(product)))))
    )
  }

}
