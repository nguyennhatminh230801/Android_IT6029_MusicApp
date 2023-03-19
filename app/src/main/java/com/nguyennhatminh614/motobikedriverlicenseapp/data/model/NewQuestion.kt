package com.nguyennhatminh614.motobikedriverlicenseapp.data.model

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import com.google.firebase.firestore.PropertyName
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = Questions.TABLE_QUESTION)
class NewQuestion {
    var id = Questions.DEFAULT_ID
    var question = ""
    var listOption = mutableListOf<String>()
    var correctAnswerPosition: Int = 1
    var isImmediateFailedTestWhenWrong: Boolean = false

    @set:PropertyName("image")
    @get:PropertyName("image")
    @SerializedName("image")
    var image = ""

    var hasImageBanner = false
    var explain = ""

    @set:PropertyName("question_type")
    @get:PropertyName("question_type")
    @SerializedName("question_type")
    var questionType = ""

    var minimumLicenseType = ""

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NewQuestion

        if (id != other.id) return false
        if (question != other.question) return false
        if (listOption != other.listOption) return false
        if (correctAnswerPosition != other.correctAnswerPosition) return false
        if (isImmediateFailedTestWhenWrong != other.isImmediateFailedTestWhenWrong) return false
        if (image != other.image) return false
        if (hasImageBanner != other.hasImageBanner) return false
        if (explain != other.explain) return false
        if (questionType != other.questionType) return false
        if (minimumLicenseType != other.minimumLicenseType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + question.hashCode()
        result = 31 * result + listOption.hashCode()
        result = 31 * result + correctAnswerPosition
        result = 31 * result + isImmediateFailedTestWhenWrong.hashCode()
        result = 31 * result + hasImageBanner.hashCode()
        result = 31 * result + explain.hashCode()
        result = 31 * result + minimumLicenseType.hashCode()
        return result
    }

    override fun toString(): String {
        return "NewQuestion(id=$id, question='$question', listOption=$listOption, correctAnswerPosition=$correctAnswerPosition, isImmediateFailedTestWhenWrong=$isImmediateFailedTestWhenWrong, image='$image', hasImageBanner=$hasImageBanner, explain='$explain', questionType='$questionType', minimumLicenseType='$minimumLicenseType')"
    }

    companion object {
        const val TABLE_QUESTION = "QUESTIONS"
        const val DEFAULT_ID = 1
        fun getDiffCallBack() = object : DiffUtil.ItemCallback<NewQuestion>() {
            override fun areItemsTheSame(
                oldItem: NewQuestion,
                newItem: NewQuestion,
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: NewQuestion,
                newItem: NewQuestion,
            ): Boolean = oldItem == newItem
        }
    }
}