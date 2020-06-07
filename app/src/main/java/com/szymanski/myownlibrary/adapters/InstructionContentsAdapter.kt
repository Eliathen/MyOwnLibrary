package com.szymanski.myownlibrary.adapters

import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.recyclerview.widget.RecyclerView
import com.szymanski.myownlibrary.R
import kotlinx.android.synthetic.main.instruction_item.view.*

class InstructionContentsAdapter(private val listeners: InstructionListeners): RecyclerView.Adapter<InstructionContentsAdapter.ViewHolder>() {

    private val instructionContents = ArrayList<String>()

    fun setInstruction(contents: ArrayList<String>){
        if(instructionContents.isNotEmpty()){
            instructionContents.clear()
        }
        instructionContents.addAll(contents)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.instruction_item, parent, false)
        return ViewHolder(view, listeners)
    }

    override fun getItemCount(): Int {
        return instructionContents.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val content = instructionContents[position]
        holder.bind(content)
    }

    inner class ViewHolder(itemView: View, private val listeners: InstructionListeners): RecyclerView.ViewHolder(itemView), View.OnClickListener{

        fun bind(text: String){
            itemView.instructionContentsElement.text = text
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listeners.onItemClick(adapterPosition)
        }

    }
    interface InstructionListeners{
        fun onItemClick(position: Int)
    }

}