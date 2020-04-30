package com.szymanski.myownlibrary.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.data.models.Book
import com.szymanski.myownlibrary.data.repositories.BookRepository
import com.szymanski.myownlibrary.exceptions.NotFoundIsbn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel: ViewModel() {
    private val books = MutableLiveData<MutableList<Book>>()
    private val wishList = MutableLiveData<MutableList<Book>>()
    private val error = MutableLiveData<NotFoundIsbn>()
    private var isBookSave = MutableLiveData<Boolean>()

    fun searchBookByIsbn(isbn: String){
        var readyIsbn = "isbn:" + isbn.trim().replace("-", "")
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                runCatching{BookRepository.getBookByIsbn(readyIsbn)}
            }
            result.onSuccess {
                val list: MutableList<Book> = mutableListOf()
                books.value?.let { books -> list.addAll(books) }
                list.add(it.bookInfo.book)
                books.value = list
                isBookSave.value = true
            }
            result.onFailure {
                error.value = NotFoundIsbn("book not found")
            }
        }
    }
    fun getBooks(): MutableLiveData<MutableList<Book>>{
        return books
    }
    fun getWishList(): MutableLiveData<MutableList<Book>>{
        return wishList
    }
    fun selectedBook(book: Book){
        books.value?.add(book)
    }
    fun getError(): MutableLiveData<NotFoundIsbn>{
        return error
    }
    fun getIsBookSaved(): MutableLiveData<Boolean> {
        return isBookSave
    }
    fun setIsBookSaved(isSaved: Boolean){
        this.isBookSave.value = isSaved
    }

}