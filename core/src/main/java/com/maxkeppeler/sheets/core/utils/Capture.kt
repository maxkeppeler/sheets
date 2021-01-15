package com.maxkeppeler.sheets.core.utils

import android.app.Dialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.IOException
import java.io.OutputStream

object Capture {

    private const val IMAGE_FORMAT = ".png"
    private const val MIME_TYPE = "image/*"

    private const val DIR = "Sheets"

    fun sheet(ctx: Context, dialog: Dialog?, name: String) {

        val view = dialog?.window?.decorView
            ?: throw IllegalStateException("Can not capture view. View is null.")

        val resolver: ContentResolver = ctx.contentResolver
        val displayName = name.plus(IMAGE_FORMAT)
        val relativePath = Environment.DIRECTORY_PICTURES + File.separator + DIR

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
            put(MediaStore.MediaColumns.MIME_TYPE, MIME_TYPE)
            put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
        }

        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        var stream: OutputStream? = null
        var uri: Uri? = null

        try {
            val tableUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            uri = resolver.insert(tableUri, contentValues)
            if (uri == null) {
                throw IOException("Failed to create new MediaStore record.")
            }
            stream = resolver.openOutputStream(uri)
            if (stream == null) {
                throw IOException("Failed to get output stream.")
            }
            if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)) {
                throw IOException("Failed to save.")
            }
        } catch (e: IOException) {
            if (uri != null) {
                // Don't leave an orphan entry in the MediaStore
                resolver.delete(uri, null, null)
            }
            throw e
        } finally {
            stream?.close()
        }
    }
}