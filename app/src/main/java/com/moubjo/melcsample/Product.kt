package com.moubjo.melcsample

class Product {

    var entityId:Int? = null
    var sku:String? = null
    var price:Double? = null
    var name:String? = null
    var picture:String? = null
    var categoryId:String? = null

    constructor(entityId:Int, sku:String, price:Double, name:String, picture:String, categoryId:String){
        this.entityId = entityId
        this.sku = sku
        this.price = price
        this.name = name
        this.picture = picture
        this.categoryId = categoryId
    }
}