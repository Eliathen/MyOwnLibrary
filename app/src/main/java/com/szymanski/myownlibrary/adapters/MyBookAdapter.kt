package com.szymanski.myownlibrary.adapters

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.opengl.Visibility
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.OneShotPreDrawListener.add
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.activities.BookDetails
import com.szymanski.myownlibrary.data.models.Book
import com.szymanski.myownlibrary.fragments.LendBorrowFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.view.*
import kotlinx.android.synthetic.main.my_book_item.view.*

class MyBookAdapter(var activity: FragmentActivity?): RecyclerView.Adapter<MyBookAdapter.MyBookViewHolder>() {
    private val books by lazy { mutableListOf<Book>()}

    fun setBooks(books: List<Book>){
        if(books.isNotEmpty()){
            this.books.clear()
        }
        this.books.addAll(books)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBookViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.my_book_item, parent, false)

        return MyBookViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: MyBookViewHolder, position: Int) {
        val book = books[position]
        holder.bind(book)
    }
    inner class MyBookViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(book: Book){
            with(itemView){
//                Glide.with(this)
//                    .load(book.coverUrl)
//                    .into(bookCover)
                bookTitle.text = book.title
                bookAuthors.text = book.authors.toString().substring(1,book.authors.toString().length-1)
                bookYear.text = book.yearOfPublished

                setOnClickListener {
                    val intent = Intent(this@MyBookAdapter.activity?.baseContext, BookDetails::class.java)
                    intent.putExtra("Book", book)
                    this@MyBookAdapter.activity?.startActivity(intent)
                }
            }
        }
    }
}