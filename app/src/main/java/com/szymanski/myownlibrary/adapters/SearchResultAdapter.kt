package com.szymanski.myownlibrary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide

import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.data.models.Book

import kotlinx.android.synthetic.main.keyword_search_result_item.view.*

class SearchResultAdapter(private val onItemListener: OnItemListener): RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    private val books: ArrayList<Book> = arrayListOf()

    public fun setBooks(books: ArrayList<Book>){
        if(this.books.isNotEmpty()){
            this.books.clear()
        }
        this.books.addAll(books)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.keyword_search_result_item, parent, false)
        return ViewHolder(itemView, onItemListener)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = books[position]
        holder.bind(book)
    }

    inner class ViewHolder(itemView: View, private val onItemListener: OnItemListener): RecyclerView.ViewHolder(itemView), View.OnClickListener {

        fun bind(book: Book){
            with(itemView){
                Glide.with(this)
                    .load(book.cover)
                    .error(R.drawable.books)
                    .into(wishItemCover)
                searchTitle.text = book.title
                searchAuthors.text = book.authors.toString()
                saveBookButton.setOnClickListener {
                    Toast.makeText(this.context,"Save book", Toast.LENGTH_SHORT).show()
                }
                this.setOnClickListener(this@ViewHolder)
            }
        }

        override fun onClick(v: View?) {
            onItemListener.onItemClick(adapterPosition)
        }
    }

    interface OnItemListener{
        fun onItemClick(position: Int)
    }
}