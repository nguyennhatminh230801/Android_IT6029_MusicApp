package com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.Exam
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.questions.QuestionItemResponse
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.StudyCategory
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.WrongAnswer
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.roomtypeconverter.ExamTypeConverter
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.roomtypeconverter.QuestionOptionTypeConverter
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.roomtypeconverter.QuestionTypeConverter
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.roomtypeconverter.WrongAnswerObjectTypeConverter
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.local.MotorbikeAppDatabase.Companion.ALLOW_EXPORT_SCHEMA
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.local.MotorbikeAppDatabase.Companion.DATABASE_VERSION
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.local.exam.ExamDao
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.local.question.QuestionsDao
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.local.study.StudyDao
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.local.wronganswer.WrongAnswerDao

@Database(
    entities = [
        Exam::class,
        QuestionItemResponse::class,
        StudyCategory::class,
        WrongAnswer::class,
    ],
    version = DATABASE_VERSION,
    exportSchema = ALLOW_EXPORT_SCHEMA,
)
@TypeConverters(
    ExamTypeConverter::class,
    QuestionTypeConverter::class,
    QuestionOptionTypeConverter::class,
    WrongAnswerObjectTypeConverter::class,
)
abstract class MotorbikeAppDatabase : RoomDatabase() {
    abstract fun getExamDao(): ExamDao
    abstract fun getQuestionDao(): QuestionsDao
    abstract fun getStudyDao(): StudyDao
    abstract fun getWrongAnswerDao(): WrongAnswerDao

    companion object {
        const val DATABASE_NAME = "MotorbikeAppDatabase"
        const val DATABASE_VERSION = 1
        const val ALLOW_EXPORT_SCHEMA = false
    }
}
