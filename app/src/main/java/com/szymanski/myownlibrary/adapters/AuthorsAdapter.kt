package com.szymanski.myownlibrary.adapters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.szymanski.myownlibrary.R
import kotlinx.android.synthetic.main.authors_list_element.view.*

class AuthorsAdapter(val context: Context): RecyclerView.Adapter<AuthorsAdapter.ViewHolder>() {

    private val fieldList: ArrayList<String> = arrayListOf("")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.authors_list_element, parent, false)
        return ViewHolder(itemView)
    }

    fun getAuthors(): ArrayList<String>{
        return fieldList
    }

    override fun getItemCount(): Int {
        return fieldList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) {
            with(itemView) {
                this.addAuthorField.setOnClickListener {
                    fieldList.add("")
                    notifyDataSetChanged()
                }
                this.author.addTextChangedListener {
                    fieldList[position] = it.toString()
                }
                if(position > 0){
                    this.addAuthorField.setImageResource(R.drawable.ic_remove_red_24dp)
                    this.addAuthorField.setOnClickListener {
                        fieldList.removeAt(position)
                        notifyDataSetChanged()
                    }
                }
            }


        }
    }
}