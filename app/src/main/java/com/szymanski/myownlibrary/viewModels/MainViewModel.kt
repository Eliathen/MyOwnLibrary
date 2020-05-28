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

import com.szymanski.myownlibrary.SortType
import com.szymanski.myownlibrary.data.firebase.FirebaseBookConverter

import com.szymanski.myownlibrary.data.firebase.services.FirebaseService
import com.szymanski.myownlibrary.data.firebase.services.FirebaseServiceImpl

import com.szymanski.myownlibrary.data.openLibraryAPI.models.Book
import com.szymanski.myownlibrary.data.firebase.models.FirebaseRent
import com.szymanski.myownlibrary.data.openLibraryAPI.repositories.BookRepository
import com.szymanski.myownlibrary.exceptions.NotFoundIsbn

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.*

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val books = MutableLiveData<MutableList<Book>>()
    private val borrowLendBooks = MutableLiveData<MutableList<FirebaseRent>>()
    private val wishList = MutableLiveData<MutableList<Book>>()
    private val error = MutableLiveData<NotFoundIsbn>()
    private var isBookSave = MutableLiveData<Boolean>()
    private lateinit var firebaseService: FirebaseService

    fun searchBookByIsbn(isbn: String){
        var readyIsbn = "isbn:" + isbn.trim().replace("-", "")
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                runCatching{BookRepository.getBookByIsbn(readyIsbn)}
            }
            result.onSuccess {
                saveBook(it.bookInfo.book)
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
    fun getBorrowLendBooks(): MutableLiveData<MutableList<FirebaseRent>>{
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
        val rent = FirebaseRent(
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
        val rent1 = FirebaseRent(
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

    private fun saveBook(book: Book){
        firebaseService =
            FirebaseServiceImpl()

        Glide.with(getApplication<Application>().baseContext)
            .asBitmap()
            .load(book.cover)
            .into(object: CustomTarget<Bitmap>(){
                override fun onLoadCleared(placeholder: Drawable?) {
                }
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val stream = ByteArrayOutputStream()
                    if(resource.compress(Bitmap.CompressFormat.PNG, 100, stream)) {
                        val image = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
                        val fireBook = FirebaseBookConverter().toFirebaseBook(book, image)
                        firebaseService.saveMyBook(fireBook)
                    }
                }
            })
    }
}