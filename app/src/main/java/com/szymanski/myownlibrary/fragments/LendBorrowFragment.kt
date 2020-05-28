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
import com.szymanski.myownlibrary.data.openLibraryAPI.models.Book
import com.szymanski.myownlibrary.data.firebase.Rent
import com.szymanski.myownlibrary.viewModels.MainViewModel
import kotlinx.android.synthetic.main.fragment_lend_borrow.view.*

/**
 * A simple [Fragment] subclass.
 */
class LendBorrowFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var lendBorrowAdapter: LendBorrowAdapter
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_lend_borrow, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        viewModel.loadExampleBorrowBook()
        initRecyclerView(rootView)

        this.activity?.let {
            viewModel.getBorrowLendBooks().observe(it, Observer<List<Rent>> { rents ->
                initRecyclerView(rootView)
            })
        }
        return rootView
    }

    private fun initRecyclerView(rootView: View) {

        val books = arrayListOf<Rent>()
            val rent = Rent(
                Book(
                    "9780641723445",
                    "The lightning thief",
                    arrayListOf("Rick Riordan"),
                    "2005",
                    377,
                    "https://covers.openlibrary.org/b/id/7989100-M.jpg"
                ),
                "startDate", "01/01/2020", "John Jones"
            )
            repeat(10){
                books.add(rent)
            }
        val recyclerView = rootView.lendBorrowBooks
        recyclerView.layoutManager = LinearLayoutManager(rootView.context)
        this.lendBorrowAdapter = LendBorrowAdapter(activity)
        lendBorrowAdapter.setRents(books)
        recyclerView.adapter = lendBorrowAdapter
    }

}
