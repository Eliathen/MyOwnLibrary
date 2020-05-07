package com.szymanski.myownlibrary.fragments

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.adapters.SearchResultAdapter
import com.szymanski.myownlibrary.adapters.WishListAdapter
import com.szymanski.myownlibrary.data.models.Book
import com.szymanski.myownlibrary.data.models.Rent
import com.szymanski.myownlibrary.viewModels.MainViewModel
import kotlinx.android.synthetic.main.fragment_wish_list.*
import kotlinx.android.synthetic.main.fragment_wish_list.view.*
import kotlinx.android.synthetic.main.fragment_wish_list.view.wishListBooks

/**
 * A simple [Fragment] subclass.
 */
class WishListFragment : Fragment(), WishListAdapter.ClickListener {

    private lateinit var viewModel: MainViewModel
    private var wishListAdapter = WishListAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_wish_list, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        initRecyclerView(rootView)
        this.activity?.let {
            viewModel.getWishList().observe(it, Observer<List<Book>> {

            })
        }

        return rootView
    }

    private fun initRecyclerView(rootView: View) {
        val books = arrayListOf<Book>()
        val book = Book("9780641723445",
            "The lightning thief",
            arrayListOf("Rick Riordan"),
            "2005",
            377,
            "https://covers.openlibrary.org/b/id/7989100-M.jpg")
        repeat(10){
            books.add(book)
        }
        val recyclerView = rootView.wishListBooks
        recyclerView.layoutManager = LinearLayoutManager(rootView.context)
        wishListAdapter.setBooks(books)
        recyclerView.adapter = wishListAdapter
    }

    override fun onClick(view: View, position: Int) {
        Log.d("WishListFragment", "DisplayMenu")
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.wish_item_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.saveItem -> {
                    Toast.makeText(activity?.baseContext, "Save", Toast.LENGTH_SHORT).show()
                }
                R.id.removeItem -> {
                    Toast.makeText(activity?.baseContext, "Save", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        popupMenu.show()
    }

}
