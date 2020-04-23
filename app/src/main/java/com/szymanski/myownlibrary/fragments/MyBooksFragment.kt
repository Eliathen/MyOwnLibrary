package com.szymanski.myownlibrary.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.adapters.MyBookAdapter
import com.szymanski.myownlibrary.fragments.dialogFragments.SaveBookDialogFragment
import com.szymanski.myownlibrary.viewModels.BookViewModel
import kotlinx.android.synthetic.main.fragment_my_books.view.*
import kotlinx.android.synthetic.main.fragment_my_books.view.myBooks

/**
 * A simple [Fragment] subclass.
 */
class MyBooksFragment : Fragment() {
    private lateinit var viewModel: BookViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_my_books, container, true)

        rootView.addBookButton?.setOnClickListener {
            showDialog()
        }
        viewModel = ViewModelProvider(requireActivity()).get(BookViewModel::class.java)

        val recyclerView = rootView.myBooks
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val myBooksAdapter = MyBookAdapter()
        viewModel.books.value?.let { myBooksAdapter.setBooks(it) }

        recyclerView.adapter = myBooksAdapter

                return rootView
    }


    private fun showDialog() {
        val fragmentManager = activity?.supportFragmentManager?.beginTransaction()
        val prev = activity?.supportFragmentManager?.findFragmentByTag("dialog")
        if (prev != null) {
            fragmentManager?.remove(prev)
        }
        fragmentManager?.addToBackStack(null)

        val dialogFragment = SaveBookDialogFragment()
        fragmentManager?.let { dialogFragment.show(it, "dialog") }
    }

}
