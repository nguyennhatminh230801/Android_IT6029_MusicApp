package com.nguyennhatminh614.motobikedriverlicenseapp.data.model.roomtypeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.ExamHistory
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.questions.QuestionItemResponse

class ExamTypeConverter {
    @TypeConverter
    fun convertJsonToListQuestion(json: String): MutableList<QuestionItemResponse?> {
        val typeToken = object : TypeToken<MutableList<QuestionItemResponse>>() {}.type
        return Gson().fromJson(json, typeToken)
    }

    @TypeConverter
    fun convertListQuestionToJson(listQuestions: MutableList<QuestionItemResponse>): String? =
        Gson().toJson(listQuestions)

    @TypeConverter
    fun convertJsonToListExamHistory(json: String): MutableList<ExamHistory?> {
        val typeToken = object : TypeToken<MutableList<ExamHistory>>() {}.type
        return Gson().fromJson(json, typeToken)
    }

    @TypeConverter
    fun convertListExamHistoryToJson(listQuestions: MutableList<ExamHistory>): String? =
        Gson().toJson(listQuestions)
}
