package com.moubjo.melcsample

class Cate3 {
    var name:String? = null
    var code:Int? = null
    var childrenCount:Int? = null
    var parentPositionInTheList:Int? = null
    var positionInTheList:Int? = null
    var productCount:Int? = null
    constructor(parentPositionInTheList:Int, positionInTheList:Int, name:String, code:Int, productCount:Int, childrenCount:Int){
        this.parentPositionInTheList = parentPositionInTheList
        this.positionInTheList = positionInTheList
        this.name = name
        this.code = code
        this.productCount = productCount
        this.childrenCount = childrenCount
    }
}