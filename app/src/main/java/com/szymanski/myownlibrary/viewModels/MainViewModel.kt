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
    private val lendBorrowBooks = MutableLiveData<MutableList<FirebaseRent>>()
    private val wishList = MutableLiveData<MutableList<FirebaseBook>>()
    private val error = MutableLiveData<NotFoundIsbn>()
    private var isBookSave = MutableLiveData<Boolean>()
    private lateinit var firebaseService: FirebaseService
    private var isMyBookLoaded = MutableLiveData<Boolean>().apply{ value = false}
    private var isWishListLoaded = MutableLiveData<Boolean>().apply{ value = false}
    private var isLendBorrowLoaded = MutableLiveData<Boolean>().apply{ value = false}
    private var booksCopy = mutableListOf<FirebaseBook>()

    fun searchBookByIsbn(isbn: String){
        val readyIsbn = "isbn:" + isbn.trim().replace("-", "")
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                runCatching{
                    BookRepository.getBookByIsbn(readyIsbn)
                }
            }
            result.onSuccess {
                saveBook(it.bookInfo.book)
            }
            result.onFailure {
                error.value = NotFoundIsbn("book not found")
            }
        }
    }

    fun markBookAsReturn(firebaseRent: FirebaseRent, position: Int): String {
        var message = ""
        val firebaseService = FirebaseServiceImpl()
            firebaseService.getBorrowedBookReference()
            .child(firebaseRent.key)
            .child("finished")
            .setValue(true)
            .addOnSuccessListener {
                firebaseService
                    .getMyBookReference()
                    .child(firebaseRent.firebaseBook.isbn)
                    .child("borrow")
                    .setValue(false)
                    .addOnCompleteListener {
                        message = "Book has been marked successful"
                    }
                    .addOnFailureListener {
                        message = it.message.toString()
                    }

            }.addOnFailureListener {
                    message = it.message.toString()
                }
        return message
    }
    fun sortAllLists(type: SortType){
        val myBooks = books.value
        val wish = wishList.value
        val lendBorrow = lendBorrowBooks.value

        when(type){
            SortType.TITLE_ASCENDING -> {
                myBooks?.sortBy { it.title.toLowerCase(Locale.ROOT) }
                wish?.sortBy { it.title.toLowerCase(Locale.ROOT) }
                lendBorrow?.sortBy { it.firebaseBook.title.toLowerCase(Locale.ROOT) }
            }
            SortType.TITLE_DESCENDING -> {
                myBooks?.sortByDescending { it.title.toLowerCase(Locale.ROOT) }
                wish?.sortByDescending { it.title.toLowerCase(Locale.ROOT) }
                lendBorrow?.sortByDescending { it.firebaseBook.title.toLowerCase(Locale.ROOT) }
            }
            SortType.DATE_ASCENDING -> {
                myBooks?.sortByDescending { it.publishedYear }
                wish?.sortByDescending { it.publishedYear }
                lendBorrow?.sortByDescending { it.endDate }
            }
            SortType.DATE_DESCENDING -> {
                myBooks?.sortBy { it.publishedYear }
                wish?.sortBy { it.publishedYear }
                lendBorrow?.sortBy { it.endDate }
            }
        }
        myBooks?.let { this.books.value = it }
        wish?.let { this.wishList.value = it }
        lendBorrow?.let { this.lendBorrowBooks.value = it }
    }

    private fun saveBook(book: Book){
        firebaseService =
            FirebaseServiceImpl()
        if(book.cover.isEmpty()){
            val fireBook = BookConverter.toFirebaseBook(book, "")
            firebaseService.saveMyBook(fireBook)
            return
        }
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
                        val fireBook = BookConverter.toFirebaseBook(book, image)
                        Log.d("OnResourceReady", fireBook.toString())
                        firebaseService.saveMyBook(fireBook)
                    }
                }
            })
    }
    fun getBookListFromDatabase(){
        isMyBookLoaded.value = false
        firebaseService = FirebaseServiceImpl()
        firebaseService.getMyBookReference().addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                isMyBookLoaded.value = true
            }
            override fun onDataChange(p0: DataSnapshot) {
                val bookList = mutableListOf<FirebaseBook>()
                p0.children.forEach {
                    bookList.add(it.getValue(FirebaseBook::class.java)!!)
                }
                books.value = bookList
                booksCopy = bookList
                isMyBookLoaded.value = true
            }
        })
    }
    fun getWishListFromDatabase(){
        isWishListLoaded.value = false
        FirebaseServiceImpl().getWishListReference().addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                isWishListLoaded.value = true
            }
            override fun onDataChange(p0: DataSnapshot) {
                val bookList = mutableListOf<FirebaseBook>()
                p0.children.forEach {
                    bookList.add(it.getValue(FirebaseBook::class.java)!!)
                }
                wishList.value = bookList
                isWishListLoaded.value = true
            }

        })
    }
    fun removeBookFromWishList(firebaseBook: FirebaseBook): String{
        var result = "Success"
        FirebaseServiceImpl().getWishListReference().child(firebaseBook.isbn).removeValue().addOnFailureListener {
            result = it.message.toString()
        }
        return result
    }

    fun markBookFromWishListAsOwn(firebaseBook: FirebaseBook){
        val firebaseService = FirebaseServiceImpl()
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                firebaseService.getWishListReference().child(firebaseBook.isbn).addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                    override fun onDataChange(p0: DataSnapshot) {
                        val book = p0.getValue(FirebaseBook::class.java)!!
                        book.isbn = p0.key!!
                        Log.d("OnDataChange", book.toString())
                        firebaseService.saveMyBook(book)
                        removeBookFromWishList(book)
                    }
                })
            }
        }
    }

    fun removeBook(firebaseBook: FirebaseBook): String{
        firebaseService = FirebaseServiceImpl()
        var message = firebaseService.removeBook(firebaseBook)
        if(message == "Success"){
            books.value?.remove(firebaseBook)
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    runCatching{
                        FirebaseServiceImpl().getBorrowedBookReference().addListenerForSingleValueEvent(object: ValueEventListener{
                            override fun onCancelled(p0: DatabaseError) {
                            }
                            override fun onDataChange(p0: DataSnapshot) {
                                p0.children.forEach{
                                    val rent = it.getValue(FirebaseRent::class.java)!!
                                    rent.key = it.key!!
                                    if(rent.firebaseBook.isbn == firebaseBook.isbn){
                                        val firebaseService = FirebaseServiceImpl()
                                        firebaseService.getBorrowedBookReference()
                                            .child(rent.key)
                                            .child("finished")
                                            .setValue(true)
                                    }
                                }
                            }

                        })
                    }
                }
            }
        }
        return message
    }
    fun getLendBorrowListFromDatabase(){
        isLendBorrowLoaded.value = false
        firebaseService = FirebaseServiceImpl()
        firebaseService.getBorrowedBookReference().addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                isLendBorrowLoaded.value = false
            }
            override fun onDataChange(p0: DataSnapshot) {
                val borrowLendList = mutableListOf<FirebaseRent>()
                p0.children.forEach { dataSnapshot ->
                    val rent = dataSnapshot.getValue(FirebaseRent::class.java)!!
                    rent.key = dataSnapshot.key!!
                    rent.takeIf { !it.isFinished }?.let { it1 -> borrowLendList.add(it1) }
                }
                lendBorrowBooks.value = borrowLendList
                isLendBorrowLoaded.value = true
            }
        })
    }
    fun setBooks(books: MutableList<FirebaseBook>){
        this.books.value = books
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
    fun getLendBorrowBooks(): MutableLiveData<MutableList<FirebaseRent>>{
        return lendBorrowBooks
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
    fun getMyBookLoaded(): MutableLiveData<Boolean>{
        return isMyBookLoaded
    }
    fun getWishListLoaded(): MutableLiveData<Boolean>{
        return isWishListLoaded
    }
    fun getLendBorrowLoaded(): MutableLiveData<Boolean>{
        return isLendBorrowLoaded
    }
    fun getBookCopy(): MutableList<FirebaseBook>{
        return booksCopy
    }
    fun setBookCopy(books: MutableList<FirebaseBook>){
        if(booksCopy.isNotEmpty()){
            booksCopy.clear()
        }
        booksCopy.addAll(books)
    }
}