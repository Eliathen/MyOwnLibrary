package com.szymanski.myownlibrary.activities

import android.content.DialogInterface
import android.content.Intent

import android.os.Bundle
import android.os.Handler
import android.util.Log

import android.view.Menu
import android.view.MenuItem

import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.ViewModelProvider

import androidx.viewpager2.widget.ViewPager2

import com.google.android.material.tabs.TabLayoutMediator

import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.SortType
import com.szymanski.myownlibrary.adapters.PagerAdapter
import com.szymanski.myownlibrary.data.firebase.models.FirebaseBook
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
                displaySortOptions()
                true
            }
            R.id.helpButton -> {
                Log.i("MainActivity", "Help button clicked")
                val intent = Intent(this,
                    HelpActivity::class.java)
                startActivity(intent)
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

    private fun displaySortOptions(){

        var currentChoice = SortType.TITLE_ASCENDING
        AlertDialog.Builder(this)
            .setTitle("Choose sort option")
            .setSingleChoiceItems(R.array.sort_options, -1) { _, id ->
                currentChoice = when(id){
                    0 -> SortType.TITLE_ASCENDING
                    1 -> SortType.TITLE_DESCENDING
                    2 -> SortType.PUBLISHED_YEAR_ASCENDING
                    3 -> SortType.PUBLISHED_YEAR_DESCENDING
                    else -> SortType.TITLE_ASCENDING
                }
            }
            .setPositiveButton("Sort",
                DialogInterface.OnClickListener{ _, _ ->
                    mainViewModel.sortAllLists(currentChoice)
                })
            .setNegativeButton(R.string.cancel_button_text,
                DialogInterface.OnClickListener { _, _ ->

                })
            .create().show()
    }
    override fun onStart() {
        super.onStart()
        mainViewModel.getBooks()

    }
}
