package com.szymanski.myownlibrary.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.szymanski.myownlibrary.data.openLibraryAPI.models.Book
import java.time.LocalDate
import java.util.*

class BookDetailsViewModel: ViewModel() {
    private val book: MutableLiveData<Book> = MutableLiveData<Book>()


    public fun lendBook(unit: String, endDate: LocalDate){
        //TODO("ADD book to rent books")
    }
    public fun setBook(book:Book){
        this.book.value = book
    }

    public fun getBook(): MutableLiveData<Book> {
        return book
    }
    fun markBookAsBorrowed(unit: String, date: Date): Boolean {
        return true
    }
    fun markBookAsLent(unit: String, date: Date): Boolean {
        return false
    }

}