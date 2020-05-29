package com.szymanski.myownlibrary.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.szymanski.myownlibrary.data.firebase.models.FirebaseBook
import com.szymanski.myownlibrary.data.firebase.services.FirebaseServiceImpl

class SaveBookManuallyViewModel: ViewModel() {
    private val newBook: MutableLiveData<FirebaseBook> = MutableLiveData()
    private var errorMessage: MutableLiveData<String> = MutableLiveData()
    fun setNewBook(firebaseBook: FirebaseBook){
        newBook.value = firebaseBook
    }
    fun saveBook(){
        val firebaseService = FirebaseServiceImpl()
        errorMessage.value = firebaseService.saveMyBook(newBook.value!!)
    }
    fun getErrorMessage() = errorMessage
}