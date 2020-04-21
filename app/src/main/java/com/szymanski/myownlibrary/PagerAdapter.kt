package com.szymanski.myownlibrary

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(fragment: MainActivity): FragmentStateAdapter(fragment) {
    private val numberOfFragments = 3
    override fun getItemCount(): Int {
        return numberOfFragments
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> {MyBooksFragment()}
            1 -> {LendBorrowFragment()}
            else -> {WishListFragment()}
        }
    }

}