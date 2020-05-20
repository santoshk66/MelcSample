package com.moubjo.melcsample

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class Categorie4ListingAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Parcelable {
    var list:ArrayList<Cate4>? = null
    var context: Context? = null
    var listener:OnItemClickListener? = null

    interface OnItemClickListener{
        fun item4Click(name:String, code: Int, childrenCount: Int)
    }
    constructor(parcel: Parcel) : this() {

    }

    constructor(context: Context, listOfCate3: ArrayList<Cate4>, listener:OnItemClickListener) : this() {
        this.list = listOfCate3
        this.context = context
        this.listener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v = LayoutInflater.from(context).inflate(R.layout.cate4listing_layout, parent, false)
        return InstrumentHolder(v)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as InstrumentHolder).bind(list!![position].name!!, list!![position].code!!, list!![position].productCount!!,  listener!!)

    }

    class InstrumentHolder(view: View): RecyclerView.ViewHolder(view){

        var v = view as ConstraintLayout
        var nameV = view.findViewById<TextView>(R.id.cate4listing_name)
        var cate4listing_itemsCountV = view.findViewById<TextView>(R.id.cate4listing_itemsCount)


        fun bind(name:String, code:Int,productCount:Int,listener: OnItemClickListener){
            nameV.text = name
            //cate4listing_itemsCountV.text = "$productCount items"


            v.setOnClickListener {
                listener.item4Click(name, code, productCount)
            }


        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Categorie4ListingAdapter> {
        override fun createFromParcel(parcel: Parcel): Categorie4ListingAdapter {
            return Categorie4ListingAdapter(parcel)
        }

        override fun newArray(size: Int): Array<Categorie4ListingAdapter?> {
            return arrayOfNulls(size)
        }
    }


}