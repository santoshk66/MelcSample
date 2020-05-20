package com.moubjo.melcsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.daimajia.androidanimations.library.Techniques
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_wishlist.*
import kotlinx.android.synthetic.main.fragment_home_fagement.view.*

class Wishlist : AppCompatActivity(), GridAdapter.OnItemClickListener {

    var operso = OPerso()
    var listOfProduct = ArrayList<WishProduct>()
    var db: DbManager? = null
    var adapter: GridAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wishlist)

        val actionbar = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)

        db = DbManager(this)

        val itemDecorator =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)

        wishlist_recycler.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        adapter = GridAdapter(this, listOfProduct, this)
        wishlist_recycler.layoutManager = GridLayoutManager(this, 2)
        wishlist_recycler.adapter = adapter

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        loadQuery("%")
    }

    fun loadQuery(title: String) {
        var col = arrayOf("ID", "sku", "name", "price", "picture")
        var argList = arrayOf(title)

        val cursor = db!!.query("wishlist", col, "name like ?", argList, "ID DESC")
        listOfProduct.clear()

        if (cursor.moveToFirst()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val sku = cursor.getString(cursor.getColumnIndex("sku"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val price = cursor.getString(cursor.getColumnIndex("price"))
                val picture = cursor.getString(cursor.getColumnIndex("picture"))
                val inTheWishlist = checkWishlistExistance(sku)
                listOfProduct.add(WishProduct(ID, sku, price.toDouble(), name, picture,inTheWishlist))
            } while (cursor.moveToNext())
        }
        /* if(listOfProduct.size == 0) {
            cart_cart.visibility = View.GONE
            cart_emptyCart.visibility = View.VISIBLE
        }*/
        adapter!!.notifyDataSetChanged()
    }


    override fun onItemClick(item: Int, ID: Int, v: View) {
        var argsList = arrayOf(ID.toString())
        db!!.delete("wishlist", "ID = ?", argsList)
        operso.viewOut(Techniques.FlipOutY, v, 200)
        listOfProduct.clear()
        loadQuery("%")
        Toast.makeText(this, "Removed", Toast.LENGTH_SHORT).show()
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

}
