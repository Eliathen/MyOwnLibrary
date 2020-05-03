package com.szymanski.myownlibrary.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.data.models.Book
import com.szymanski.myownlibrary.viewModels.BookDetailsViewModel
import kotlinx.android.synthetic.main.activity_book_details.*
import kotlinx.android.synthetic.main.borrow_lend_dialog.*
import java.time.LocalDate
import java.util.*

class BookDetailsActivity : AppCompatActivity() {
    private lateinit var bookDetailsViewModel: BookDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        bookDetailsViewModel = ViewModelProvider(this).get(BookDetailsViewModel::class.java)
        val book: Book = this.intent.getSerializableExtra("Book") as Book
        bookDetailsViewModel.setBook(book)
        bookDetailsViewModel.getBook().observe(this, Observer {
            loadDetails()
        })
        lendButton.setOnClickListener {
            displayLendDialog()
        }
        borrowButton.setOnClickListener {
            createBorrowDialog()
        }
    }

    private fun createBorrowDialog() {
        val inflater = this.layoutInflater
        val alertDialog: AlertDialog? = this.let {
            val builder = AlertDialog.Builder(it).apply {
                setView(inflater.inflate(R.layout.borrow_lend_dialog, null))
                setPositiveButton(R.string.borrow_text
                ) { dialog, id ->
                    Toast.makeText(it,"Borrow", Toast.LENGTH_SHORT).show()
                    //TODO("Implements borrow functionality")
                }
                setNegativeButton(android.R.string.cancel
                ) { dialog, id ->
                    dialog.cancel()
                }
                setTitle(getString(R.string.borrow_text).toUpperCase())
            }

            builder.create()
        }
        alertDialog?.show()
        alertDialog?.unit_dialog_label?.text = getString(R.string.from_dialog_text)
    }

    private fun displayLendDialog() {
        val inflater = this.layoutInflater
        val alertDialog: AlertDialog? = this.let {
            val builder = AlertDialog.Builder(it).apply {
                setView(inflater.inflate(R.layout.borrow_lend_dialog, null))
                setPositiveButton(R.string.lend_text
                ) { dialog, id ->
                    Toast.makeText(it,"Lend", Toast.LENGTH_SHORT).show()
                }
                setNegativeButton(android.R.string.cancel
                ) { dialog, id ->
                    dialog.cancel()
                }
                setTitle(getString(R.string.lend_text).toUpperCase())
            }
            builder.create()
        }
        alertDialog?.show()
        alertDialog?.unit_dialog_label?.text = getString(R.string.to_dialog_text)
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
