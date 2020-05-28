package com.szymanski.myownlibrary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide

import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.data.openLibraryAPI.models.Book

import kotlinx.android.synthetic.main.wishlist_item.view.*

class WishListAdapter(private val listener: ClickListener): RecyclerView.Adapter<WishListAdapter.ItemViewHolder>() {
    private val books = mutableListOf<Book>()

    fun setBooks(books: MutableList<Book>){
        if(this.books.isNotEmpty()){
            this.books.clear()
        }
        this.books.addAll(books)
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.wishlist_item,parent, false)
        return ItemViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(books[position])
    }

    inner class ItemViewHolder(itemView: View, private val listener: ClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

        fun bind(book: Book){
            with(itemView){
                Glide.with(this)
                    .load(book.cover)
                    .error(R.drawable.books)
                    .into(wishItemCover)
                wishItemTitle.text = book.title
                wishItemAuthors.text = book.authors.toString()
                itemOptions.setOnClickListener(this@ItemViewHolder)
            }
        }

        override fun onClick(v: View?) {
            listener.onClick(itemView.itemOptions, adapterPosition)

        }
    }
    interface ClickListener{
        fun onClick(view:View, position: Int)
    }
}