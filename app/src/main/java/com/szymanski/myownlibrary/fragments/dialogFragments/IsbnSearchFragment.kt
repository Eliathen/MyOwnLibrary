package com.szymanski.myownlibrary.fragments.dialogFragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.activities.SaveBookManuallyActivity
import com.szymanski.myownlibrary.viewModels.MainViewModel
import kotlinx.android.synthetic.main.fragment_isbn_search.view.*
import kotlinx.android.synthetic.main.fragment_isbn_search.view.addManualButton

/**
 * A simple [Fragment] subclass.
 */
class IsbnSearchFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    private var bookSavedObserver: BookSavedObserver = BookSavedObserver()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_isbn_search, container, false)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        mainViewModel.setIsBookSaved(false)
        view.searchWitIsbnButton.setOnClickListener {
            saveBook(view.isbnNumberText.text.toString())
        }

        observeError()
        observeIsBookSaved()
        view.addManualButton.setOnClickListener {
            val intent = Intent(activity, SaveBookManuallyActivity::class.java)
            startActivity(intent)
        }
        return view
    }
    private fun observeIsBookSaved(){
        mainViewModel.getIsBookSaved().observe(viewLifecycleOwner, bookSavedObserver)
    }
    private fun observeError() {
        mainViewModel.getError().observe(viewLifecycleOwner, Observer {
            it.message?.let { message ->
                displaySnackBar(message)
            }
        })
    }
    private fun saveBook(isbn: String) {
        mainViewModel.searchBookByIsbn(isbn)
    }

    private fun displaySnackBar(message: String){
        activity?.findViewById<ViewGroup>(android.R.id.content)?.getChildAt(0)?.let {
            Snackbar.make(it,message,Snackbar.LENGTH_LONG).apply {
                this.animationMode = Snackbar.ANIMATION_MODE_FADE
            }.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //TODO("Remove Observer")
        Log.d("ISBN", "DestroyView")
        mainViewModel.getIsBookSaved().removeObserver {
            bookSavedObserver
        }
    }
    inner class BookSavedObserver: Observer<Boolean>{
        override fun onChanged(t: Boolean) {
            if (t) {
                    displaySnackBar(getString(R.string.book_add_correct))
                    activity?.supportFragmentManager?.popBackStack()
            }
        }

    }

}

