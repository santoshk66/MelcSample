package com.moubjo.melcsample

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import com.squareup.picasso.Picasso

class SliderHomeAdapter (private val context: Context, private val imageModelArrayList: ArrayList<ImageModel>) : PagerAdapter() {
    private val inflater: LayoutInflater


    init {
        inflater = LayoutInflater.from(context)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return imageModelArrayList.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false)!!

        val imageView = imageLayout
            .findViewById(R.id.image) as ImageView

        Picasso.with(context)
            .load(imageModelArrayList[position].getImage_drawables())
            .placeholder(R.drawable.productplace)
            .error(R.drawable.icons_shopping_cart)
            .into(imageView);

        imageView.setOnClickListener {
            var categoryId = 0
            when(position){
                0 -> categoryId = 1466
                1 -> categoryId = 1276
                2 -> categoryId = 1352
                3 -> categoryId = 1291
            }

            var intent = Intent(context, ProductListing::class.java)
            intent.putExtra("category", categoryId.toString())
            context.startActivity(intent)

        }
        view.addView(imageLayout, 0)

        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }
}