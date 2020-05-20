package com.moubjo.melcsample

class WishProduct {
    var ID:Int? = null
    var sku:String? = null
    var price:Double? = null
    var name:String? = null
    var picture:String? = null
    var inTheWishlist:Int? = null

    constructor(ID:Int, sku:String, price:Double, name:String,  picture:String, inTheWishList:Int){
        this.ID = ID
        this.sku = sku
        this.price = price
        this.name = name
        this.picture = picture
        this.inTheWishlist = inTheWishList
    }
}