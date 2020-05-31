package com.szymanski.myownlibrary.adapters

import android.content.Context
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.szymanski.myownlibrary.R
import kotlinx.android.synthetic.main.authors_list_element.view.*

class AuthorsAdapter(val context: Context): RecyclerView.Adapter<AuthorsAdapter.ViewHolder>() {

    private val authors: ArrayList<String> = arrayListOf("")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.authors_list_element, parent, false)
        return ViewHolder(itemView)
    }

    fun getAuthors(): ArrayList<String>{
        return authors
    }
    fun setAuthors(authors: ArrayList<String>){
        if(this.authors.isNotEmpty()){
            this.authors.clear()
        }
        this.authors.addAll(authors)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return authors.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) {
            with(itemView) {
                author.text = Editable.Factory.getInstance().newEditable(authors[position])
                this.addAuthorField.setOnClickListener {
                    authors.add("")
                    notifyDataSetChanged()
                }
                this.author.addTextChangedListener {
                    authors[position] = it.toString()
                }
                if(position > 0){
                    this.addAuthorField.setImageResource(R.drawable.ic_remove_red_24dp)
                    this.addAuthorField.setOnClickListener {
                        authors.removeAt(position)
                        notifyDataSetChanged()
                    }
                }
            }


        }
    }
}