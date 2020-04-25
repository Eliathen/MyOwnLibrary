package com.szymanski.myownlibrary.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.data.models.Book

class BookViewModel: ViewModel() {
    private val books = MutableLiveData<MutableList<Book>>()
    private val wishList = MutableLiveData<MutableList<Book>>()
    fun loadExampleData(){
        books.value = mutableListOf<Book>().apply {
            val authors = ArrayList<String>(1)
            authors.add("Henryk Sienkiewicz")
            this.add(Book("Ogniem i mieczem",authors, "2004", ""))
            this.add(Book("Potop",authors, "1999", "http://via.placeholder.com/640x360"))
            this.add(Book("Ogniem i mieczem",authors, "2004", ""))
            this.add(Book("Potop",authors, "1999", ""))
            this.add(Book("Ogniem i mieczem",authors, "2004", ""))
            this.add(Book("Potop",authors, "1999", ""))
            this.add(Book("Ogniem i mieczem",authors, "2004", ""))
            this.add(Book("Potop",authors, "1999", ""))
            this.add(Book("Ogniem i mieczem",authors, "2004", ""))
            this.add(Book("Potop",authors, "1999", ""))
        }
        wishList.value = mutableListOf<Book>().apply{
            val authors = ArrayList<String>(1).apply{
                add("J.R.R. Tolkien")
            }
            this.add(Book("Władca Pierścieni: Drużyna Pierścienia",authors, "2009", ""))
            this.add(Book("Władca Pierścieni: Dwie Wieże",authors, "2011", ""))
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
}