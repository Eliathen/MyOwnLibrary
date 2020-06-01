package com.szymanski.myownlibrary.data.firebase.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.szymanski.myownlibrary.data.firebase.models.FirebaseBook
import com.szymanski.myownlibrary.data.firebase.models.FirebaseRent

class FirebaseServiceImpl:
    FirebaseService {

    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun saveMyBook(book: FirebaseBook): String {
        var result = "Success"
        getMyBookReference()
            .child(book.isbn)
            .setValue(book) { databaseError, databaseReference ->
                if(databaseError != null){
                    result = databaseError.message
                }
            }
        return result
    }

    override fun getMyBookReference(): DatabaseReference {
        return database.reference
            .child("users")
            .child(auth.uid.toString())
            .child("myBooks")
    }

    override fun getBorrowedBookReference(): DatabaseReference {
       return database.reference
            .child("users")
            .child(auth.uid.toString())
            .child("borrowedLent")
    }
    override fun removeBook(book: FirebaseBook): String {
        var result = "Success"
        getMyBookReference().child(book.isbn).removeValue{
                databaseError: DatabaseError?,
                _: DatabaseReference ->
            if(databaseError != null){
                result = databaseError.message
            } else {
            }
        }
        return result
    }
    override fun markBookAsBorrowed(rent: FirebaseRent): String {
        var result = "Success"
        getBorrowedBookReference().push().setValue(rent) {
            databaseError, _ ->
            if(databaseError != null){
                result = databaseError.message
            } else {
                getMyBookReference().child(rent.firebaseBook.isbn).child("borrow").setValue(true)
            }
        }
        return result
    }

    override fun markBookAsLent(rent: FirebaseRent): String {
        var result = "Success"
        getBorrowedBookReference().push().setValue(rent) {
                databaseError, _ ->
            if(databaseError != null){
                result = databaseError.message
            } else {
                getMyBookReference().child(rent.firebaseBook.isbn).child("borrow").setValue(true)
            }
        }
        return result
    }

}