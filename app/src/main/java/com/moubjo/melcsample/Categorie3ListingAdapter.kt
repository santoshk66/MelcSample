package com.moubjo.melcsample

import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class Categorie3ListingAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Parcelable {
    var list:ArrayList<Cate3>? = null
    var context: Context? = null
    var listener:OnItemClickListener? = null

    interface OnItemClickListener{
        fun item3Click(parentPositionInTheList:Int, positionInTheList:Int,name:String, code: Int, childrenCount: Int, v:ConstraintLayout)
        fun seeAllClick(code:Int)
    }
    constructor(parcel: Parcel) : this() {

    }

    constructor(context: Context, listOfCate3: ArrayList<Cate3>, listener:OnItemClickListener) : this() {
        this.list = listOfCate3
        this.context = context
        this.listener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v = LayoutInflater.from(context).inflate(R.layout.cate3listing_layout, parent, false)
        return InstrumentHolder(v)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as InstrumentHolder).bind(list!![position].parentPositionInTheList!!,list!![position].positionInTheList!!,list!![position].name!!, list!![position].code!!, list!![position].productCount!!, list!![position].childrenCount!!, listener!!)

    }

    inner class InstrumentHolder(view: View): RecyclerView.ViewHolder(view){

        var v = view as ConstraintLayout
        var nameV = view.findViewById<TextView>(R.id.cate2listing_name)
        var cate2listing_AllV = view.findViewById<TextView>(R.id.cate2listing_All)


        fun bind(parentPositionInTheList:Int, positionInTheList: Int, name:String, code:Int,productCount:Int, childrenCount:Int,listener: OnItemClickListener){
            nameV.text = name

            cate2listing_AllV.setOnClickListener{
                //Toast.makeText(context, "OKOK", Toast.LENGTH_SHORT).show()
                listener.seeAllClick(code)
            }

            v.setOnClickListener {
                listener.item3Click(parentPositionInTheList,positionInTheList, name, code, childrenCount,v)
            }


        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Categorie3ListingAdapter> {
        override fun createFromParcel(parcel: Parcel): Categorie3ListingAdapter {
            return Categorie3ListingAdapter(parcel)
        }

        override fun newArray(size: Int): Array<Categorie3ListingAdapter?> {
            return arrayOfNulls(size)
        }
    }


}