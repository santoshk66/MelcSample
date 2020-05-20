package com.moubjo.melcsample


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import kotlinx.android.synthetic.main.fragment_menu.view.*

/**
 * A simple [Fragment] subclass.
 */
class MenuFragment : Fragment() {

    var db:DbManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_menu, container, false)

        db = DbManager(context!!)

        val sharedPref = activity?.getSharedPreferences("melcomApp", Context.MODE_PRIVATE)

        v.menu_wishlist.setOnClickListener {
            var intent = Intent(context, Wishlist::class.java)
            startActivity(intent)
        }
        v.menu_viewed.setOnClickListener {
            var intent = Intent(context, Viewed::class.java)
            startActivity(intent)
        }

        v.menu_about.setOnClickListener {
            var intent = Intent(context, About::class.java)
            startActivity(intent)
        }


        v.menu_delRecentSearch.setOnClickListener {
            val builder = AlertDialog.Builder(context!!)
            builder.setMessage("Are you sure you want to delete the scan?")
            builder.apply {
                setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, id ->
                        deletion("%")
                    })
                setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            }

            builder.create()
            builder.show()
        }


        var switch1Value = sharedPref!!.getBoolean("switch1Value", true)
        v.switch1.isChecked = switch1Value
        v.switch1.setOnCheckedChangeListener { _, isChecked ->
            // do whatever you need to do when the switch is toggled here
            if(isChecked){
                with (sharedPref.edit()) {
                    putBoolean("switch1Value", true)
                    commit()
                }

                Toast.makeText(context, "Activated", Toast.LENGTH_SHORT).show()
            }else{
                with (sharedPref.edit()) {
                    putBoolean("switch1Value", false)
                    commit()
                }
                Toast.makeText(context, "Desactivated", Toast.LENGTH_SHORT).show()
            }
        }

        var switch2Value = sharedPref!!.getBoolean("switch2Value", true)
        v.switch2.isChecked = switch2Value
        v.switch2.setOnCheckedChangeListener { _, isChecked ->
            // do whatever you need to do when the switch is toggled here
            if(isChecked){
                with (sharedPref.edit()) {
                    putBoolean("switch2Value", true)
                    commit()
                }

                Toast.makeText(context, "Activated", Toast.LENGTH_SHORT).show()
            }else{
                with (sharedPref.edit()) {
                    putBoolean("switch2Value", false)
                    commit()
                }
                Toast.makeText(context, "Desactivated", Toast.LENGTH_SHORT).show()
            }
        }

        return v
    }

    fun deletion(title:String){
        var col = arrayOf("ID")
        var argList = arrayOf(title)

        val cursor = db!!.query("viewed", col,"name like ?",argList,"ID")

        if(cursor.moveToFirst()){
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                var argsList = arrayOf(ID.toString())
                db!!.delete("viewed","ID = ?",argsList)

            }while (cursor.moveToNext())
        }
        Toast.makeText(context!!, "Recent views cleaned", Toast.LENGTH_SHORT).show()
    }


}
