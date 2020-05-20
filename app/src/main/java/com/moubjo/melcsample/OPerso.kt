package com.moubjo.melcsample

import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.Transformation
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class OPerso {

    fun convertToString(input: InputStream):String{
        var result = ""
        var bufferReader = BufferedReader(InputStreamReader(input))
        var line = ""
        try {
            do{
                line = bufferReader.readLine()
                if(line != null){
                    result +=line
                }
            }while(line != null)

        }catch (ex: Exception){

        }

        return result
    }

    fun viewIn(technique: Techniques,view: View, duration: Long ){
        YoYo.with(technique)
            .duration(200)
            .playOn(view);
        view.visibility = View.VISIBLE
    }
    fun viewOut(technique: Techniques,view: View, duration: Long ){
        YoYo.with(technique)
            .duration(200)
            .playOn(view);
        var handler = Handler()
        var runnable = Runnable {
            view.visibility = View.GONE
        }
        handler.postDelayed(runnable,200)

    }

    fun expand(v: View, duration: Long) {
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        val targetHeight = v.measuredHeight

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.layoutParams.height = 1
        v.visibility = View.VISIBLE
        val a: Animation = object : Animation() {
            override fun applyTransformation(
                interpolatedTime: Float,
                t: Transformation
            ) {
                v.layoutParams.height =
                    if (interpolatedTime == 1f) WindowManager.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
                v.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // 1dp/ms
        a.setDuration(duration)
        v.startAnimation(a)
    }

    fun collapse(v: View, duration: Long) {
        val initialHeight = v.measuredHeight
        val a: Animation = object : Animation() {
            override fun applyTransformation(
                interpolatedTime: Float,
                t: Transformation
            ) {
                if (interpolatedTime == 1f) {
                    v.visibility = View.GONE
                } else {
                    v.layoutParams.height =
                        initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // 1dp/ms
        a.setDuration(duration)
        v.startAnimation(a)
    }

}