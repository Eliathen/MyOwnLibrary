package com.szymanski.myownlibrary.adapters

import androidx.fragment.app.Fragment

import androidx.viewpager2.adapter.FragmentStateAdapter

import com.szymanski.myownlibrary.fragments.LendBorrowFragment
import com.szymanski.myownlibrary.activities.MainActivity
import com.szymanski.myownlibrary.fragments.MyBooksFragment
import com.szymanski.myownlibrary.fragments.WishListFragment

class PagerAdapter(fragment: MainActivity): FragmentStateAdapter(fragment) {
    private val numberOfFragments = 3
    override fun getItemCount(): Int {
        return numberOfFragments
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> {
                MyBooksFragment()
            }
            1 -> {
                LendBorrowFragment()
            }
            else -> {
                WishListFragment()
            }
        }
    }

}