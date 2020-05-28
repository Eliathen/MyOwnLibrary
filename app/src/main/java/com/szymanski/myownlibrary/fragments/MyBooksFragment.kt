package com.szymanski.myownlibrary.fragments

import android.os.Bundle

import androidx.fragment.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu

import android.widget.Toast

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

import androidx.recyclerview.widget.LinearLayoutManager

import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.adapters.MyBookAdapter
import com.szymanski.myownlibrary.data.openLibraryAPI.models.Book
import com.szymanski.myownlibrary.fragments.dialogFragments.SaveBookDialogFragment
import com.szymanski.myownlibrary.viewModels.MainViewModel

import kotlinx.android.synthetic.main.fragment_my_books.view.*
import kotlinx.android.synthetic.main.fragment_my_books.view.myBooks

/**
 * A simple [Fragment] subclass.
 */
class MyBooksFragment : Fragment(), ViewModelStoreOwner, MyBookAdapter.OnBookItemListener {
    private lateinit var viewModel: MainViewModel
    private lateinit var myBooksAdapter: MyBookAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_my_books, container, false)

        rootView.addBookButton?.setOnClickListener {
            showDialog()
        }
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        initRecyclerView(rootView)
        this.activity?.let {
            viewModel.getBooks().observe(it, Observer<List<Book>> {books->
                myBooksAdapter.setBooks(books)
            })
        }

        return rootView
    }

    private fun initRecyclerView(rootView: View) {
        val recyclerView = rootView.myBooks
        recyclerView.layoutManager = LinearLayoutManager(activity)
        this.myBooksAdapter = MyBookAdapter(activity, this)
        viewModel.getBooks().value?.let { myBooksAdapter.setBooks(it) }
        recyclerView.adapter = myBooksAdapter
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

    override fun onClick(view: View, position: Int) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.my_book_item_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.editItem -> {
                    Toast.makeText(activity?.baseContext, "Edit", Toast.LENGTH_SHORT).show()
                }
                R.id.removeItem -> {
                    Toast.makeText(activity?.baseContext, "Remove", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        popupMenu.show()
    }
}
