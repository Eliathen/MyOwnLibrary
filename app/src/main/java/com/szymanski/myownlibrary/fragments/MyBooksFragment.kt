package com.szymanski.myownlibrary.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log

import androidx.fragment.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu

import android.widget.Toast

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.DividerItemDecoration

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.activities.BookDetailsActivity
import com.szymanski.myownlibrary.activities.SaveBookManuallyActivity
import com.szymanski.myownlibrary.adapters.MyBookAdapter
import com.szymanski.myownlibrary.data.firebase.models.FirebaseBook
import com.szymanski.myownlibrary.fragments.dialogFragments.SaveBookDialogFragment
import com.szymanski.myownlibrary.viewModels.MainViewModel
import kotlinx.android.synthetic.main.fragment_my_books.*

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
        viewModel.getBookListFromDatabase()
        this.activity?.let { fragmentActivity ->
            viewModel.getBooks().observe(fragmentActivity, Observer<List<FirebaseBook>> { books->
                myBooksAdapter.setBooks(books)
            })
            viewModel.getMyBookLoaded().observe(fragmentActivity, Observer{
                if(it){
                    rootView.myBookProgressBar.visibility = View.GONE
                } else {
                    rootView.myBookProgressBar.visibility = View.VISIBLE
                }
            })
        }

        return rootView
    }

    private fun initRecyclerView(rootView: View) {
        val recyclerView = rootView.myBooks
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL)

        recyclerView.addItemDecoration(dividerItemDecoration)
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
                    viewModel.getBooks().value?.get(position)?.let{book ->
                        displayEditActivity(book)
                    }
                }
                R.id.removeItem -> {
                    viewModel.getBooks().value?.get(position)?.let { book -> attemptRemoveBook(book) }
                }
            }
            true
        }
        popupMenu.show()
    }
    private fun attemptRemoveBook(firebaseBook: FirebaseBook){
        AlertDialog.Builder(context).apply{
            setTitle("\"${firebaseBook.title}\"")
            setMessage("Are you sure you want to remove this book? This option is irreversible!")
            setPositiveButton("Remove"){ _, _ ->
                displayResultSnackBar(viewModel.removeBook(firebaseBook))
                myBooksAdapter.setBooks(viewModel.getBooks().value!!.toList())
            }
            setNegativeButton("Cancel"){ _, _ ->

            }
        }.create().show()
    }
    private fun displayResultSnackBar(message: String){
        Snackbar.make(myBooksContainer,message, Snackbar.LENGTH_SHORT).show()
    }
    private fun displayEditActivity(book: FirebaseBook){
        val intent = Intent(activity, SaveBookManuallyActivity::class.java)
        intent.putExtra("editBook", book)
        startActivity(intent)
    }

}
