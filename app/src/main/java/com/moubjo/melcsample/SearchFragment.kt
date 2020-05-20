package com.moubjo.melcsample


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_search.view.*

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view  = inflater.inflate(R.layout.fragment_search, container, false)

        view.search_searchView.requestFocus()
        view.search_searchView.setIconifiedByDefault(false)

        view.search_searchView.setFocusable(true);
        view.search_searchView.setIconified(false);
        view.search_searchView.requestFocusFromTouch();



        return view
    }


}
