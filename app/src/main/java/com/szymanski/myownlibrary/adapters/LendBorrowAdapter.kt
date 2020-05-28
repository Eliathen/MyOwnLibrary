package com.szymanski.myownlibrary.adapters

import android.text.Editable

import com.szymanski.myownlibrary.data.firebase.Rent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Toast

import androidx.fragment.app.FragmentActivity

import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide

import com.szymanski.myownlibrary.R

import kotlinx.android.synthetic.main.lend_borrow_item.view.*

class LendBorrowAdapter(var activity: FragmentActivity?): RecyclerView.Adapter<LendBorrowAdapter.RentViewHolder>() {
    private val rents by lazy { mutableListOf<Rent>()}

    fun setRents(rents: List<Rent>){
        if(rents.isNotEmpty()){
            this.rents.clear()
        }
        this.rents.addAll(rents)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.lend_borrow_item, parent, false)
        return RentViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return rents.size
    }

    override fun onBindViewHolder(holder: RentViewHolder, position: Int) {
        val rent = rents[position]
        holder.bind(rent)
    }
    inner class RentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(rent: Rent){
            with(itemView){
                Glide.with(this)
                    .load(rent.book.cover)
                    .error(R.drawable.books)
                    .into(cover)
                title.text = rent.book.title
                unit.text = Editable.Factory.getInstance().newEditable(rent.unit)
                date.text = rent.endDate
                endRentButton.setOnClickListener {
                    Toast.makeText(activity,"Finished rent", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}