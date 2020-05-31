package com.szymanski.myownlibrary.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.adapters.LendBorrowAdapter
import com.szymanski.myownlibrary.data.firebase.models.FirebaseBook
import com.szymanski.myownlibrary.data.firebase.models.FirebaseRent
import com.szymanski.myownlibrary.viewModels.MainViewModel
import kotlinx.android.synthetic.main.fragment_lend_borrow.view.*

/**
 * A simple [Fragment] subclass.
 */
class LendBorrowFragment : Fragment(), LendBorrowAdapter.LendBorrowItemListeners {
    private lateinit var viewModel: MainViewModel
    private lateinit var lendBorrowAdapter: LendBorrowAdapter
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_lend_borrow, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        initRecyclerView(rootView)
        viewModel.getBorrowLendListFromDatabase()
        this.activity?.let {
            viewModel.getBorrowLendBooks().observe(it, Observer<List<FirebaseRent>> { rents ->
                lendBorrowAdapter.setRents(rents)
            })
        }
        return rootView
    }

    private fun initRecyclerView(rootView: View) {
        val recyclerView = rootView.lendBorrowBooks
        recyclerView.layoutManager = LinearLayoutManager(rootView.context)
        this.lendBorrowAdapter = LendBorrowAdapter(activity, this)
        recyclerView.adapter = lendBorrowAdapter
    }

    override fun markBookAsReturn(view: View?, position: Int) {
        viewModel.getBorrowLendBooks().value?.get(position)?.let { viewModel.markBookAsReturn(it) }
    }

}
