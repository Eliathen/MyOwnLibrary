package com.szymanski.myownlibrary.fragments.dialogFragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.adapters.SaveMethodAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.choose_method_save_book_dialog.view.*
import kotlinx.android.synthetic.main.choose_method_save_book_dialog.view.tabLayout

class SaveBookDialogFragment: DialogFragment(){

    private lateinit var viewPager: ViewPager2

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.choose_method_save_book_dialog, container, true)
        viewPager = view.findViewById(R.id.viewPager)
        val pagerAdapter = SaveMethodAdapter(this)
        viewPager.adapter = pagerAdapter
        TabLayoutMediator(view.tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> {
                    getString(R.string.isbn_search_text)
                }
                else -> {
                    getString(R.string.keyword_search_text)
                }
            }
        }.attach()
        return view
    }
}