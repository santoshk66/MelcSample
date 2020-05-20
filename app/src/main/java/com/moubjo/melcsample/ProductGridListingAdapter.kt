package com.moubjo.melcsample

import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductGridListingAdapter() :RecyclerView.Adapter<RecyclerView.ViewHolder>(), Parcelable  {
    var list:ArrayList<Product>? = null
    var context: Context? = null

    constructor(parcel: Parcel) : this()

    constructor(context: Context, listOfInstrument: ArrayList<Product>) : this() {
        this.list = listOfInstrument
        this.context = context
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v = LayoutInflater.from(context).inflate(R.layout.product_grid_layout, parent, false)
        return InstrumentHolder(v)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as InstrumentHolder).bind(list!![position].sku!!, list!![position].categoryId!!)

    }

    class InstrumentHolder(view: View): RecyclerView.ViewHolder(view){

        var v = view
        var skuView = view.findViewById<TextView>(R.id.product_grid_sku)
        var categoryIdView = view.findViewById<TextView>(R.id.product_grid_cartId)


        fun bind(sku:String, categoryId:String){
            skuView.text = sku
            categoryIdView.text = categoryId


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