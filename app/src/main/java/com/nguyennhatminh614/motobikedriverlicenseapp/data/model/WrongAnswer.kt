package com.nguyennhatminh614.motobikedriverlicenseapp.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.WrongAnswer.Companion.WRONG_ANSWER_TABLE
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
@Entity(tableName = WRONG_ANSWER_TABLE)
data class WrongAnswer(
    @PrimaryKey val questionsID: Int,
    val lastWrongTime: Long,
    val lastSelectedState: QuestionOptions,
) : Parcelable {
    companion object {
        const val WRONG_ANSWER_TABLE = "WRONG_ANSWER_TABLE"
    }
}
