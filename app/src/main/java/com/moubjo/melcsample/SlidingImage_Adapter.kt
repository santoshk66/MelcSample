package com.moubjo.melcsample

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler
import com.moubjo.melcsample.ProductDetails.Companion.imageModelArrayList
import com.squareup.picasso.Picasso
import com.viven.imagezoom.ImageZoomHelper

class SlidingImage_Adapter(private val context: Context) : PagerAdapter() {
    private val inflater: LayoutInflater


    init {
        inflater = LayoutInflater.from(context)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return imageModelArrayList!!.size

    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false)!!

        val imageView = imageLayout
            .findViewById(R.id.image) as ImageView

        //println("Filee: https://www.melcomonline.com/media/catalog/product/cache/image/620x678/e9c3970ab036de70892d86c6d221abfe/"+imageModelArrayList[position].getImage_drawables())

        Picasso.with(context)
            .load("https://www.melcomonline.com/media/catalog/product/cache/image/620x678/e9c3970ab036de70892d86c6d221abfe/${imageModelArrayList!![position].getImage_drawables()}")
            .placeholder(R.drawable.productplace)
            .error(R.drawable.icons_shopping_cart)
            .into(imageView);
        imageView.setOnClickListener {
                var intent = Intent(context, Zoomer::class.java)
                intent.putExtra("currentItem", position)

                context.startActivity(intent)
            }
            /*imageView.setOnTouchListener(ImageMatrixTouchHandler(view.getContext()))
            ImageZoomHelper.setViewZoomable(imageView)*/

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