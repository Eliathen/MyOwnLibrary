package com.szymanski.myownlibrary.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.data.models.Book
import kotlinx.android.synthetic.main.my_book_item.view.*

class MyBookAdapter: RecyclerView.Adapter<MyBookAdapter.MyBookViewHolder>() {
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
    class MyBookViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(book: Book){
                itemView.bookTitle.text = book.title
                itemView.bookAuthors.text = book.authors.toString().substring(1,book.authors.toString().length-1)
                itemView.bookYear.text = book.yearOfPublished

        }
    }
}