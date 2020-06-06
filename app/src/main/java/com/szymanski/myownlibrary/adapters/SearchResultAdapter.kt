package com.szymanski.myownlibrary.adapters

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.data.openLibraryAPI.models.SearchBook

import kotlinx.android.synthetic.main.keyword_search_result_item.view.*

class SearchResultAdapter(private val onItemListener: OnItemListener): RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    private val books: MutableList<SearchBook> = arrayListOf()

    fun setBooks(books: MutableList<SearchBook>){
        if(this.books.isNotEmpty()){
            this.books.clear()
        }
        this.books.addAll(books)
        notifyDataSetChanged()
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

        fun bind(book: SearchBook){
            with(itemView){
                itemView.loadCoverProgressBar.visibility = View.VISIBLE
                Glide.with(this)
                    .load(book.cover)
                    .error(R.drawable.blank_cover)
                    .listener(object: RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            itemView.loadCoverProgressBar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            if(resource?.intrinsicWidth == 300){
                                resultItemCover.setImageResource(R.drawable.blank_cover)
                                itemView.loadCoverProgressBar.visibility = View.GONE
                                return true
                            }
                            itemView.loadCoverProgressBar.visibility = View.GONE
                            return false
                        }
                    })
                    .into(resultItemCover)
                searchTitle.text = book.title
                searchAuthors.text = getAuthors(book.authors)
                saveBookButton.setOnClickListener { onItemListener.saveBook(adapterPosition) }
                this.setOnClickListener(this@ViewHolder)
            }
        }

        override fun onClick(v: View?) {
                        onItemListener.onItemClick(adapterPosition)
        }
        private fun getAuthors(authors: MutableList<String>): String {
            var readyAuthors = ""
            authors.forEach {
                readyAuthors += it+""
            }
            return readyAuthors.trim()
        }
    }

    interface OnItemListener{
        fun onItemClick(position: Int)
        fun saveBook(position: Int)
    }
}