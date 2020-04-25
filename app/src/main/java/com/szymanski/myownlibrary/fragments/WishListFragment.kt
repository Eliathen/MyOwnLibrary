package com.szymanski.myownlibrary.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.adapters.MyBookAdapter
import com.szymanski.myownlibrary.data.models.Book
import com.szymanski.myownlibrary.viewModels.BookViewModel
import kotlinx.android.synthetic.main.fragment_lend_borrow.view.*
import kotlinx.android.synthetic.main.fragment_wish_list.view.*

/**
 * A simple [Fragment] subclass.
 */
class WishListFragment : Fragment() {

    private lateinit var viewModel: BookViewModel
    private lateinit var myBooksAdapter: MyBookAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_wish_list, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(BookViewModel::class.java)
        initRecyclerView(rootView)
        this.activity?.let {
            viewModel.getWishList().observe(it, Observer<List<Book>> {
                myBooksAdapter.notifyDataSetChanged()
            })
        }

        return rootView
    }

    private fun initRecyclerView(rootView: View) {
        val recyclerView = rootView.wishListBooks
        recyclerView.layoutManager = LinearLayoutManager(activity)
        this.myBooksAdapter = MyBookAdapter(activity)
        viewModel.getWishList().value?.let { myBooksAdapter.setBooks(it) }
        recyclerView.adapter = myBooksAdapter
    }

}
