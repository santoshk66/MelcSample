package com.moubjo.melcsample

import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cart_item.view.*

class CartItemAdapter () : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Parcelable {
    var list:ArrayList<CartProduct>? = null
    var context: Context? = null
    var listener:OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(item:Int, ID:Int)
    }

    constructor(parcel: Parcel) : this()

    constructor(context: Context, listOfCartProduct: ArrayList<CartProduct>, listener:OnItemClickListener) : this() {
        this.list = listOfCartProduct
        this.context = context
        this.listener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false)
        return InstrumentHolder(v)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as InstrumentHolder).bind(position,list!![position].ID!!,list!![position].sku!!, list!![position].name!!, list!![position].price!!, list!![position].quantity!!, list!![position].picture!!,listener!!)

    }

    inner class InstrumentHolder(view: View): RecyclerView.ViewHolder(view){
        var v = view
        var skuV = view.findViewById<TextView>(R.id.cart_item_pSku)
        var nameV = view.findViewById<TextView>(R.id.cart_item_pName)
        var priceV = view.findViewById<TextView>(R.id.cart_item_pPrice)
        var quantityV = view.findViewById<TextView>(R.id.cart_item_pQuantity)
        var removeV = view.findViewById<ImageView>(R.id.cart_item_delBut)
        var imageV = view.findViewById<ImageView>(R.id.cart_item_picture)

        fun bind(position: Int,ID:Int, sku:String, name:String, price:Double, quantity:Int, picture:String, listener: OnItemClickListener){

            skuV.text = sku
            nameV.text = name
            priceV.text = price.toString()
            quantityV.text = quantity.toString()
           Picasso.with(context)
                .load("https://www.melcomonline.com/media/catalog/product/cache/image/620x678/e9c3970ab036de70892d86c6d221abfe/$picture")
                .placeholder(R.drawable.productplace)
                .error(R.drawable.icons_shopping_cart)
                .into(imageV);
            removeV.setOnClickListener {
                listener.onItemClick(position, ID)
            }
            v.setOnClickListener {
                var intent = Intent(it.context, ProductDetails::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    it.putExtra("sku",sku)
                }
                it.context.startActivity(intent)
            }


        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartItemAdapter> {
        override fun createFromParcel(parcel: Parcel): CartItemAdapter {
            return CartItemAdapter(parcel)
        }

        override fun newArray(size: Int): Array<CartItemAdapter?> {
            return arrayOfNulls(size)
        }
    }
}