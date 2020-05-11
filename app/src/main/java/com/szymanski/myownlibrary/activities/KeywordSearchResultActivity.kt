package com.szymanski.myownlibrary.activities

import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog

import androidx.recyclerview.widget.LinearLayoutManager

import com.bumptech.glide.Glide

import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.adapters.SearchResultAdapter
import com.szymanski.myownlibrary.data.models.Book

import kotlinx.android.synthetic.main.activity_keyword_search_result.*
import kotlinx.android.synthetic.main.search_item_details_dialog.*

class KeywordSearchResultActivity : AppCompatActivity(), SearchResultAdapter.OnItemListener {

    private lateinit var searchResultAdapter: SearchResultAdapter

    private var books: ArrayList<Book> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keyword_search_result)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        //Example data
        val book = Book(
            "9780641723445",
            "The lightning thief",
            arrayListOf("Rick Riordan"),
            "2005",
            377,
            "https://covers.openlibrary.org/b/id/7989100-M.jpg"
        )
        repeat(10) {
            this.books.add(book)
        }

        val recyclerView = this.searchResult
        recyclerView.layoutManager = LinearLayoutManager(this)
        this.searchResultAdapter = SearchResultAdapter(this)
        searchResultAdapter.setBooks(books)
        recyclerView.adapter = searchResultAdapter
    }

    override fun onItemClick(position: Int) {
        val book = books[position]
        Log.d("KeywordSearchResult", "Click item")
        createDetailsDialog(book)
    }

    private fun createDetailsDialog(book: Book) {
        Log.i("Keyword", book.toString())
        val alertDialog: AlertDialog = this.let{
            val builder = AlertDialog.Builder(it)
            builder.apply{
                setView(this@KeywordSearchResultActivity.layoutInflater.inflate(R.layout.search_item_details_dialog, null))

            }.create()
        }
        alertDialog.show()
        loadDataToDialogDetails(alertDialog, book)
    }

    private fun loadDataToDialogDetails(
        alertDialog: AlertDialog,
        book: Book
    ) {
        alertDialog.bookDetailsTitle.text = book.title
        Glide.with(this)
            .load(book.cover)
            .error(R.drawable.books)
            .into(alertDialog.bookDetailsCover)
        alertDialog.bookDetailsPublishedDate.text = book.publishedDate
        alertDialog.bookDetailsAuthors.text = book.authors.toString()
        alertDialog.bookDetailsPages.text = book.pageCount.toString()
        alertDialog.bookDetailsIsbn.text = book.isbn
    }

}
