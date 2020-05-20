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
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class GridAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Parcelable {
    var list:ArrayList<WishProduct>? = null
    var context: Context? = null
    var listener:OnItemClickListener? = null
    var db:DbManager? = null


    constructor(parcel: Parcel) : this() {

    }

    interface OnItemClickListener {
        fun onItemClick(item:Int, ID:Int, v:View)
    }


    constructor(context: Context, listOfCartProduct: ArrayList<WishProduct>, listener:OnItemClickListener) : this() {
        this.list = listOfCartProduct
        this.context = context
        this.listener = listener
        this.db = DbManager(context)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v = LayoutInflater.from(context).inflate(R.layout.product_wishlist_layout, parent, false)
        return InstrumentHolder(v)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as InstrumentHolder).bind(position,list!![position].ID!!,list!![position].sku!!, list!![position].name!!, list!![position].price!!,  list!![position].picture!!, list!![position].inTheWishlist!!, listener!!)

    }

    inner class InstrumentHolder(view: View): RecyclerView.ViewHolder(view){
        var v = view
        var nameV = view.findViewById<TextView>(R.id.pWishlist_name)
        var priceV = view.findViewById<TextView>(R.id.pWishlist_price)
        var removeV = view.findViewById<ImageView>(R.id.pWishlist_fav)
        var imageV = view.findViewById<ImageView>(R.id.pWishlist_picture)



        fun bind(position: Int,ID:Int, sku:String, name:String, price:Double, picture:String, inTheWishlist:Int, listener: OnItemClickListener){

            nameV.text = name
            priceV.text = "GH₵ "+ price.toString()

            if(inTheWishlist == 0 ){
                removeV.setImageResource(R.drawable.icons8_heart_outline)
            }


            Picasso.with(context)
                .load("https://www.melcomonline.com/media/catalog/product/cache/image/620x678/e9c3970ab036de70892d86c6d221abfe/$picture")
                .placeholder(R.drawable.productplace)
                .error(R.drawable.icons_shopping_cart)
                .into(imageV);
            removeV.setOnClickListener {
                listener.onItemClick(position, ID, v)
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

    companion object CREATOR : Parcelable.Creator<GridAdapter> {
        override fun createFromParcel(parcel: Parcel): GridAdapter {
            return GridAdapter(parcel)
        }

        override fun newArray(size: Int): Array<GridAdapter?> {
            return arrayOfNulls(size)
        }
    }


}