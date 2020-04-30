package com.szymanski.myownlibrary.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.data.models.Book

class BookDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)

        val book: Book = this.intent.getSerializableExtra("Book") as Book
        Log.d("Book", book.toString())
    }
}
