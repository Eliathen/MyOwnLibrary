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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import com.szymanski.myownlibrary.SortType

import com.szymanski.myownlibrary.converters.BookConverter

import com.szymanski.myownlibrary.data.firebase.models.FirebaseBook
import com.szymanski.myownlibrary.data.firebase.models.FirebaseRent


import com.szymanski.myownlibrary.data.firebase.services.FirebaseService
import com.szymanski.myownlibrary.data.firebase.services.FirebaseServiceImpl
import com.szymanski.myownlibrary.data.openLibraryAPI.models.Book

import com.szymanski.myownlibrary.data.openLibraryAPI.repositories.BookRepository

import com.szymanski.myownlibrary.exceptions.NotFoundIsbn

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import java.io.ByteArrayOutputStream
import java.util.*

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val books = MutableLiveData<MutableList<FirebaseBook>>()
    private val borrowLendBooks = MutableLiveData<MutableList<FirebaseRent>>()
    private val wishList = MutableLiveData<MutableList<FirebaseBook>>()
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
                isBookSave.value = true
            }
            result.onFailure {
                error.value = NotFoundIsbn("book not found")
            }
        }
    }

    fun removeBookFromWishList(firebaseBook: FirebaseBook){

    }
    fun markBookFromWishListAsOwn(firebaseBook: FirebaseBook){

    }
    fun markBookAsReturn(firebaseRent: FirebaseRent){

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
                        val fireBook = BookConverter().toFirebaseBook(book, image)
                        firebaseService.saveMyBook(fireBook)
                    }
                }
            })
    }
    fun getBookListFromDatabase(){
        firebaseService = FirebaseServiceImpl()
        firebaseService.getMyBookReference().addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onDataChange(p0: DataSnapshot) {
                val bookList = mutableListOf<FirebaseBook>()
                p0.children.forEach {
                    bookList.add(it.getValue(FirebaseBook::class.java)!!)
                }
                books.value = bookList
            }
        })
    }
    fun removeBook(firebaseBook: FirebaseBook): String{
        firebaseService = FirebaseServiceImpl()
        val message = firebaseService.removeBook(firebaseBook)
        if(message == "Success"){
            books.value?.remove(firebaseBook)
        }
        return message
    }
    fun getBorrowLendListFromDatabase(){
        firebaseService = FirebaseServiceImpl()
        firebaseService.getBorrowedBookReference().addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onDataChange(p0: DataSnapshot) {
                val borrowLendList = mutableListOf<FirebaseRent>()
                p0.children.forEach { dataSnapshot ->
                    val rent = dataSnapshot.getValue(FirebaseRent::class.java)!!
                    rent.key = dataSnapshot.key!!
                    rent.takeIf { !it.isFinished }?.let { it1 -> borrowLendList.add(it1) }
                }
                borrowLendBooks.value = borrowLendList
            }

        })
    }
    fun getBooks(): MutableLiveData<MutableList<FirebaseBook>>{
        return books
    }
    fun getWishList(): MutableLiveData<MutableList<FirebaseBook>>{
        return wishList
    }
    fun setWishList(list: MutableList<FirebaseBook>){
        this.wishList.value = list
    }
    fun getBorrowLendBooks(): MutableLiveData<MutableList<FirebaseRent>>{
        return borrowLendBooks
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

    fun setBooks(firebaseBooks: MutableList<FirebaseBook>){
        this.books.value = firebaseBooks
    }
}