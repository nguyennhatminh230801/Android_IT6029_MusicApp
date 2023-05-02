package com.nguyennhatminh614.motobikedriverlicenseapp.screen.exam.infodialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import com.nguyennhatminh614.motobikedriverlicenseapp.R
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.CreateExamRules
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.DialogExamInfoBinding


object ExamInfoDialog {
    private var dialog: AlertDialog? = null

    fun showExamInfoDialog(context: Context?, createExamRules: CreateExamRules) {
        context?.let { context ->
            if (dialog == null) {

                val windowLayoutParams = WindowManager.LayoutParams()
                windowLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
                windowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT

                val dialogBinding = DialogExamInfoBinding.inflate(
                    LayoutInflater.from(context),
                    null,
                    false
                )

                dialogBinding.apply {
                    textNumbersOfQuestion.text = context.resources.getString(R.string.text_numbers_of_question_exam_info, createExamRules.totalNumberOfQuestion)
                    textExamDuration.text = context.resources.getString(R.string.text_exam_info_duration, createExamRules.examDurationByMinutes)
                    textMinimumQuestionToPassExam.text = context.resources.getString(R.string.text_minimum_question_to_pass_exam_info, createExamRules.minimumCorrectToPassExam)
                    textTitleExamInfo.text = context.resources.getString(R.string.text_title_exam_info, createExamRules.licenseType.type)
                }

                dialog = AlertDialog.Builder(context)
                    .setView(dialogBinding.root)
                    .setCancelable(false)
                    .create()
                dialog?.window?.attributes = windowLayoutParams

                dialogBinding.buttonCloseDialog.setOnClickListener {
                    dialog?.dismiss()
                    shutDownDialog()
                }
            }

            dialog?.show()
        }
    }

    fun shutDownDialog() {
        dialog = null
    }
}