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
import androidx.recyclerview.widget.LinearLayoutManager
import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.adapters.WishListAdapter
import com.szymanski.myownlibrary.data.openLibraryAPI.models.Book
import com.szymanski.myownlibrary.viewModels.MainViewModel
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
            viewModel.getWishList().observe(it, Observer { list ->
                wishListAdapter.setBooks(list)
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
        val book1 = Book(
            "9781857230765",
            "The Eye of the World (Wheel of Time)",
            arrayListOf("Robert Jordan"),
            "1992",
            377,
            "https://covers.openlibrary.org/b/id/908780-M.jpg"
        )
            books.add(book)
            books.add(book1)
        viewModel.setWishList(books)
        val recyclerView = rootView.wishListBooks
        recyclerView.layoutManager = LinearLayoutManager(rootView.context)
        viewModel.getWishList().value?.let { wishListAdapter.setBooks(it) }
        recyclerView.adapter = wishListAdapter
    }

    override fun onClick(view: View, position: Int) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.wish_item_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.saveItem -> {
                    viewModel.markBookFromWishListAsOwn(viewModel.getWishList().value?.get(position)!!)
                }
                R.id.removeItem -> {
                    viewModel.removeBookFromWishList(viewModel.getWishList().value?.get(position)!!)
                }
            }
            true
        }
        popupMenu.show()
    }

}
