package com.moubjo.melcsample

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_viewed.*

class Viewed : AppCompatActivity(), GridAdapter.OnItemClickListener {

    var operso = OPerso()
    var listOfProduct = ArrayList<WishProduct>()
    var db:DbManager? = null
    var adapter:GridAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewed)

        val actionbar = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)

        db = DbManager(this)

        val itemDecorator =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)

        viewed_recycler.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

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
        var col = arrayOf("ID","sku","name","price","picture")
        var argList = arrayOf(title)

        val cursor = db!!.query("viewed", col,"name like ?",argList,"ID DESC")
        listOfProduct.clear()

        if(cursor.moveToFirst()){
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val sku = cursor.getString(cursor.getColumnIndex("sku"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val price = cursor.getString(cursor.getColumnIndex("price"))
                val picture = cursor.getString(cursor.getColumnIndex("picture"))
                val inTheWishlist = checkWishlistExistance(sku)
                listOfProduct.add(WishProduct(ID, sku, price.toDouble(), name,  picture, inTheWishlist))
            }while (cursor.moveToNext())
        }
        /* if(listOfProduct.size == 0) {
             cart_cart.visibility = View.GONE
             cart_emptyCart.visibility = View.VISIBLE
         }*/
        adapter = GridAdapter(this, listOfProduct, this)
        viewed_recycler.layoutManager = GridLayoutManager(this,2)
        viewed_recycler.adapter = adapter
    }

    fun checkWishlistExistance(sk:String):Int{
        var IDD = 0;
        var col = arrayOf("ID")
        var argList = arrayOf(sk)
        val cursor = db!!.query("wishlist", col,"sku = ?",argList,"ID")
        if(cursor.moveToFirst()){
            IDD = cursor.getInt(cursor.getColumnIndex("ID"))
        }
        return IDD
    }

    override fun onItemClick(item: Int, ID: Int, v: View) {

    }


}
