package com.szymanski.myownlibrary.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.szymanski.myownlibrary.data.models.Book

class BookViewModel: ViewModel() {
    val books = MutableLiveData<MutableList<Book>>()


    fun loadExampleData(){
        books.value = mutableListOf<Book>().apply {
            val authors = ArrayList<String>(1)
            authors.add("Henryk Sienkiewicz")
            this.add(Book("Ogniem i mieczem",authors, "2004"))
            this.add(Book("Potop",authors, "1999"))
            this.add(Book("Ogniem i mieczem",authors, "2004"))
            this.add(Book("Potop",authors, "1999"))
            this.add(Book("Ogniem i mieczem",authors, "2004"))
            this.add(Book("Potop",authors, "1999"))
            this.add(Book("Ogniem i mieczem",authors, "2004"))
            this.add(Book("Potop",authors, "1999"))
            this.add(Book("Ogniem i mieczem",authors, "2004"))
            this.add(Book("Potop",authors, "1999"))

        }
    }
    fun selectedBook(book: Book){
        books.value?.add(book)
    }
}