package com.szymanski.myownlibrary.adapters

import android.annotation.SuppressLint
import android.text.Editable
import android.util.Log

import com.szymanski.myownlibrary.data.firebase.models.FirebaseRent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.FragmentActivity

import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide

import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.converters.ImageConverter

import kotlinx.android.synthetic.main.lend_borrow_item.view.*
import java.text.SimpleDateFormat

class LendBorrowAdapter(var activity: FragmentActivity?, var listeners: LendBorrowItemListeners): RecyclerView.Adapter<LendBorrowAdapter.RentViewHolder>() {

    private val rents by lazy { mutableListOf<FirebaseRent>()}

    fun setRents(firebaseRents: List<FirebaseRent>){
        if(firebaseRents.isNotEmpty()){
            this.rents.clear()
        }
        this.rents.addAll(firebaseRents)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.lend_borrow_item, parent, false)
        return RentViewHolder(itemView, listeners)
    }

    override fun getItemCount(): Int {
        return rents.size
    }

    override fun onBindViewHolder(holder: RentViewHolder, position: Int) {
        val rent = rents[position]
        holder.bind(rent)
    }
    inner class RentViewHolder(itemView: View, private val listeners: LendBorrowItemListeners): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        @SuppressLint("SimpleDateFormat")
        fun bind(firebaseRent: FirebaseRent){
            with(itemView){
                if(firebaseRent.firebaseBook.cover.isNotEmpty()){
                    Glide.with(this)
                        .load(ImageConverter.base64ToBitmap(firebaseRent.firebaseBook.cover))
                        .error(R.drawable.books)
                        .into(cover)
                } else {
                    Glide.with(this)
                        .load(R.drawable.books)
                        .into(cover)
                }
                title.text = firebaseRent.firebaseBook.title
                unit.text = Editable.Factory.getInstance().newEditable(firebaseRent.unit)
                date.text = SimpleDateFormat("dd/MM/yyyy").format(firebaseRent.endDate.time)
                endRentButton.setOnClickListener(this@RentViewHolder)
            }
        }

        override fun onClick(v: View?) {
            listeners.markBookAsReturn(v, adapterPosition)
        }
    }

    interface LendBorrowItemListeners{
        fun markBookAsReturn(view: View?, position: Int)
    }
}