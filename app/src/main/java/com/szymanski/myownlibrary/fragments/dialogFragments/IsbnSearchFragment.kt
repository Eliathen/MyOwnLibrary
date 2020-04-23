package com.szymanski.myownlibrary.fragments.dialogFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.szymanski.myownlibrary.R
import kotlinx.android.synthetic.main.fragment_isbn_search.view.*

/**
 * A simple [Fragment] subclass.
 */
class IsbnSearchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_isbn_search, container, false)
        view.searchWitIsbnButton.setOnClickListener {
            saveBook(view.isbnNumberText.text.toString())
        }
        return view
    }

    private fun saveBook(isbn: String){
        //TODO find and save book implementation
        displaySnackBar("Dodano")
        activity?.supportFragmentManager?.popBackStack()
    }

    private fun displaySnackBar(message: String){
        activity?.findViewById<ViewGroup>(android.R.id.content)?.getChildAt(0)?.let {
            Snackbar.make(it,message,Snackbar.LENGTH_LONG).apply {
                this.animationMode = Snackbar.ANIMATION_MODE_FADE
            }.show()
        }
    }

}

