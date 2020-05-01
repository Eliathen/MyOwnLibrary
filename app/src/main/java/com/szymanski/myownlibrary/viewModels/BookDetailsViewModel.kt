package com.szymanski.myownlibrary.viewModels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.szymanski.myownlibrary.data.models.Book
import com.szymanski.myownlibrary.data.models.Rent
import java.time.LocalDate

class BookDetailsViewModel: ViewModel() {
    private val book: MutableLiveData<Book> = MutableLiveData<Book>()


    @RequiresApi(Build.VERSION_CODES.O)
    public fun lendBook(unit: String, endDate: LocalDate){
        val rent = book.value?.let { Rent(it, LocalDate.now(), endDate, unit) }
        //TODO("ADD book to rent books")
    }
    public fun setBook(book:Book){
        this.book.value = book
    }

    public fun getBook(): MutableLiveData<Book> {
        return book
    }

}