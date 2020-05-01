package com.szymanski.myownlibrary.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.data.models.Book
import com.szymanski.myownlibrary.viewModels.BookDetailsViewModel
import com.szymanski.myownlibrary.viewModels.MainViewModel
import kotlinx.android.synthetic.main.activity_book_details.*

class BookDetailsActivity : AppCompatActivity() {
    private lateinit var bookDetailsViewModel: BookDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)
        bookDetailsViewModel = ViewModelProvider(this).get(BookDetailsViewModel::class.java)
        val book: Book = this.intent.getSerializableExtra("Book") as Book
        bookDetailsViewModel.setBook(book)
        bookDetailsViewModel.getBook().observe(this, Observer {
            loadDetails()
        })

    }


    private fun loadDetails(){
        bookDetailsTitle.text = bookDetailsViewModel.getBook().value?.title
        Glide.with(this)
            .load(bookDetailsViewModel.getBook().value?.cover)
            .error(R.drawable.books)
            .into(bookDetailsCover)
        bookDetailsAuthors.text = bookDetailsViewModel.getBook().value?.authors
                .toString()
                .substring(1,bookDetailsViewModel.getBook().value?.authors.toString().length-1)
        bookDetailsPublishedDate.text = bookDetailsViewModel.getBook().value?.publishedDate
        bookDetailsPages.text = bookDetailsViewModel.getBook().value?.pageCount.toString()
        bookDetailsIsbn.text = bookDetailsViewModel.getBook().value?.isbn
    }
}
