package com.szymanski.myownlibrary.viewModels

import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Base64

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.szymanski.myownlibrary.converters.BookConverter
import com.szymanski.myownlibrary.data.firebase.services.FirebaseServiceImpl

import com.szymanski.myownlibrary.data.openLibraryAPI.models.SearchBook
import com.szymanski.myownlibrary.data.openLibraryAPI.repositories.BookRepository
import com.szymanski.myownlibrary.data.openLibraryAPI.repositories.SearchRepository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class KeywordSearchResultViewModel(application: Application): AndroidViewModel(application) {

    private val searchResult = MutableLiveData<MutableList<SearchBook>>()

    private val isDataLoaded = MutableLiveData<Boolean>().apply{ value = false}

    private val result = MutableLiveData<String>()

    fun findBooks(keyword: String){
        isDataLoaded.value = false
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = kotlin.runCatching {
                    SearchRepository.findBooksByKeyword(keyword)
                }
                result.onFailure {
                    isDataLoaded.postValue(true)
                }
                result.onSuccess {
                    searchResult.postValue(it.searchBooks)
                    isDataLoaded.postValue(true)
                }
            }

        }
    }
    fun saveBook(searchBook: SearchBook){
        val firebaseService = FirebaseServiceImpl()
        var message = ""
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                kotlin.runCatching {
                    val book = BookRepository.getBookByIsbn(searchBook.isbn).bookInfo.book
                    if(book.cover.isEmpty()){
                        val fireBook = BookConverter.toFirebaseBook(book, "")
                        result.postValue(firebaseService.saveMyBook(fireBook))

                    } else {
                        Glide.with(getApplication<Application>().baseContext)
                            .asBitmap()
                            .load(book.cover)
                            .into(object : CustomTarget<Bitmap>() {
                                override fun onLoadCleared(placeholder: Drawable?) {
                                }

                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap>?
                                ) {
                                    val stream = ByteArrayOutputStream()
                                    if (resource.compress(Bitmap.CompressFormat.PNG, 100, stream)) {
                                        val image = Base64.encodeToString(
                                            stream.toByteArray(),
                                            Base64.DEFAULT
                                        )
                                        val fireBook = BookConverter.toFirebaseBook(book, image)
                                        result.postValue(firebaseService.saveMyBook(fireBook))
                                    }
                                }
                            })
                    }
                }

            }
        }
    }
    fun getSearchResult(): MutableLiveData<MutableList<SearchBook>>{
        return searchResult
    }
    fun getDataLoaded(): MutableLiveData<Boolean>{
        return isDataLoaded
    }
    fun getResult(): MutableLiveData<String>{
        return result
    }
}