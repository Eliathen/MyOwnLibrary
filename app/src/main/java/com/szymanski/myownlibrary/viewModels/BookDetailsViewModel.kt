package com.szymanski.myownlibrary.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.szymanski.myownlibrary.data.firebase.models.FirebaseBook
import java.time.LocalDate
import java.util.*

class BookDetailsViewModel: ViewModel() {
    private val firebaseBook: MutableLiveData<FirebaseBook> = MutableLiveData<FirebaseBook>()


    public fun lendBook(unit: String, endDate: LocalDate){
        //TODO("ADD book to rent books")
    }
    public fun setBook(firebaseBook:FirebaseBook){
        this.firebaseBook.value = firebaseBook
    }

    public fun getBook(): MutableLiveData<FirebaseBook> {
        return firebaseBook
    }
    fun markBookAsBorrowed(unit: String, date: Date): Boolean {
        return true
    }
    fun markBookAsLent(unit: String, date: Date): Boolean {
        return false
    }

}