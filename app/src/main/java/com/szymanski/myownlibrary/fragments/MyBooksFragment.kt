package com.szymanski.myownlibrary.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.fragments.dialogFragments.SaveBookDialogFragment
import kotlinx.android.synthetic.main.fragment_my_books.view.*

/**
 * A simple [Fragment] subclass.
 */
class MyBooksFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_my_books, container, true)
        rootView.addBookButton.setOnClickListener {
            showDialog()
        }
        return rootView
    }

    private fun showDialog() {
        val fragmentManager = activity?.supportFragmentManager?.beginTransaction()
        val prev = activity?.supportFragmentManager?.findFragmentByTag("dialog")
        if (prev != null) {
            fragmentManager?.remove(prev)
        }
        fragmentManager?.addToBackStack(null)

        val dialogFragment = SaveBookDialogFragment()
        fragmentManager?.let { dialogFragment.show(it, "dialog") }
    }
}
