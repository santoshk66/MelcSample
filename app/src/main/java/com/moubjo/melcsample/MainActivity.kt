package com.moubjo.melcsample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var db:DbManager? = null
    var hasWarned = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_NoActionBar)
        setContentView(R.layout.activity_main)

        db = DbManager(this)

        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        if (savedInstanceState == null) {
            val fragment = HomeFragment()
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()

        bottom_navigation.removeBadge(R.id.navigation_cart)

        var cartCount = cartCount()
        if(cartCount > 0){
            val badge: BadgeDrawable = bottom_navigation.getOrCreateBadge(R.id.navigation_cart)
            badge.number = cartCount
            badge.isVisible = true
        }



    }

    fun click(v: View){

        var intent = Intent(this, ProductListing::class.java)
        intent.putExtra("category","${v.tag}")
        startActivity(intent)
    }
    fun first3Click(v: View){

        var intent = Intent(this, ProductListing::class.java)
        intent.putExtra("category","${v.tag}")
        startActivity(intent)
    }
    fun topCategoriesClick(v: View){

        var intent = Intent(this, ProductListing::class.java)
        intent.putExtra("category","${v.tag}")
        startActivity(intent)
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
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.navigation_home -> {
                val fragment = HomeFragment()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifiaction -> {
                val fragment = NotificationFragment()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search -> {
                val fragment = SearchFragment()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                    .commit()
                return@OnNavigationItemSelectedListener true
                /*val intent = Intent(applicationContext,SearchActivity::class.java)
                startActivity(intent)*/
            }
            R.id.navigation_cart -> {
                val intent = Intent(applicationContext,CartActivity::class.java)
                startActivity(intent)
            }
            R.id.navigation_menu -> {
                val fragment = MenuFragment()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onBackPressed() {
        val bottomNavigationView =
            findViewById(R.id.bottom_navigation) as BottomNavigationView
        val seletedItemId = bottomNavigationView.selectedItemId
        if (R.id.navigation_home != seletedItemId) {
            setHomeItem(this@MainActivity)
        } else {
            closingAlert()
        }
    }

    fun setHomeItem(activity: Activity) {
        val bottomNavigationView =
            activity.findViewById<View>(R.id.bottom_navigation) as BottomNavigationView
        bottomNavigationView.selectedItemId = R.id.navigation_home
    }

    fun closingAlert(){
        val handler = Handler()
        val runnable = Runnable { hasWarned = false }

        if(hasWarned){
            handler.removeCallbacks(runnable)
            super.onBackPressed()
        }else{
            Toast.makeText(this@MainActivity, "Press again to quit", Toast.LENGTH_SHORT).show()
            hasWarned = true
            handler.postDelayed(runnable, 3000)
        }
    }

    companion object{
        var production = 1
        var baseUrl = if (production == 0 ) "http://192.168.100.3:8/mag" else "https://royalethaimassage.com/mag"

    }
}
