package com.example.videointeractive.presentation.helpers

import android.content.Context
import android.net.Uri
import com.example.videointeractive.R
import java.lang.reflect.Field
import javax.inject.Inject

class ResourcesProvider @Inject constructor(private val context: Context) {

    fun getVideosUriList(): List<Uri> {
        val result = arrayListOf<Uri>()
        val fields: Array<Field> = R.raw::class.java.fields
        for (field in fields) {
            val path = BASE_PATH + "${context.packageName}/${field.getInt(field)}"
            result.add(Uri.parse(path))
        }
        return result
    }

    fun getStringResources(id: Int): String {
        return context.resources.getString(id)
    }

    companion object {
        private const val BASE_PATH = "android.resource://"
    }
}
