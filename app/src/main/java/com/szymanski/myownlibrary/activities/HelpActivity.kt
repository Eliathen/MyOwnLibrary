package com.szymanski.myownlibrary.activities

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle

import android.widget.TextView

import com.szymanski.myownlibrary.R

class HelpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        val textViews = ArrayList<TextView>().apply{
            add(findViewById(R.id.titleInstruction))
            add(findViewById(R.id.mainActivityInstruction))
            add(findViewById(R.id.appBarInstruction))
            add(findViewById(R.id.myBookInstruction))
            add(findViewById(R.id.wishListInstruction))
            add(findViewById(R.id.lendBorrowInstruction))
            add(findViewById(R.id.searchBookInstruction))
            add(findViewById(R.id.addBookManuallyInstruction))
        }

        resources.getStringArray(R.array.instructions_text).forEachIndexed { index, s ->
            textViews[index].text = s
        }

    }
}
