package com.moubjo.melcsample

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DbManager {

    //Databse Info
    val dbName = "MelcomOnline"
    val dbVersion = 1

    //Table cart and columns
    val tableName = "cart"
    var colId = "ID"
    var colSku = "sku"
    var colPicture = "picture"
    var colName = "name"
    var colPrice =  "price"
    var colQuantity = "quantity"

    var tableNameW = "wishlist"
    var colIdW = "ID"
    var colSkuW = "sku"
    var colNameW = "name"
    var colPriceW = "price"
    var colPictureW = "picture"

    var tableNameV = "viewed"
    var colIdV = "ID"
    var colSkuV = "sku"
    var colNameV = "name"
    var colPriceV = "price"
    var colPictureV = "picture"

    //Sql statements
    val sqlCreateStatement =
        "CREATE TABLE IF NOT EXISTS $tableName($colId INTEGER PRIMARY KEY, $colSku TEXT, $colPicture TEXT, $colName TEXT, $colPrice REAL, $colQuantity INTEGER)"

    val sqlCreateStatementW =
        "CREATE TABLE IF NOT EXISTS $tableNameW($colIdW INTEGER PRIMARY KEY, $colSkuW TEXT, $colNameW TEXT, $colPriceW REAL, $colPictureW Text)"

    val sqlCreateStatementV =
        "CREATE TABLE IF NOT EXISTS $tableNameV($colIdV INTEGER PRIMARY KEY, $colSkuV TEXT, $colNameV TEXT, $colPriceV REAL, $colPictureV Text)"

    var SQLDB: SQLiteDatabase? = null

    constructor(context: Context) {
        var db = SQLDATABASENOTES(context)
        SQLDB = db.writableDatabase
    }

    inner class SQLDATABASENOTES : SQLiteOpenHelper {
        var context: Context? = null

        constructor(context: Context) : super(context, dbName, null, dbVersion) {
            this.context = context
        }

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlCreateStatement)
            db!!.execSQL(sqlCreateStatementW)
            db!!.execSQL(sqlCreateStatementV)
            //Toast.makeText(this.context, "Databse created", Toast.LENGTH_SHORT).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP table IF EXISTS $tableName")

        }
    }

    fun insert(tName:String, values: ContentValues):Long{
        var id = SQLDB!!.insert(tName,"",values)
        return id
    }

    fun query(tName:String, projection:Array<String>,selection:String,selectionArgs:Array<String>,sortArgs:String):Cursor{
        var qb = SQLiteQueryBuilder()
        qb.tables = tName
        var cursor = qb.query(SQLDB, projection, selection,selectionArgs, null, null, sortArgs)
        return cursor
    }

    fun delete(tName:String, projection:String, selectArgs:Array<String>):Int
    {
        var count = SQLDB!!.delete(tName,projection,selectArgs)
        return count
    }

    fun update(tName:String, values:ContentValues, projection:String, argsList:Array<String>):Int{
        var count = SQLDB!!.update(tName,values,projection,argsList)
        return count
    }
}