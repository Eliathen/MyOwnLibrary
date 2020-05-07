package com.szymanski.myownlibrary.activities

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.adapters.PagerAdapter
import com.szymanski.myownlibrary.viewModels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false
    private lateinit var viewPager: ViewPager2
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewPager()
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        loadExampleData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)

        return (super.onCreateOptionsMenu(menu))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.i("MainActivity", "OnContextItemSelected")
        return when(item.itemId){
            R.id.searchButton -> {
                Log.i("MainActivity", "Search button clicked")
                true
            }
            R.id.sortButton -> {
                Log.i("MainActivity", "Sort button clicked")
                true
            }
            R.id.helpButton -> {
                Log.i("MainActivity", "Help button clicked")

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun setViewPager() {
        viewPager = findViewById(R.id.pager)
        val pagerAdapter = PagerAdapter(this)
        viewPager.adapter = pagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> {
                    getString(R.string.my_books_tabItem)
                }
                1 -> {
                    getString(R.string.lend_borrow_tabItem)
                }
                else -> {
                    getString(R.string.wishList_tabItem)
                }
            }
        }.attach()
    }
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, getString(R.string.double_click_to_exit_text), Toast.LENGTH_SHORT).show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
    private fun loadExampleData(){
        mainViewModel.searchBookByIsbn("9780641723445")
//        mainViewModel.searchBookByIsbn("9781857230765")
    }

    override fun onStart() {
        super.onStart()
        //TODO get books list

    }
}
