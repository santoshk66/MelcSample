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

class ProductListingAdapter() :RecyclerView.Adapter<RecyclerView.ViewHolder>(), Parcelable {
    var list:ArrayList<Product>? = null
    var context: Context? = null

    constructor(parcel: Parcel) : this()

    constructor(context: Context, listOfInstrument: ArrayList<Product>) : this() {
        this.list = listOfInstrument
        this.context = context
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v = LayoutInflater.from(context).inflate(R.layout.product_layout, parent, false)
        return InstrumentHolder(v)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as InstrumentHolder).bind(list!![position].entityId!!,list!![position].sku!!,list!![position].price!!,list!![position].name!!,list!![position].picture!!,list!![position].categoryId!!)

    }

    inner class InstrumentHolder(view: View): RecyclerView.ViewHolder(view){
        var v = view
        var pictureV = view.findViewById<ImageView>(R.id.product_img)
        var nameV = view.findViewById<TextView>(R.id.product_name)
        var priceV = view.findViewById<TextView>(R.id.product_price)


        fun bind(entityId:Int, sku:String, price:Double, name:String, picture:String, categoryId:String){
            nameV.text = name
            priceV.text = "GH₵ "+price.toString()

            Picasso.with(context)
                .load("https://www.melcomonline.com/media/catalog/product/cache/image/620x678/e9c3970ab036de70892d86c6d221abfe/$picture")
                .placeholder(R.drawable.productplace)
                .error(R.drawable.icons_shopping_cart)
                .into(pictureV);

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

    companion object CREATOR : Parcelable.Creator<ProductListingAdapter> {
        override fun createFromParcel(parcel: Parcel): ProductListingAdapter {
            return ProductListingAdapter(parcel)
        }

        override fun newArray(size: Int): Array<ProductListingAdapter?> {
            return arrayOfNulls(size)
        }
    }
}