package com.szymanski.myownlibrary.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.szymanski.myownlibrary.R
import com.szymanski.myownlibrary.adapters.AuthorsAdapter
import com.szymanski.myownlibrary.converters.BookConverter
import com.szymanski.myownlibrary.converters.ImageConverter
import com.szymanski.myownlibrary.data.firebase.models.FirebaseBook
import com.szymanski.myownlibrary.viewModels.SaveBookManuallyViewModel
import kotlinx.android.synthetic.main.activity_save_book_manually.*
import java.io.ByteArrayOutputStream


class SaveBookManuallyActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var viewModel : SaveBookManuallyViewModel
    private lateinit var authorsAdapter: AuthorsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_book_manually)
        viewModel = ViewModelProvider(this).get(SaveBookManuallyViewModel::class.java)
        viewModel.getErrorMessage().observe(this, Observer {
            if(it == "Success"){
                finish()
            } else {
                displayErrorDialog(it)
            }
        })
        initRecyclerView()
        saveButton.setOnClickListener {

        }
        imageButton.setOnClickListener {
            if(checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA),1)
            } else {
                getPhoto();
            }
        }
        saveButton.setOnClickListener {
            attemptSaveBook()
        }


    }

    private fun attemptSaveBook() {
        var canBeSaved = true
        var focus = View(baseContext)
        if (bookTitle.text.toString().isEmpty()) {
            canBeSaved = false
            focus = bookTitle
            bookTitle.error = getString(R.string.required_field_message)
        }
        if (isbn.text.toString().isEmpty()) {
            canBeSaved = false
            focus = isbn
            isbn.error = getString(R.string.required_field_message)
        }
        if (authorsAdapter.getAuthors().first().isEmpty()) {
            focus = authorsList
            canBeSaved = false
        }
        if (pages.text.toString().isEmpty()) {
            canBeSaved = false
            focus = pages
            pages.error = getString(R.string.required_field_message)
        }
        if (pages.text.toString().isEmpty()) {
            canBeSaved = false
            focus = pages
            pages.error = getString(R.string.required_field_message)
        }
        if (canBeSaved) {
            saveBook()
        } else {
            focus.requestFocus()
        }
    }

    private fun saveBook() {
        val stream = ByteArrayOutputStream()
        var image = cover.drawToBitmap()
        var encodedImage = ""
        if (image.compress(Bitmap.CompressFormat.PNG, 100, stream)) {
            encodedImage = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
        }
        val book = FirebaseBook(
            isbn.text.toString(),
            bookTitle.text.toString(),
            ArrayList<String>(authorsAdapter.getAuthors()),
            publishedYear.text.toString(),
            pages.text.toString().toInt(),
            encodedImage
        )
        viewModel.setNewBook(book)
        viewModel.saveBook()
    }

    private fun initRecyclerView(){
        val recyclerView = authorsList
        authorsAdapter = AuthorsAdapter(this.baseContext)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            requestedOrientation
        )
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.adapter = authorsAdapter
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK && data != null){
            var photoUri = data.data

            val imageBitmap = data.extras?.get("data") as Bitmap
            cover.setImageBitmap(imageBitmap)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 1){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getPhoto()
            }
        }
    }

    private fun getPhoto(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val int = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also{
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }
    private fun displayErrorDialog(message: String){
        AlertDialog.Builder(this).apply{
            setTitle("Error")
            setMessage(message)
            setNeutralButton("OK") { _: DialogInterface, _: Int ->

            }
        }.create().show()
    }

}
