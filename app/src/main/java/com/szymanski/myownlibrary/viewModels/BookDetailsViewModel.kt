package com.szymanski.myownlibrary.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.szymanski.myownlibrary.data.firebase.models.FirebaseBook
import com.szymanski.myownlibrary.data.firebase.models.FirebaseRent
import com.szymanski.myownlibrary.data.firebase.services.FirebaseServiceImpl
import java.time.LocalDate
import java.util.*

class BookDetailsViewModel: ViewModel() {
    private val firebaseBook: MutableLiveData<FirebaseBook> = MutableLiveData<FirebaseBook>()


    fun setBook(firebaseBook:FirebaseBook){
        this.firebaseBook.value = firebaseBook
    }

    fun getBook(): MutableLiveData<FirebaseBook> {
        return firebaseBook
    }
    fun markBookAsBorrowed(unit: String, date: Date): String {
        val firebaseService = FirebaseServiceImpl()
        val rent = FirebaseRent("", firebaseBook.value!!, Calendar.getInstance().time, date , unit,
            isBorrowed = true,
            isLent = false,
            isFinished = false
        )
        return firebaseService.markBookAsBorrowed(rent)

    }
    fun markBookAsLent(unit: String, date: Date): String {
        val firebaseService = FirebaseServiceImpl()
        val rent = FirebaseRent("",firebaseBook.value!!, Calendar.getInstance().time, date , unit,
            isBorrowed = false,
            isLent = true,
            isFinished = false
        )
        return firebaseService.markBookAsLent(rent)
    }

}