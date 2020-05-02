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
import com.szymanski.myownlibrary.adapters.SearchResultAdapter
import com.szymanski.myownlibrary.data.models.Book
import com.szymanski.myownlibrary.viewModels.MainViewModel
import kotlinx.android.synthetic.main.fragment_wish_list.view.*

/**
 * A simple [Fragment] subclass.
 */
class WishListFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var searchResultAdapter: SearchResultAdapter

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

    }

}
