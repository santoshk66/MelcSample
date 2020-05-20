package com.moubjo.melcsample

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_sub_categories_listing.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.collections.ArrayList

class SubCategoriesListing : AppCompatActivity(),Categorie2ListingAdapter.OnItemClickListener, Categorie3ListingAdapter.OnItemClickListener, Categorie4ListingAdapter.OnItemClickListener {

    var operso = OPerso()
    var categoriy2Listing = ArrayList<Cate2>()


    var adapter:Categorie2ListingAdapter? = null

    var jsonResultArray:JSONArray? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_categories_listing)
        title = "All categories"

        cate2HeaderName.text = resources.getString(R.string.mobileAndComputer)

        var cate2Url = "http://34.242.215.249/mag/sample.json"
        FetchCate2().execute(cate2Url)

        categoriy2Listing.add(Cate2(0, resources.getString(R.string.mobileAndComputer),1277, 4))

        adapter = Categorie2ListingAdapter(this, categoriy2Listing, this)
        categoriesListing.layoutManager = LinearLayoutManager(applicationContext)
        categoriesListing.adapter = adapter

    }

    inner class FetchCate2: AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }
        override fun doInBackground(vararg params: String?): String {
            try {
                val url = URL(params[0])
                var urlConnect = url.openConnection() as HttpURLConnection
                var resultString = operso.convertToString(urlConnect.inputStream)

                publishProgress(resultString)
            }catch (ex:Exception){

            }
            return ""
        }

        override fun onProgressUpdate(vararg values: String?) {
            var result = values[0]
            var json = JSONObject(result)
            var jsonArray = json.getJSONArray("children_data")
            jsonResultArray = jsonArray
            var size = jsonArray.length()-1
            categoriy2Listing.clear()
            for(i in 0..size){
                var jsonOb = jsonArray.getJSONObject(i)
                var name = jsonOb.getString("name")
                var code = jsonOb.getInt("id")
                var childrenCount = jsonOb.getJSONArray("children_data").length()
                println(name + "  "+ code+" "+childrenCount)
                categoriy2Listing.add(Cate2(i, name,code, childrenCount))
            }

            val itemDecorator =
                DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
            itemDecorator.setDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.divider)!!)

            categoriesListing.addItemDecoration(
                DividerItemDecoration(
                    applicationContext,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter!!.notifyDataSetChanged()

            var categoriy3Listing = ArrayList<Cate3>()
            var jsonOb = jsonResultArray!!.getJSONObject(0)
            var children = jsonOb.getJSONArray("children_data")
            var sizee = children.length()-1
            for(i in 0..sizee){
                var jsonOb = children.getJSONObject(i)
                var name = jsonOb.getString("name")
                var code = jsonOb.getInt("id")
                var productCount = jsonOb.getInt("product_count")
                var childrenCount = jsonOb.getJSONArray("children_data").length()
                categoriy3Listing.add(Cate3(0,i, name,code,productCount, childrenCount))
            }

            val itemDecoratorr =
                DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
            itemDecoratorr.setDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.divider)!!)

            categoriesListing.addItemDecoration(
                DividerItemDecoration(
                    applicationContext,
                    DividerItemDecoration.VERTICAL
                )
            )
            var adapterCate3 = Categorie3ListingAdapter(applicationContext, categoriy3Listing, this@SubCategoriesListing)
            cate3Listing.layoutManager = LinearLayoutManager(applicationContext)
            cate3Listing.adapter = adapterCate3
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)


        }

    }

    override fun itemClick(positionInTheList:Int,name: String, code: Int, childrenCount: Int) {
        if(childrenCount == 0){
            var intent = Intent(this,ProductListing::class.java)
            intent.putExtra("category",code.toString())
            startActivity(intent)
        }else{
            cate2HeaderName.text = name
            var categoriy3Listing = ArrayList<Cate3>()
            var jsonOb = jsonResultArray!!.getJSONObject(positionInTheList)
            var children = jsonOb.getJSONArray("children_data")
            var size = children.length()-1
            for(i in 0..size){
                var jsonOb = children.getJSONObject(i)
                var name = jsonOb.getString("name")
                var code = jsonOb.getInt("id")
                var productCount = jsonOb.getInt("product_count")
                var childrenCount = jsonOb.getJSONArray("children_data").length()
               categoriy3Listing.add(Cate3(positionInTheList,i, name,code,productCount, childrenCount))
            }

            val itemDecorator =
                DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
            itemDecorator.setDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.divider)!!)

            categoriesListing.addItemDecoration(
                DividerItemDecoration(
                    applicationContext,
                    DividerItemDecoration.VERTICAL
                )
            )
            var adapterCate3 = Categorie3ListingAdapter(this, categoriy3Listing, this)
            cate3Listing.layoutManager = LinearLayoutManager(this)
            cate3Listing.adapter = adapterCate3

            //Toast.makeText(this, "position: $childrenCount",Toast.LENGTH_SHORT).show()
        }
    }

    override fun item3Click(
        parentPositionInTheList: Int,
        positionInTheList: Int,
        name: String,
        code: Int,
        childrenCount: Int,
        v:ConstraintLayout
    ) {
        if(childrenCount == 0){
            var intent = Intent(this,ProductListing::class.java)
            intent.putExtra("category",code.toString())
            startActivity(intent)
        }else{
            var cate4Recycler = v.getChildAt(2) as RecyclerView
            cate4Recycler.visibility = View.VISIBLE

            var categoriy4Listing = ArrayList<Cate4>()
            var jsonOb = jsonResultArray!!.getJSONObject(parentPositionInTheList)
            var children = jsonOb.getJSONArray("children_data")
            var actualElement = children.getJSONObject(positionInTheList)
            var actualChildren = actualElement.getJSONArray("children_data")
            var size = actualChildren.length()-1
            for(i in 0..size){
                var jsonOb = actualChildren.getJSONObject(i)
                var name = jsonOb.getString("name")
                var code = jsonOb.getInt("id")
                var productCount = jsonOb.getInt("product_count")
               categoriy4Listing.add(Cate4(name,code,productCount))
            }
            println(categoriy4Listing)

            val itemDecorator =
                DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
            itemDecorator.setDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.divider)!!)

            categoriesListing.addItemDecoration(
                DividerItemDecoration(
                    applicationContext,
                    DividerItemDecoration.VERTICAL
                )
            )
            var adapterCate4 = Categorie4ListingAdapter(this, categoriy4Listing, this)
            cate4Recycler.layoutManager = LinearLayoutManager(this)
            cate4Recycler.adapter = adapterCate4
            operso.expand(cate4Recycler, 50)
            //Toast.makeText(this, "children: $childrenCount",Toast.LENGTH_SHORT).show()
        }
    }

    override fun seeAllClick(code: Int) {
        var intent = Intent(this,ProductListing::class.java)
        intent.putExtra("category",code.toString())
        startActivity(intent)
    }

    override fun item4Click(name: String, code: Int, childrenCount: Int) {
        var intent = Intent(this,ProductListing::class.java)
        intent.putExtra("category",code.toString())
        startActivity(intent)
    }


}
