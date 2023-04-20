package com.nguyennhatminh614.motobikedriverlicenseapp.data.model.roomtypeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.NewQuestion

class ExamTypeConverter {
    @TypeConverter
    fun convertJsonToListQuestion(json: String): MutableList<NewQuestion?> {
        val typeToken = object : TypeToken<MutableList<NewQuestion>>() {}.type
        return Gson().fromJson(json, typeToken)
    }

    @TypeConverter
    fun convertListQuestionToJson(listQuestions: MutableList<NewQuestion>): String? =
        Gson().toJson(listQuestions)
}
