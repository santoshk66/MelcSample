package com.moubjo.melcsample


import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.moubjo.melcsample.MainActivity.Companion.baseUrl
import com.viewpagerindicator.CirclePageIndicator
import kotlinx.android.synthetic.main.fragment_home_fagement.view.*
import kotlinx.android.synthetic.main.fragment_home_fagement.view.home_TaRecycler
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    var operso = OPerso()
    var milistOfProduct = ArrayList<Product>()
    var laptoplistOfProduct = ArrayList<Product>()
    var splitAclistOfProduct = ArrayList<Product>()
    var HomeAndKitchenlistOfProduct = ArrayList<Product>()

    var adapter:ProductListingAdapter? = null
    var laptopAdapter:ProductListingAdapter? = null
    var splitAcAdapter:ProductListingAdapter? = null
    var HomeAndKitchenAdapter:ProductListingAdapter? = null

    private var imageModelArrayList: ArrayList<ImageModel>? = null
    private val myImageListt = arrayOf(
        "https://www.melcomonline.com/media/wysiwyg/mobiles_4_.jpg",
        "https://www.melcomonline.com/media/wysiwyg/mobiles_1__1.png",
        "https://www.melcomonline.com/media/wysiwyg/Food.jpg",
        "https://www.melcomonline.com/media/wysiwyg/LED.jpg"
    )

    var indicator:CirclePageIndicator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         var view = inflater.inflate(R.layout.fragment_home_fagement, container, false)
        view.home_allcategories.setOnClickListener {
            var intent = Intent(activity, SubCategoriesListing::class.java)
            intent.putExtra("category","All")
            startActivity(intent)
        }

        var home_TaRecycler = view.home_TaRecycler
        var home_MobilesRecycler = view.home_MobilesRecycler
        var home_tablets = view.home_tablets
        var HomeAndKitchen = view.home_HomeAndKitchenRecycler

        mPage = view.pager as ViewPager
        indicator = view.indicator as CirclePageIndicator

        imageModelArrayList = ArrayList()
        imageModelArrayList = populateList()
        init()


        adapter = ProductListingAdapter(context!!, milistOfProduct)
        home_TaRecycler.layoutManager = GridLayoutManager(context,2)
        home_TaRecycler.adapter = adapter

        var miUrl = "$baseUrl/pCategory.php?q=1290"
        loadData(miUrl, milistOfProduct,adapter!!)


        laptopAdapter = ProductListingAdapter(context!!, laptoplistOfProduct)
        home_MobilesRecycler.layoutManager = GridLayoutManager(context,2)
        home_MobilesRecycler.adapter = laptopAdapter

        var mobilesUrl = "$baseUrl/pCategory.php?q=1279"
        loadData(mobilesUrl, laptoplistOfProduct,laptopAdapter!!)


        splitAcAdapter = ProductListingAdapter(context!!, splitAclistOfProduct)
        home_tablets.layoutManager = GridLayoutManager(context,2)
        home_tablets.adapter = splitAcAdapter

        var splitAcUrl = "$baseUrl/pCategory.php?q=1282"
        loadData(splitAcUrl, splitAclistOfProduct,splitAcAdapter!!)

        HomeAndKitchenAdapter = ProductListingAdapter(context!!, HomeAndKitchenlistOfProduct)
        HomeAndKitchen.layoutManager = GridLayoutManager(context,2)
        HomeAndKitchen.adapter = HomeAndKitchenAdapter

        var HomeAndKitchenUrl = "$baseUrl/pCategory.php?q=1313"
        loadData(HomeAndKitchenUrl, HomeAndKitchenlistOfProduct,HomeAndKitchenAdapter!!)

        return view;
    }

    override fun onResume() {
        super.onResume()

    }

    fun loadData(actualUrl:String,listOfRecord:ArrayList<Product>, actualAdapter:ProductListingAdapter){
        class FetchProduct: AsyncTask<String, String, String>() {

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
                var json = JSONArray(result)

                var size = json.length()-1
                print("tic: $size")
                for(i in 0..size){
                    var jsonOb = json.getJSONObject(i)
                    var sku = jsonOb.getString("sku")
                    var categoryId = jsonOb.getString("category_id")
                    var price = jsonOb.getDouble("price")
                    var name = jsonOb.getString("name")
                    var picture = jsonOb.getString("thumbnail")
                    var entity_id = jsonOb.getInt("entity_id")
                    listOfRecord.add(Product(entity_id, sku, price, name, picture, categoryId))
                }

                actualAdapter!!.notifyDataSetChanged()


            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
            }

        }

        FetchProduct().execute(actualUrl)
    }


    private fun populateList(): ArrayList<ImageModel> {

        val list = ArrayList<ImageModel>()

        for (i in 0..(myImageListt.size-1)) {
            val imageModel = ImageModel()
            imageModel.setImage_drawables(myImageListt[i])
            list.add(imageModel)
        }

        return list
    }
    private fun init() {

        //mPager = findViewById(R.id.pager) as ViewPager
        mPage!!.adapter = SliderHomeAdapter(context!!, imageModelArrayList!!)

        //val indicator = findViewById(R.id.indicator) as CirclePageIndicator

        if(myImageListt.size < 2) indicator!!.visibility = View.GONE

        indicator!!.setViewPager(mPage)

        val density = resources.displayMetrics.density

        //Set circle indicator radius
        indicator!!.setRadius(5 * density)

        NUM_PAGE = imageModelArrayList!!.size

        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPag == NUM_PAGE) {
                currentPag = 0
            }
            mPage!!.setCurrentItem(currentPag++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 3000, 3000)

        // Pager listener over indicator
        indicator!!.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                currentPag = position

            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(pos: Int) {

            }
        })

    }
    companion object {
        private var mPage: ViewPager? = null
        private var currentPag = 0
        private var NUM_PAGE = 0
    }
}
