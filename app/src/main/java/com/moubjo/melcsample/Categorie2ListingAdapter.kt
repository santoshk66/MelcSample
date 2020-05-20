package com.moubjo.melcsample

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Categorie2ListingAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Parcelable {
    var list:ArrayList<Cate2>? = null
    var context: Context? = null
    var listener:OnItemClickListener? = null

    interface OnItemClickListener{
        fun itemClick(positionInTheList:Int,name:String, code: Int, childrenCount: Int)
    }
    constructor(parcel: Parcel) : this() {

    }

    constructor(context: Context, listOfCate2: ArrayList<Cate2>, listener:OnItemClickListener) : this() {
        this.list = listOfCate2
        this.context = context
        this.listener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v = LayoutInflater.from(context).inflate(R.layout.cate2listing_layout, parent, false)
        return InstrumentHolder(v)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as InstrumentHolder).bind(list!![position].positionInTheList!!,list!![position].name!!, list!![position].code!!, list!![position].childrenCount!!, listener!!)

    }

    class InstrumentHolder(view: View): RecyclerView.ViewHolder(view){

        var v = view
        var nameV = view.findViewById<TextView>(R.id.cate2listing_name)


        fun bind(positionInTheList: Int, name:String, code:Int, childrenCount:Int,listener: OnItemClickListener){
            nameV.text = name


            v.setOnClickListener {
                listener.itemClick(positionInTheList, name, code, childrenCount)
            }

            /*v.setOnClickListener {
                var intent = Intent(it.context, ProductDetails::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    it.putExtra("sku",sku)
                }
                it.context.startActivity(intent)
            }*/
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Categorie2ListingAdapter> {
        override fun createFromParcel(parcel: Parcel): Categorie2ListingAdapter {
            return Categorie2ListingAdapter(parcel)
        }

        override fun newArray(size: Int): Array<Categorie2ListingAdapter?> {
            return arrayOfNulls(size)
        }
    }


}