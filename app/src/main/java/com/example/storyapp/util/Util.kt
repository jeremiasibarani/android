package com.example.storyapp.util

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

fun isEmailValid(email : String) : Boolean{
    val emailPattern = "[a-zA-Z\\d._-]+@[a-z]+\\.+[a-z]+".toRegex()
    return email.matches(emailPattern)
}

fun createTempImageFile(context: Context) : File{
    val timeStamp : String = SimpleDateFormat("MM-DD-yyyy").format(Date())
    val storageDir : File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "JPEG_${timeStamp}",
        ".jpg",
        storageDir
    )
}

fun uriToFile(selectedImg : Uri, context: Context) : File{
    val contentResolver : ContentResolver = context.contentResolver
    val myFile = createTempImageFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream : OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len : Int
    while(inputStream.read(buf).also{ len = it } > 0){
        outputStream.write(buf, 0, len)
    }
    outputStream.close()
    inputStream.close()

    return myFile
}

fun reduceFileImage(file : File) : File{
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > 1000000)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}