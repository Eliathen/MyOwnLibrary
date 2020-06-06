package com.szymanski.myownlibrary.activities

import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast

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
import com.google.android.material.snackbar.Snackbar

import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.adapters.SearchResultAdapter
import com.szymanski.myownlibrary.data.openLibraryAPI.models.Book
import com.szymanski.myownlibrary.data.openLibraryAPI.models.SearchBook
import com.szymanski.myownlibrary.viewModels.KeywordSearchResultViewModel

import kotlinx.android.synthetic.main.activity_keyword_search_result.*
import kotlinx.android.synthetic.main.keyword_search_result_item.*
import kotlinx.android.synthetic.main.keyword_search_result_item.view.*
import kotlinx.android.synthetic.main.keyword_search_result_item.view.saveBookProgressBar
import kotlinx.android.synthetic.main.search_item_details_dialog.*

class KeywordSearchResultActivity : AppCompatActivity(), SearchResultAdapter.OnItemListener {

    private lateinit var searchResultAdapter: SearchResultAdapter

    private var books: ArrayList<Book> = ArrayList()

    private lateinit var bookDetails: AlertDialog

    private lateinit var viewModel: KeywordSearchResultViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keyword_search_result)
        var phrase = ""
        if(intent.hasExtra("phrase")){
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
        viewModel.getResult().observe(this, Observer {
            displayResultDialog(it)
            saveBookProgressBar.visibility = View.GONE
            bookDetails.progressBar.visibility = View.GONE

        })
    }

    private fun initRecyclerView() {
        val recyclerView = this.searchResult
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, requestedOrientation))
        this.searchResultAdapter = SearchResultAdapter(this)
        recyclerView.adapter = searchResultAdapter
    }

    override fun onItemClick(position: Int) {
        val book = viewModel.getSearchResult().value?.get(position)
        if (book != null) {
            createDetailsDialog(book)
        }
    }

    override fun saveBook(v: View, position: Int) {
        saveBookProgressBar.visibility = View.VISIBLE
        viewModel.getSearchResult().value?.get(position)?.let { viewModel.saveBook(it) }
    }

    private fun createDetailsDialog(book: SearchBook) {
        bookDetails = this.let{
            val builder = AlertDialog.Builder(it)
            builder.apply{
                setView(this@KeywordSearchResultActivity.layoutInflater.inflate(R.layout.search_item_details_dialog, null))
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
            .listener(object: RequestListener<Drawable> {
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
                    if(resource?.intrinsicWidth == 600){
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
        alertDialog.saveButton.setOnClickListener {
            alertDialog.progressBar.visibility = View.VISIBLE
            viewModel.saveBook(book)

        }
    }
    private fun setSearchViewListeners(){
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
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
    private fun displayResultDialog(message: String){
        val alertDialog: AlertDialog = this.let{
            val builder = AlertDialog.Builder(it)
            builder.apply{
                setTitle("Message")
                setMessage(message)
                setNeutralButton("OK") { _: DialogInterface, _: Int ->
                    if(bookDetails!=null){
                        bookDetails.cancel()
                    }
                }
            }.create()
        }
        alertDialog.show()
    }

}
