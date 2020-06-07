package com.szymanski.myownlibrary.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.adapters.WishListAdapter
import com.szymanski.myownlibrary.data.firebase.models.FirebaseBook
import com.szymanski.myownlibrary.viewModels.MainViewModel
import kotlinx.android.synthetic.main.fragment_wish_list.*
import kotlinx.android.synthetic.main.fragment_wish_list.view.*
import kotlinx.android.synthetic.main.fragment_wish_list.view.wishListContainer

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
            viewModel.getWishListLoaded().observe(it, Observer{isLoaded ->
                if(isLoaded){
                    rootView.wishListProgressBar.visibility = View.GONE
                } else {
                    rootView.wishListProgressBar.visibility = View.VISIBLE
                }
            })
        }
        viewModel.getWishListFromDatabase()
        return rootView
    }

    private fun initRecyclerView(rootView: View) {
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
                    var message = viewModel.removeBookFromWishList(viewModel.getWishList().value?.get(position)!!)
                    displaySnackBar(message)
                }
            }
            true
        }
        popupMenu.show()
    }
    private fun displaySnackBar(message:String){
        Snackbar.make(this.wishListContainer, message, Snackbar.LENGTH_SHORT).show()
    }
}
