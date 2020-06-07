package com.szymanski.myownlibrary.activities

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.SearchView

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration

import androidx.recyclerview.widget.LinearLayoutManager

import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.adapters.SearchResultAdapter
import com.szymanski.myownlibrary.data.openLibraryAPI.models.Book
import com.szymanski.myownlibrary.data.openLibraryAPI.models.SearchBook
import com.szymanski.myownlibrary.viewModels.KeywordSearchResultViewModel

import kotlinx.android.synthetic.main.activity_keyword_search_result.*
import kotlinx.android.synthetic.main.keyword_search_result_item.*
import kotlinx.android.synthetic.main.keyword_search_result_item.view.*
import kotlinx.android.synthetic.main.search_item_details_dialog.*
import kotlinx.android.synthetic.main.search_item_details_dialog.view.*

class KeywordSearchResultActivity : AppCompatActivity(), SearchResultAdapter.OnItemListener {

    private lateinit var searchResultAdapter: SearchResultAdapter

    private lateinit var bookDetails: AlertDialog

    private lateinit var viewModel: KeywordSearchResultViewModel

    private lateinit var currentView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keyword_search_result)
        var phrase = ""
        if (intent.hasExtra("phrase")) {
            phrase = intent.getStringExtra("phrase")!!
        }
        initRecyclerView()
        initViewModel()
        setSearchViewListeners()
        viewModel.findBooks(phrase)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(KeywordSearchResultViewModel::class.java)
        viewModel.getSearchResult().observe(this, Observer {
            searchResultAdapter.setBooks(it)
        })
        viewModel.getDataLoaded().observe(this, Observer {
            if (it) {
                searchBookDetailsProgressBar.visibility = View.GONE
            } else {
                searchBookDetailsProgressBar.visibility = View.VISIBLE
            }
        })
        viewModel.getBookSaved().observe(this, Observer {
            currentView.saveBookProgressBar.visibility = View.GONE
            displayResultDialog(it)
            currentView.saveBookButton.isClickable = false
            searchResultAdapter.setBooks(viewModel.getSearchResult().value!!)
        })
        viewModel.getBookSavedByDetails().observe(this, Observer {
            displayResultDialog(it)
            searchResultAdapter.setBooks(viewModel.getSearchResult().value!!)
            disableButton(bookDetails.saveButton)
            disableButton(bookDetails.addToWishListButton)
            if (this::bookDetails.isInitialized) {
                bookDetails.progressBar.visibility = View.GONE
            }
        })
        viewModel.getBookAddToWishList().observe(this, Observer {
            displayResultDialog(it)
            searchResultAdapter.setBooks(viewModel.getSearchResult().value!!)
            disableButton(bookDetails.addToWishListButton)
            bookDetails.progressBar.visibility = View.GONE
        })
    }

    private fun initRecyclerView() {
        val recyclerView = this.searchResult
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                requestedOrientation
            )
        )
        this.searchResultAdapter = SearchResultAdapter(this)
        recyclerView.adapter = searchResultAdapter
    }

    override fun onItemClick(v: View, position: Int) {
        val book = viewModel.getSearchResult().value?.get(position)
        if (book != null) {
            createDetailsDialog(book)
        }
    }

    override fun saveBook(v: View, position: Int) {
        currentView = v
        viewModel.getSearchResult().value?.get(position)?.let {
            v.saveBookProgressBar.visibility = View.VISIBLE
            viewModel.saveBook(it, false)

        }
    }

    private fun createDetailsDialog(book: SearchBook) {
        bookDetails = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setView(
                    this@KeywordSearchResultActivity.layoutInflater.inflate(
                        R.layout.search_item_details_dialog,
                        null
                    )
                )
            }.create()
        }
        bookDetails.show()
        loadDataToDialogDetails(bookDetails, book)
    }

    private fun loadDataToDialogDetails(
        alertDialog: AlertDialog,
        book: SearchBook
    ) {
        alertDialog.bookDetailsTitle.text = book.title
        Glide.with(this)
            .load(book.cover)
            .error(R.drawable.blank_cover)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    if (resource?.intrinsicWidth == 600) {
                        alertDialog.bookDetailsCover.setImageResource(R.drawable.blank_cover)
                        return true
                    }
                    return false
                }
            })
            .into(alertDialog.bookDetailsCover)
        alertDialog.bookDetailsPublishedDate.text = book.year
        alertDialog.bookDetailsAuthors.text = book.authors.toString()
        alertDialog.bookDetailsIsbn.text = book.isbn
        if(book.isBookInWishList){
            disableButton(alertDialog.addToWishListButton)
        }
        else if(book.isBookSaved){
            disableButton(alertDialog.saveButton)
            disableButton(alertDialog.addToWishListButton)
        } else {
            alertDialog.saveButton.setOnClickListener {
                bookDetails.progressBar.visibility = View.VISIBLE
                viewModel.saveBook(book, true)

            }
            alertDialog.addToWishListButton.setOnClickListener {
                bookDetails.progressBar.visibility = View.VISIBLE
                viewModel.addBookToWishList(book)
            }
        }
    }
    private fun disableButton(button: Button?){
        button?.setBackgroundResource(R.drawable.disable_button_shape)
        button?.isClickable = false
    }
    private fun setSearchViewListeners() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchResultAdapter.setBooks(mutableListOf())
                if (query != null) {
                    viewModel.findBooks(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun displayResultDialog(message: String) {
        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle("Message")
                setMessage(message)
                setPositiveButton("OK") { _: DialogInterface, _: Int ->

                }
            }.create()
        }
        alertDialog.show()
    }
}

