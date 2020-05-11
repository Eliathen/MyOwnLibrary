package com.szymanski.myownlibrary.adapters

import androidx.fragment.app.Fragment

import androidx.viewpager2.adapter.FragmentStateAdapter

import com.szymanski.myownlibrary.fragments.dialogFragments.IsbnSearchFragment
import com.szymanski.myownlibrary.fragments.dialogFragments.KeywordSearchFragment

class SaveMethodAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    private val numberOfPage = 2
    override fun getItemCount(): Int {
        return numberOfPage
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> {
                IsbnSearchFragment()
            }
            else -> {
                KeywordSearchFragment()
            }
        }
    }

}