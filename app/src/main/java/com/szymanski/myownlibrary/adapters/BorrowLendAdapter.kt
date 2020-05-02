package com.szymanski.myownlibrary.adapters

import android.util.Log
import com.szymanski.myownlibrary.data.models.Rent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.szymanski.myownlibrary.R
import kotlinx.android.synthetic.main.borrow_lend_item.view.*

class BorrowLendAdapter(var activity: FragmentActivity?): RecyclerView.Adapter<BorrowLendAdapter.RentViewHolder>() {
    private val rents by lazy { mutableListOf<Rent>()}

    fun setRents(rents: List<Rent>){
        if(rents.isNotEmpty()){
            this.rents.clear()
        }
        this.rents.addAll(rents)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentViewHolder {
        Log.i("BorrowLendAdapter", "BorrowLendAdapter")
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.borrow_lend_item, parent, false)
        return RentViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return rents.size
    }

    override fun onBindViewHolder(holder: RentViewHolder, position: Int) {
        Log.i("BorrowLendAdapter", "BorrowLendAdapter")
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
                unit.text = rent.unit
                date.text = rent.endDate
                endRentButton.setOnClickListener {
                    Toast.makeText(activity,"Finished rent", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}