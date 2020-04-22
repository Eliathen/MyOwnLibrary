package com.szymanski.myownlibrary.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.szymanski.myownlibrary.R
import kotlinx.android.synthetic.main.fragment_my_books.view.*

/**
 * A simple [Fragment] subclass.
 */
class MyBooksFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_my_books, container, false)
        rootView.addBookButton.setOnClickListener{
            Toast.makeText(activity, "Save book", Toast.LENGTH_SHORT).show()
        }
        return rootView
    }

}
