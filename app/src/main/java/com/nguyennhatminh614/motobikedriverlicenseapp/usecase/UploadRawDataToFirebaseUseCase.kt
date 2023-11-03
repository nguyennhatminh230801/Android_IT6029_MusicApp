package com.nguyennhatminh614.motobikedriverlicenseapp.usecase

import android.content.Context
import androidx.annotation.RawRes
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UploadRawDataToFirebaseUseCase(
    private val firebaseFirestore: FirebaseFirestore,
    private val context: Context,
) {
    fun <T : Any> loadDataToFireBase(@RawRes rawData: Int, type: T) {
        val fireStore = firebaseFirestore.collection("question_new_ver")

        val jsonString = context.resources.openRawResource(rawData).bufferedReader().use {
            it.readLines().joinToString(separator = "") { it.trim() }
        }

        val typeToken = object : TypeToken<List<T>>() {}.type
        Gson().fromJson<List<T>>(jsonString, typeToken).forEachIndexed { index, item ->
            fireStore.document(index.toString()).set(item)
        }
    }
}