package com.szymanski.myownlibrary.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast

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
import java.time.LocalDate

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

        setListeners()
        bookDetailsViewModel.getBook().observe(this, Observer {
            loadDetails()
        })

    }

    private fun displayBorrowDialog() {

        val inflater = this.layoutInflater
        val alertDialog: AlertDialog? = this.let {
            val dialogLayout = inflater.inflate(R.layout.borrow_lend_dialog, null)
            dialogLayout.calendarView.minDate = Calendar.getInstance().timeInMillis
            var date = Date()
            dialogLayout.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
                date = Date(GregorianCalendar(year, month - 1, dayOfMonth).timeInMillis)
            }
            val builder = AlertDialog.Builder(it).apply {
                setView(dialogLayout)
                setPositiveButton(R.string.borrow_text
                ) { _, _ ->
                    val unit = dialogLayout.unit_dialog.text.toString()
                    val result = bookDetailsViewModel.markBookAsBorrowed(unit, date)
                    if(result == "Success"){
                        displayResultMessage(getString(R.string.borrow_success_message))
                        setButtonAsDisable(lendButton)
                        setButtonAsDisable(borrowButton)
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
            var date = Date()
            dialogLayout.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
                date = Date(GregorianCalendar(year, month - 1, dayOfMonth).timeInMillis)
            }
            val builder = AlertDialog.Builder(it).apply {
                setView(dialogLayout)
                setPositiveButton(R.string.lend_text
                ) { _, _ ->
                    val unit = dialogLayout.unit_dialog.text.toString()
                    val result = bookDetailsViewModel.markBookAsLent(unit, date)
                    if(result == "Success"){
                        displayResultMessage(getString(R.string.lent_success_message))
                        setButtonAsDisable(lendButton)
                        setButtonAsDisable(borrowButton)
                    } else {
                        displayResultMessage(bookDetailsViewModel.markBookAsLent(unit, date))
                    }
                }
                setNegativeButton(android.R.string.cancel
                ) { dialog, _ ->
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
        val book = bookDetailsViewModel.getBook().value!!
        bookDetailsTitle.text = book.title
        if(book.cover.isNotEmpty()){
            Glide.with(this)
                .load(ImageConverter.base64ToBitmap(book.cover))
                .error(R.drawable.books)
                .into(bookDetailsCover)
        }
        bookDetailsAuthors.text = book.authors
                .toString()
                .substring(1,book.authors.toString().length-1)
        bookDetailsPublishedDate.text = book.publishedYear
        bookDetailsPages.text = book.pageCount.toString()
        bookDetailsIsbn.text = book.isbn

    }
    private fun displayResultMessage(message: String){
        Snackbar.make(this.bookDetailsParentLayout, message, Snackbar.LENGTH_SHORT).show()
    }
    private fun setButtonAsDisable(button: Button){
        button.setBackgroundResource(R.drawable.disable_button_shape)
        button.setOnClickListener {
            Toast.makeText(baseContext, "Book is already borrowed/lent", Toast.LENGTH_SHORT).show()
        }
    }
    private fun setListeners(){
        if(bookDetailsViewModel.getBook().value!!.isBorrow){
            setButtonAsDisable(lendButton)
            setButtonAsDisable(borrowButton)
        } else {
            lendButton.setOnClickListener {
                displayLendDialog()
            }
            borrowButton.setOnClickListener {
                displayBorrowDialog()
            }
        }
    }
}
