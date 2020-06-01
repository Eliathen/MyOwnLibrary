package com.szymanski.myownlibrary.fragments.dialogFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.activities.KeywordSearchResultActivity
import com.szymanski.myownlibrary.activities.SaveBookManuallyActivity
import kotlinx.android.synthetic.main.fragment_keyword_search.*
import kotlinx.android.synthetic.main.fragment_keyword_search.view.*

/**
 * A simple [Fragment] subclass.
 */
class KeywordSearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_keyword_search, container, false)

        rootView.searchByKeywords.setOnClickListener{
            val intent = Intent(activity, KeywordSearchResultActivity::class.java)
            intent.putExtra("phrase", keywordText.text.toString())
            startActivity(intent)
        }
        rootView.addBookManually.setOnClickListener {
            val intent = Intent(activity, SaveBookManuallyActivity::class.java)
            startActivity(intent)
        }

        return rootView
    }

}
