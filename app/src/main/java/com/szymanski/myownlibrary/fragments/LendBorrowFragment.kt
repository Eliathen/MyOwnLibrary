package com.szymanski.myownlibrary.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.adapters.LendBorrowAdapter
import com.szymanski.myownlibrary.data.firebase.models.FirebaseRent
import com.szymanski.myownlibrary.viewModels.MainViewModel
import kotlinx.android.synthetic.main.fragment_lend_borrow.view.*

/**
 * A simple [Fragment] subclass.
 */
class LendBorrowFragment : Fragment(), LendBorrowAdapter.LendBorrowItemListeners {

    private lateinit var viewModel: MainViewModel
    private lateinit var lendBorrowAdapter: LendBorrowAdapter
    private lateinit var rootView: View

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_lend_borrow, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        initRecyclerView(rootView)
        viewModel.getLendBorrowListFromDatabase()
        initDataValue()

        return rootView
    }

    private fun initDataValue() {
        this.activity?.let {
            viewModel.getLendBorrowBooks().observe(it, Observer<List<FirebaseRent>> { rents ->
                if(rents.isEmpty()){
                    rootView.lendBorrowBooks.visibility = View.GONE
                    rootView.lendBorrowProgressBar.visibility = View.GONE
                } else {
                    lendBorrowAdapter.setRents(rents)
                    rootView.lendBorrowBooks.visibility = View.VISIBLE
                }
            })
            viewModel.getLendBorrowLoaded().observe(it, Observer { isLoaded ->
                if (isLoaded) {
                    rootView.lendBorrowProgressBar.visibility = View.GONE
                } else {
                    rootView.lendBorrowProgressBar.visibility = View.VISIBLE
                }
            })
        }
    }

    private fun initRecyclerView(rootView: View) {
        val recyclerView = rootView.lendBorrowBooks
        recyclerView.layoutManager = LinearLayoutManager(rootView.context)
        this.lendBorrowAdapter = LendBorrowAdapter(activity, this)
        recyclerView.adapter = lendBorrowAdapter
    }

    override fun markBookAsReturn(view: View?, position: Int) {
        val result = viewModel.getLendBorrowBooks()
            .value?.get(position)?.let { viewModel.markBookAsReturn(it, position) }
            if (result != null) {
                displayResultToastMessage(result)
            }
    }
    private fun displayResultToastMessage(message: String){
        Toast.makeText(rootView.context, message, Toast.LENGTH_SHORT).show()
    }

}
