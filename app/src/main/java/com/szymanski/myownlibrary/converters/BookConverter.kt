package com.szymanski.myownlibrary.converters

import com.szymanski.myownlibrary.data.firebase.models.FirebaseBook
import com.szymanski.myownlibrary.data.openLibraryAPI.models.Book

class BookConverter {

    fun toFirebaseBook(book: Book, encodedImage: String): FirebaseBook {
        return FirebaseBook(
            book.isbn,
            book.title,
            book.authors.toList(),
            book.publishedYear,
            book.pageCount,
            encodedImage
        )
    }
}