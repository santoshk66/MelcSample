package com.moubjo.melcsample

class CartProduct {
    var ID:Int? = null
    var sku:String? = null
    var price:Double? = null
    var name:String? = null
    var quantity:Int? = null
    var picture:String? = null

    constructor(ID:Int, sku:String, price:Double, name:String, quantity:Int, picture:String){
        this.ID = ID
        this.sku = sku
        this.price = price
        this.quantity = quantity
        this.name = name
        this.picture = picture
    }
}