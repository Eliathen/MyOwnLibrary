package com.szymanski.myownlibrary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide

import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.data.firebase.models.FirebaseBook

import kotlinx.android.synthetic.main.wishlist_item.view.*

class WishListAdapter(private val listener: ClickListener): RecyclerView.Adapter<WishListAdapter.ItemViewHolder>() {

    private val books = mutableListOf<FirebaseBook>()

    fun setBooks(firebaseBooks: MutableList<FirebaseBook>){
        if(this.books.isNotEmpty()){
            this.books.clear()
        }
        this.books.addAll(firebaseBooks)
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

        fun bind(firebaseBook: FirebaseBook){
            with(itemView){
                if(firebaseBook.cover.isNotEmpty()){
                    Glide.with(this)
                        .load(firebaseBook.cover)
                        .error(R.drawable.books)
                        .into(wishItemCover)
                } else {
                    Glide.with(this)
                        .load(R.drawable.books)
                        .into(wishItemCover)
                }
                wishItemTitle.text = firebaseBook.title
                wishItemAuthors.text = firebaseBook.authors.toString()
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