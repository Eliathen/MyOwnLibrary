package com.szymanski.myownlibrary.adapters

import android.content.Intent
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.activities.BookDetailsActivity
import com.szymanski.myownlibrary.data.models.Book
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
                val cover = book.cover
                Glide.with(this)
                    .load(cover)
                    .error(R.drawable.books)
                    .into(bookCover)
                bookTitle.text = book.title
                bookAuthors.text = SpannableStringBuilder(book.authors.toString().substring(1,book.authors.toString().length-1))
                bookYear.text = book.publishedDate

                setOnClickListener {
                    val intent = Intent(this@MyBookAdapter.activity?.baseContext, BookDetailsActivity::class.java)
                    intent.putExtra("Book", book)
                    this@MyBookAdapter.activity?.startActivity(intent)
                }
            }
        }
    }
}