package com.moubjo.melcsample

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.fragment_home_fagement.view.*

class CartActivity : AppCompatActivity(), CartItemAdapter.OnItemClickListener {

    var operso = OPerso()
    var listOfProduct = ArrayList<CartProduct>()
    var db:DbManager? = null
    var adapter:CartItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val actionbar = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)

        db = DbManager(this)

        val itemDecorator =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)

        var cart_recycler = cart_recycler
        cart_recycler.addItemDecoration(
                DividerItemDecoration(
                    this,
                    DividerItemDecoration.VERTICAL
                )
            )

        adapter = CartItemAdapter(this, listOfProduct, this)
        cart_recycler.layoutManager = LinearLayoutManager(this)
        cart_recycler.adapter = adapter

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        loadQuery("%")
    }
    fun loadQuery(title:String){
        var col = arrayOf("ID","sku","name","price","quantity","picture")
        var argList = arrayOf(title)

        val cursor = db!!.query("cart", col,"name like ?",argList,"ID")
        listOfProduct.clear()

        if(cursor.moveToFirst()){
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val sku = cursor.getString(cursor.getColumnIndex("sku"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val price = cursor.getString(cursor.getColumnIndex("price"))
                val quantity = cursor.getString(cursor.getColumnIndex("quantity"))
                val picture = cursor.getString(cursor.getColumnIndex("picture"))
                listOfProduct.add(CartProduct(ID, sku, price.toDouble(), name, quantity.toInt(), picture))
            }while (cursor.moveToNext())
        }
        if(listOfProduct.size == 0) {
            cart_cart.visibility = View.GONE
            cart_emptyCart.visibility = View.VISIBLE
        }
        adapter!!.notifyDataSetChanged()
        loadDeatils("%")
    }
    fun loadDeatils(title:String){
        var col = arrayOf("price","quantity")
        var argList = arrayOf(title)

        var totalQuantity = 0
        var totalPrice = 0.0
        val cursor = db!!.query("cart", col,"name like ?",argList,"ID")

        if(cursor.moveToFirst()){
            do {
                val price = cursor.getString(cursor.getColumnIndex("price")).toDouble()
                val quantity = cursor.getString(cursor.getColumnIndex("quantity")).toInt()
                totalQuantity += quantity
                totalPrice += (price*quantity)
            }while (cursor.moveToNext())
        }

        cart_count.text = totalQuantity.toString()+" items"
        cart_summaryPrice.text = totalPrice.toString()
        cart_totalPrice.text = totalPrice.toString()

    }
    fun dial(v:View){
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+"+233 56 111 2777"))
        startActivity(intent)
    }

    fun checkout(view: View){
        var intent = Intent(this, CheckoutInfo::class.java)
        startActivity(intent)
    }

    override fun onItemClick(position: Int, ID:Int) {
        var am = listOfProduct[position].price?.times(listOfProduct[position].quantity!!)

        var argsList = arrayOf(ID.toString())
        db!!.delete("cart","ID = ?",argsList)
        listOfProduct.clear()
        loadQuery("%")
    }

    fun startShopping(v:View){
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
