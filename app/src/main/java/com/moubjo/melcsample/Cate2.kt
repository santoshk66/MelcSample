package com.moubjo.melcsample

class Cate2 {
    var name:String? = null
    var code:Int? = null
    var childrenCount:Int? = null
    var positionInTheList:Int? = null
    constructor(positionInTheList:Int, name:String, code:Int, childrenCount:Int){
        this.positionInTheList = positionInTheList
        this.name = name
        this.code = code
        this.childrenCount = childrenCount
    }
}