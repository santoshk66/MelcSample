package com.moubjo.melcsample

import android.content.ContentValues
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.viewpagerindicator.CirclePageIndicator
import kotlinx.android.synthetic.main.activity_product_details.*
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.collections.ArrayList

class ProductDetails : AppCompatActivity(), GridAdapter.OnItemClickListener {
    var listOfProduct = ArrayList<WishProduct>()
    var adapter:GridAdapter? = null

    var operso = OPerso()
    var db:DbManager? = null

    var sharingTitle:String? = null
    var sharingLink:String? = null

    var pPrice = 0.00
    var pName = ""
    var pSku = ""
    var firstImageLink = ""
    var quantity = 0
    
    var wished = false
    var ID = 0;

    var im:ImageView? = null
    var badgeText:TextView? = null

    private val myImageList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        val actionbar = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)

        db = DbManager(this)

        im = productDetails_minusImage
        im!!.setColorFilter(R.color.colorBlueMel, PorterDuff.Mode.SRC_IN)

        var bundle = intent.extras
        var productSku = bundle.getString("sku")

        productDetails_sku.text = productSku
        pSku = productSku
        ID = checkWishlistExistance(pSku)
        if(ID != 0 ) {
            productDetails_favorite.setImageResource(R.drawable.icons8_heart_filled)
            wished = true
        }
        //Toast.makeText(this, "This is the ID: $ID", Toast.LENGTH_SHORT).show()


        var url = "http://34.242.215.249/mag/productDetails.php?sku=$productSku"
        FetchProductDetails().execute(url)

        val itemDecorator =
            DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)

        productDetails_recentRecycler.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.HORIZONTAL
            )
        )
        loadRecentViews("%",pSku)

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.product_details_menu, menu)

        val menuItem = menu!!.findItem(R.id.product_details_menu_cart)
        val actionView = menuItem.actionView
        badgeText = actionView.findViewById<TextView>(R.id.cart_badge)

        var cartCount = cartCount()
        if(cartCount > 0) badgeText!!.text = cartCount.toString()
        else badgeText!!.visibility = View.GONE

        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        var cartCount = cartCount()
        try {
            if(cartCount > 0) badgeText!!.text = cartCount.toString()
            else badgeText!!.visibility = View.GONE
        }catch (ex:Exception){
            println(ex.message)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item != null){
            when(item.itemId){
                R.id.product_details_menu_share ->{
                    val share = Intent(Intent.ACTION_SEND)
                    share.type = "text/plain"
                    share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)


                    share.putExtra(Intent.EXTRA_TEXT, sharingLink)

                    startActivity(Intent.createChooser(share, "Share Product!"))
                }
                R.id.product_details_menu_cart ->{

                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun cartCount():Int{
        var count = 0
        var col = arrayOf("COUNT(ID) As count")
        var argList = arrayOf("%")

        val cursor = db!!.query("cart", col,"name like ?",argList,"ID")


        if(cursor.moveToFirst()){
            count = cursor.getInt(cursor.getColumnIndex("count"))
        }
        return count
    }

    fun confirm(v:View){
        var db = DbManager(this)
        var values = ContentValues()

        var quantit = productDetails_quantityTextview.text.toString()
        quantity = Integer.parseInt(quantit)

        values.put("name",pName)
        values.put("sku",pSku)
        values.put("price",pPrice)
        values.put("quantity",quantity)
        values.put("picture",firstImageLink)

        var idInserted = db.insert("cart",values)
            if (idInserted > 0) {
                badgeText!!.apply {
                    text = cartCount().toString()
                    visibility = View.VISIBLE
                }
                Toast.makeText(this, "Added to the cart ", Toast.LENGTH_SHORT).show()
                operso.viewOut(Techniques.SlideOutDown,productDetails_quantityLayout, 200 )
            }else{
                Toast.makeText(this, "Error! Try again", Toast.LENGTH_SHORT).show()
            }

    }

    fun cancel(v:View){
        operso.viewOut(Techniques.SlideOutDown,productDetails_quantityLayout, 200 )

    }

    fun addToCart(v:View){
        operso.viewIn(Techniques.SlideInUp,productDetails_quantityLayout, 200 )
    }

    fun buyNow(v:View){
        var db = DbManager(this)
        var values = ContentValues()

        values.put("name",pName)
        values.put("sku",pSku)
        values.put("price",pPrice)
        values.put("quantity",1)
        values.put("picture",firstImageLink)

        var idInserted = db.insert("cart",values)
        if (idInserted > 0) {
            val intent = Intent(this,CartActivity::class.java)
            startActivity(intent)
        }else{
            Toast.makeText(this, "Error! Try again", Toast.LENGTH_SHORT).show()
        }
    }

    fun dial(v:View){
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+"+233 56 111 2777"))
        startActivity(intent)
    }

    fun plus(v:View){
        var currentQuantityText = productDetails_quantityTextview.text.toString()
        var currentQuantity = Integer.parseInt(currentQuantityText)
        var newQuantity =  currentQuantity + 1
        productDetails_quantityTextview.text = newQuantity.toString()
        im!!.clearColorFilter()
    }

    fun minus(v:View){

        var currentQuantityText = productDetails_quantityTextview.text.toString()
        var currentQuantity = Integer.parseInt(currentQuantityText)
        var newQuantity = 1;
        if(currentQuantity >=2){
            newQuantity =  currentQuantity - 1
            productDetails_quantityTextview.text = newQuantity.toString()
        }
        if(newQuantity == 1){
            im!!.setColorFilter(R.color.colorBlueMel, PorterDuff.Mode.SRC_IN)
        }


    }

    fun cartClick(v:View){
        val intent = Intent(this,CartActivity::class.java)
        startActivity(intent)
    }

    fun addToWishlist(v:View){
        if(!wished) {
            var db = DbManager(this)
            var values = ContentValues()

            values.put("sku", pSku)
            values.put("name", pName)
            values.put("price", pPrice)
            values.put("picture", firstImageLink)

            var idInserted = db.insert("wishlist", values)
            wished = true
            if (idInserted > 0) {
                productDetails_favorite.setImageResource(R.drawable.icons8_heart_filled)
                Toast.makeText(this, "Added to the whishlist", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error! Try again", Toast.LENGTH_SHORT).show()
                wished = false
            }
        }else{
            /*var argsList = arrayOf(ID.toString())
            db!!.delete("wishlist","ID = ?",argsList)
            productDetails_favorite.setImageResource(R.drawable.icons8_heart_outline)
            Toast.makeText(this, "Removed from the whishlist", Toast.LENGTH_SHORT).show()
            wished = false*/
        }
    }

    fun checkWishlistExistance(sku:String):Int{
        var col = arrayOf("ID")
        var argList = arrayOf(sku)
        val cursor = db!!.query("wishlist", col,"sku = ?",argList,"ID LIMIT 1")
        if(cursor.moveToFirst()){
            ID = cursor.getInt(cursor.getColumnIndex("ID"))
        }
        return ID
    }

    fun checkViewedExistance(sku:String):Int{

        var col = arrayOf("ID")
        var argList = arrayOf(sku)
        val cursor = db!!.query("viewed", col,"sku = ?",argList,"ID LIMIT 1")
        if(cursor.moveToFirst()){
            ID = cursor.getInt(cursor.getColumnIndex("ID"))
        }
        return ID
    }

    fun loadRecentViews(namePar:String, skuPar: String){

        var col = arrayOf("ID","sku","name","price","picture")
        var argList = arrayOf(namePar,skuPar)

        val cursor = db!!.query("viewed", col,"name like ? AND sku != ?",argList,"ID DESC LIMIT 5")
        listOfProduct.clear()

        if(cursor.moveToFirst()){
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val sku = cursor.getString(cursor.getColumnIndex("sku"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val price = cursor.getString(cursor.getColumnIndex("price"))
                val picture = cursor.getString(cursor.getColumnIndex("picture"))
                listOfProduct.add(WishProduct(ID, sku, price.toDouble(), name,  picture))
            }while (cursor.moveToNext())
        }
        /* if(listOfProduct.size == 0) {
             cart_cart.visibility = View.GONE
             cart_emptyCart.visibility = View.VISIBLE
         }*/
        adapter = GridAdapter(this, listOfProduct, this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        productDetails_recentRecycler.layoutManager = layoutManager
        productDetails_recentRecycler.adapter = adapter
    }

    inner class FetchProductDetails: AsyncTask<String, String, String>() {

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

            var productName = json.getString("name")
            var productPrice = json.getString("price")
            productDetails_price.text = productPrice
            productDetails_nameTop.text = productName
            productDetails_nameDown.text = productName
            pName = productName
            pPrice = productPrice.toDouble()
            sharingLink = "Take a look at this product: $productName on Melcomonline via this link: https://www.melcomonline.com/"


            var extension_attributes = json.getJSONObject("extension_attributes");
            var stock_item = extension_attributes.getJSONObject("stock_item");
            var isInstock = stock_item.getBoolean("is_in_stock");
            if(isInstock === true) productDetails_imgYes.visibility = View.VISIBLE
            else productDetails_imgNo.visibility = View.VISIBLE

            var media_gallery_entries = json.getJSONArray("media_gallery_entries")
            if(media_gallery_entries.length() > 0) {

                for(i in 0.. (media_gallery_entries.length() - 1)){
                    var o = media_gallery_entries.getJSONObject(i)
                    var file = o.getString("file")
                    myImageList.add(file)
                    if(i == 0) firstImageLink = file
                }

                imageModelArrayList = ArrayList()
                imageModelArrayList = populateList()
                init()

            }

            var custom_attributes = json.getJSONArray("custom_attributes")
            var custom_attributes0 = custom_attributes.getJSONObject(0)
            var description = custom_attributes0.getString("value")
            productDetails_description.text = Html.fromHtml(description)

            var db = DbManager(applicationContext)
            var values = ContentValues()

            var vID = checkViewedExistance(pSku)
            if(vID != 0) {
                var argsList = arrayOf(vID.toString())
                db!!.delete("viewed","ID = ?",argsList)
            }
            values.put("sku", pSku)
            values.put("name", pName)
            values.put("price", pPrice)
            values.put("picture", firstImageLink)
            db.insert("viewed", values)

            loadingGif.visibility = View.GONE
            productDeatails_container.visibility = View.VISIBLE

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
        }

    }


    private fun populateList(): ArrayList<ImageModel> {

        val list = ArrayList<ImageModel>()

        for (i in 0..(myImageList.size-1)) {
            val imageModel = ImageModel()
            imageModel.setImage_drawables(myImageList[i])
            list.add(imageModel)
        }

        return list
    }
    private fun init() {

        mPager = findViewById(R.id.pager) as ViewPager
        mPager!!.adapter = SlidingImage_Adapter(this)

        val indicator = findViewById(R.id.indicator) as CirclePageIndicator

        if(myImageList.size < 2) indicator.visibility = View.GONE

        indicator.setViewPager(mPager)

        val density = resources.displayMetrics.density

        //Set circle indicator radius
        indicator.setRadius(5 * density)

        NUM_PAGES = imageModelArrayList!!.size

        // Auto start of viewpager
        /*val handler = Handler()
        val Update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            mPager!!.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 3000, 3000)
*/
        // Pager listener over indicator
        indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                currentPage = position

            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(pos: Int) {

            }
        })

    }
    companion object {
        private var mPager: ViewPager? = null
        private var currentPage = 0
        private var NUM_PAGES = 0
        var imageModelArrayList: ArrayList<ImageModel>? = null
    }

    override fun onItemClick(item: Int, ID: Int, v: View) {

    }

}
