package com.szymanski.myownlibrary.converters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log


class ImageConverter {

    companion object{
        fun base64ToBitmap(image: String?): Bitmap {
            val decodeByte = Base64.decode(image, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.size)
        }
    }
}