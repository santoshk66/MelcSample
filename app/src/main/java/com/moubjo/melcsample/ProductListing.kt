package com.moubjo.melcsample

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.moubjo.melcsample.MainActivity.Companion.baseUrl
import kotlinx.android.synthetic.main.activity_product_listing.*
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import kotlin.system.exitProcess

class ProductListing : AppCompatActivity() {


    var operso = OPerso()
    var listOfProduct = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_listing)

        var b:Bundle = intent.extras
        var categoryCode = b.getString("category")

        var url = "$baseUrl/pCategory.php?q=$categoryCode"

        FetchProduct().execute(url)

        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Product Listing"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    inner class FetchProduct: AsyncTask<String, String, String>() {

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
            println("ticResult: $result")
            if(result == "null") {
                productListing_progressBar.visibility = View.GONE
                productListing_noAds.visibility = View.VISIBLE
                println("ticResult: 0kokok")
                exitProcess(1)
            }

            var json = JSONArray(result)
            var size = json.length()-1
            for(i in 0..size){
                var jsonOb = json.getJSONObject(i)

                var sku = jsonOb.getString("sku")
                var categoryId = jsonOb.getString("category_id")
                var price = jsonOb.getDouble("price")
                var name = jsonOb.getString("name")
                var picture = jsonOb.getString("thumbnail")
                var entity_id = jsonOb.getInt("entity_id")

                listOfProduct.add(Product(entity_id, sku, price, name, picture, categoryId))
            }

            /*val itemDecorator =
                DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
            itemDecorator.setDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.divider)!!)

            recycler.addItemDecoration(
                DividerItemDecoration(
                    applicationContext,
                    DividerItemDecoration.VERTICAL
                )
            )*/
            productListing_progressBar.visibility = View.GONE
            if(listOfProduct.isEmpty())  productListing_noAds.visibility = View.VISIBLE
            else recycler.visibility = View.VISIBLE

            var adapter = ProductListingAdapter(applicationContext, listOfProduct)
            recycler.layoutManager = GridLayoutManager(applicationContext, 2)
            recycler.adapter = adapter

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
        }

    }
}
