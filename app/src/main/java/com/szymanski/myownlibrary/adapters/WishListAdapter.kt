package com.szymanski.myownlibrary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.data.models.Book
import kotlinx.android.synthetic.main.wishlist_item.view.*
import java.util.zip.Inflater

class WishListAdapter: RecyclerView.Adapter<WishListAdapter.ItemViewHolder>() {
    private val books = ArrayList<Book>()

    fun setBooks(books: ArrayList<Book>){
        if(this.books.isNotEmpty()){
            this.books.clear()
        }
        this.books.addAll(books)
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.wishlist_item,parent, false)
        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(books[position])
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(book: Book){
            with(itemView){
                Glide.with(this)
                    .load(book.cover)
                    .error(R.drawable.books)
                    .into(wishItemCover)
                wishItemTitle.text = book.title
                wishItemAuthors.text = book.authors.toString()
            }
        }
    }
}