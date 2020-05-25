package com.szymanski.myownlibrary.adapters

import android.content.Intent

import android.graphics.drawable.Drawable

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.FragmentActivity

import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.activities.BookDetailsActivity
import com.szymanski.myownlibrary.data.models.Book

import kotlinx.android.synthetic.main.my_book_item.view.*

class MyBookAdapter(var activity: FragmentActivity?, var onBookItemListener: OnBookItemListener): RecyclerView.Adapter<MyBookAdapter.MyBookViewHolder>() {
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

        return MyBookViewHolder(itemView, onBookItemListener)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: MyBookViewHolder, position: Int) {
        val book = books[position]
        holder.bind(book)
    }
    inner class MyBookViewHolder(itemView: View, onBookItemListener: OnBookItemListener): RecyclerView.ViewHolder(itemView), View.OnClickListener{

        fun bind(book: Book){
            with(itemView){
                val cover = book.cover
                Glide.with(this)
                    .load(cover)
                    .listener(object: RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            imageProgressBar.visibility = View.VISIBLE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            imageProgressBar.visibility = View.INVISIBLE
                            return false
                        }
                    })
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
                myBookItemOptions.setOnClickListener(this@MyBookViewHolder)

            }
        }


        override fun onClick(v: View?) {
            onBookItemListener.onClick(itemView.myBookItemOptions, adapterPosition)
        }
    }
    interface OnBookItemListener{
        fun onClick(view: View, position: Int)
    }
}