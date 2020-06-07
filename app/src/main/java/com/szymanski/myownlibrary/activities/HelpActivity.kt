package com.szymanski.myownlibrary.activities

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter

import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.adapters.InstructionContentsAdapter
import kotlinx.android.synthetic.main.activity_help.*

class HelpActivity : AppCompatActivity(), InstructionContentsAdapter.InstructionListeners {

    private lateinit var content: Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        val contents = resources.getStringArray(R.array.instruction_contents)
        val title = resources.getString(R.string.instruction_title)
        val subtitle = resources.getString(R.string.instruction_subtitle)
        content = resources.getStringArray(R.array.instructions_text)

        titleInstruction.text = title
        subtitleInstruction.text = subtitle
        initRecyclerView(contents)
    }

    private fun initRecyclerView(
        contents: Array<out String>
    ) {
        val arrayContents = ArrayList<String>()
        contents.forEach {
            arrayContents.add(it)
        }
        val adapter = InstructionContentsAdapter(this)
        adapter.setInstruction(arrayContents)
        instructionContents.layoutManager = LinearLayoutManager(baseContext)
        instructionContents.addItemDecoration(
            DividerItemDecoration(
                instructionContents.context,
                requestedOrientation
            )
        )
        instructionContents.adapter = adapter
    }

    override fun onItemClick(position: Int) {
        AlertDialog.Builder(this).apply {
            this.setTitle("Instruction")
            this.setMessage(content[position])
            this.setPositiveButton("Close", { _: DialogInterface, _: Int ->
            })
        }.create().show()
    }
}
