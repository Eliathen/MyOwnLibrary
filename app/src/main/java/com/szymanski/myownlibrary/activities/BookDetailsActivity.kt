package com.szymanski.myownlibrary.activities

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle

import androidx.appcompat.app.AlertDialog

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.converters.ImageConverter
import com.szymanski.myownlibrary.data.firebase.models.FirebaseBook
import com.szymanski.myownlibrary.viewModels.BookDetailsViewModel

import kotlinx.android.synthetic.main.activity_book_details.*
import kotlinx.android.synthetic.main.borrow_lend_dialog.*
import kotlinx.android.synthetic.main.borrow_lend_dialog.view.*

import java.util.*


class BookDetailsActivity : AppCompatActivity() {
    private lateinit var bookDetailsViewModel: BookDetailsViewModel
    private lateinit var firebaseBook: FirebaseBook

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        bookDetailsViewModel = ViewModelProvider(this).get(BookDetailsViewModel::class.java)

        firebaseBook = this.intent.getSerializableExtra("Book") as FirebaseBook

        bookDetailsViewModel.setBook(firebaseBook)
        bookDetailsViewModel.getBook().observe(this, Observer {
            loadDetails()
        })
        lendButton.setOnClickListener {
            displayLendDialog()
        }
        borrowButton.setOnClickListener {
            displayBorrowDialog()
        }
    }

    private fun displayBorrowDialog() {
        val inflater = this.layoutInflater
        val alertDialog: AlertDialog? = this.let {
            val dialogLayout = inflater.inflate(R.layout.borrow_lend_dialog, null)
            dialogLayout.calendarView.minDate = Calendar.getInstance().timeInMillis
            val builder = AlertDialog.Builder(it).apply {
                setView(dialogLayout)
                setPositiveButton(R.string.borrow_text
                ) { _, _ ->
                    val unit = dialogLayout.unit_dialog.text.toString()
                    val date = Date(dialogLayout.calendarView.date)
                    if(bookDetailsViewModel.markBookAsBorrowed(unit, date)){
                        displayResultMessage(getString(R.string.borrow_success_message))
                    } else {
                        displayResultMessage(getString(R.string.error_message))
                    }

                }
                setNegativeButton(android.R.string.cancel
                ) { dialog, _ ->
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
            val dialogLayout = inflater.inflate(R.layout.borrow_lend_dialog, null)
            dialogLayout.calendarView.minDate = Calendar.getInstance().timeInMillis
            val builder = AlertDialog.Builder(it).apply {
                setView(dialogLayout)
                setPositiveButton(R.string.lend_text
                ) { _, _ ->
                    val unit = dialogLayout.unit_dialog.text.toString()
                    val date = Calendar.getInstance().time
                    if(bookDetailsViewModel.markBookAsLent(unit, date)){
                        displayResultMessage(getString(R.string.lend_success_message))
                    } else {
                        displayResultMessage(getString(R.string.error_message))
                    }
                }
                setNegativeButton(android.R.string.cancel
                ) { dialog, id ->
                    dialog.cancel()
                }
                setTitle(getString(R.string.lend_text).toUpperCase(Locale.ROOT))
            }
            builder.create()
        }
        alertDialog?.show()
        alertDialog?.unit_dialog_label?.text = getString(R.string.to_dialog_text)
    }


    private fun loadDetails(){
        bookDetailsTitle.text = bookDetailsViewModel.getBook().value?.title
        Glide.with(this)
            .load(ImageConverter.base64ToBitmap(bookDetailsViewModel.getBook().value?.cover!!))
            .error(R.drawable.books)
            .into(bookDetailsCover)
        bookDetailsAuthors.text = bookDetailsViewModel.getBook().value?.authors
                .toString()
                .substring(1,bookDetailsViewModel.getBook().value?.authors.toString().length-1)
        bookDetailsPublishedDate.text = bookDetailsViewModel.getBook().value?.publishedYear
        bookDetailsPages.text = bookDetailsViewModel.getBook().value?.pageCount.toString()
        bookDetailsIsbn.text = bookDetailsViewModel.getBook().value?.isbn
    }
    private fun displayResultMessage(message: String){
        Snackbar.make(this.bookDetailsParentLayout, message, Snackbar.LENGTH_SHORT).show()
    }
}
