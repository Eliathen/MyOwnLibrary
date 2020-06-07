package com.szymanski.myownlibrary.viewModels

import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Base64
import android.util.Log

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

    private val isDataLoaded = MutableLiveData<Boolean>().apply { value = false }

    private val isBookSave = MutableLiveData<String>()

    private val isBookSaveByDetails = MutableLiveData<String>()

    private val isBookAddToWishList = MutableLiveData<String>()

    fun findBooks(keyword: String) {
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

    fun saveBook(
        searchBook: SearchBook,
        isBookSavedByDetailsDialog: Boolean
    ) {
        val firebaseService = FirebaseServiceImpl()
        var message = "Success"
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = kotlin.runCatching {
                    val book = BookRepository.getBookByIsbn(searchBook.isbn).bookInfo.book
                    if (book.cover.isEmpty()) {
                        val fireBook = BookConverter.toFirebaseBook(book, "")
                        return@runCatching firebaseService.saveMyBook(fireBook)
                    }
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
                                    message = firebaseService.saveMyBook(fireBook)
                                }
                            }
                        })
                        return@runCatching message
                }
                result.onSuccess {
                    searchBook.isBookSaved = true
                    if(!isBookSavedByDetailsDialog){
                        isBookSave.postValue(message)
                    } else {
                        isBookSaveByDetails.postValue(message)
                    }
                }
                result.onFailure {
                    if(!isBookSavedByDetailsDialog){
                        isBookSave.postValue(message)
                    } else {
                        isBookSaveByDetails.postValue(message)
                    }
                }
            }
        }
    }
    fun addBookToWishList(searchBook: SearchBook){
        val firebaseService = FirebaseServiceImpl()
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val book = BookRepository.getBookByIsbn(searchBook.isbn).bookInfo.book
                if(book.cover.isNotEmpty()){
                    Glide.with(getApplication<Application>().baseContext)
                        .asBitmap()
                        .load(book.cover)
                        .into(object: CustomTarget<Bitmap>(){
                            override fun onLoadCleared(placeholder: Drawable?) {
                            }
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                Log.d("KeyWord", "OnResourceReady")
                                val stream = ByteArrayOutputStream()
                                if(resource.compress(Bitmap.CompressFormat.PNG, 100, stream)) {
                                    val image = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
                                    val fireBook = BookConverter.toFirebaseBook(book, image)
                                    firebaseService.getWishListReference().child(searchBook.isbn).setValue(fireBook)
                                        .addOnSuccessListener {
                                            searchBook.isBookInWishList = true
                                            isBookAddToWishList.value = "Success"
                                        }
                                        .addOnFailureListener {
                                            isBookAddToWishList.value = it.message
                                        }
                                }
                            }
                        })
                } else {
                    Log.d("KeyWord", "If cover is empty")

                    firebaseService.getWishListReference().child(searchBook.isbn).setValue(BookConverter.toFirebaseBook(book, ""))
                        .addOnSuccessListener {
                            searchBook.isBookInWishList = true
                            isBookAddToWishList.value = "Success"
                        }
                        .addOnFailureListener {
                            isBookAddToWishList.value = it.message
                        }
                }
            }
        }


    }
    fun getSearchResult(): MutableLiveData<MutableList<SearchBook>> {
        return searchResult
    }

    fun getDataLoaded(): MutableLiveData<Boolean> {
        return isDataLoaded
    }

    fun getBookSaved(): MutableLiveData<String> {
        return isBookSave
    }
    fun getBookSavedByDetails(): MutableLiveData<String> {
        return isBookSaveByDetails
    }
    fun getBookAddToWishList(): MutableLiveData<String>{
        return isBookAddToWishList
    }

}