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
import com.szymanski.myownlibrary.converters.ImageConverter
import com.szymanski.myownlibrary.data.firebase.models.FirebaseBook

import kotlinx.android.synthetic.main.my_book_item.view.*

import android.util.Log

class MyBookAdapter(var activity: FragmentActivity?, var onBookItemListener: OnBookItemListener): RecyclerView.Adapter<MyBookAdapter.MyBookViewHolder>() {
    private val books by lazy { mutableListOf<FirebaseBook>()}

    fun setBooks(firebaseBooks: List<FirebaseBook>){
        if(firebaseBooks.isNotEmpty()){
            this.books.clear()
        }
        this.books.addAll(firebaseBooks)
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

        fun bind(firebaseBook: FirebaseBook){
            with(itemView){
                val cover = firebaseBook.cover
                imageProgressBar.visibility = View.VISIBLE
                if(cover.isNotEmpty()) {
                    Glide.with(this)
                        .load(ImageConverter.base64ToBitmap(cover))
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
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
                }
                bookTitle.text = firebaseBook.title
                bookAuthors.text = SpannableStringBuilder(firebaseBook.authors.toString().substring(1,firebaseBook.authors.toString().length-1))
                bookYear.text = firebaseBook.publishedYear

                setOnClickListener {
                    val intent = Intent(this@MyBookAdapter.activity?.baseContext, BookDetailsActivity::class.java)
                    intent.putExtra("Book", firebaseBook)
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