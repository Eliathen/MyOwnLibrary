package com.szymanski.myownlibrary.viewModels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.szymanski.myownlibrary.SortType

import android.util.Log

import com.szymanski.myownlibrary.data.openLibraryAPI.models.Book
import com.szymanski.myownlibrary.data.firebase.Rent
import com.szymanski.myownlibrary.data.openLibraryAPI.repositories.BookRepository
import com.szymanski.myownlibrary.exceptions.NotFoundIsbn

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainViewModel: ViewModel() {
    private val books = MutableLiveData<MutableList<Book>>()
    private val borrowLendBooks = MutableLiveData<MutableList<Rent>>()
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
    fun setWishList(list: MutableList<Book>){
        this.wishList.value = list
    }
    fun getBorrowLendBooks(): MutableLiveData<MutableList<Rent>>{
        return borrowLendBooks
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
    fun loadExampleBorrowBook(){
        val rent = Rent(
            Book(
                "9780641723445",
                "The ligthing thief",
                arrayListOf("Rick Riordan"),
                "2005",
                377,
                "https://covers.openlibrary.org/b/id/7989100-M.jpg"
            ),
            "startDate", "end", "To John"
        )
        val rent1 = Rent(
            Book(
                "9781857230765",
                "The Eye of the World (Wheel of Time)",
                arrayListOf("Robert Jordan"),
                "1992",
                377,
                "https://covers.openlibrary.org/b/id/908780-M.jpg"
            ),
            "startDate1", "end1", "To Johnny"
        )
        borrowLendBooks.value?.add(rent)
        borrowLendBooks.value?.add(rent1)
    }
    fun setBooks(books: MutableList<Book>){
        this.books.value = books
    }
    fun removeBookFromWishList(book: Book){

    }
    fun markBookFromWishListAsOwn(book: Book){

    }
    fun sortAllLists(type: SortType){
        val myBooks = books.value
        val wish = wishList.value
        when(type){
            SortType.TITLE_ASCENDING -> {
                myBooks?.sortBy { it.title.toLowerCase(Locale.ROOT) }
                wish?.sortBy { it.title.toLowerCase(Locale.ROOT) }
            }
            SortType.TITLE_DESCENDING -> {
                myBooks?.sortByDescending { it.title.toLowerCase(Locale.ROOT) }
                wish?.sortByDescending { it.title.toLowerCase(Locale.ROOT) }
            }
            SortType.PUBLISHED_YEAR_ASCENDING -> {
                myBooks?.sortByDescending { it.publishedYear }
                wish?.sortByDescending { it.publishedYear }
            }
            SortType.PUBLISHED_YEAR_DESCENDING -> {
                myBooks?.sortBy { it.publishedYear }
                wish?.sortBy { it.publishedYear }
            }
        }
        myBooks?.let { books.value = it }
        wish?.let { this.wishList.value = it}
    }

}