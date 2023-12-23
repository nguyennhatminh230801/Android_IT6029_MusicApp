package com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.remote.study

import com.google.firebase.firestore.FirebaseFirestore
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.questions.QuestionItemResponse
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.IStudyDataSource
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.interfaces.IResponseListener

class StudyRemoteDataSource(
    private val fireStoreDB: FirebaseFirestore,
) : IStudyDataSource.Remote {

    private val questionCollections by lazy {
        fireStoreDB.collection(AppConstant.NEW_QUESTION_COLLECTION)
    }

    override suspend fun getListQuestion(listener: IResponseListener<MutableList<QuestionItemResponse>>) {
        questionCollections.get().addOnCompleteListener { tasks ->
            if (tasks.isSuccessful) {
                val listQuestions = mutableListOf<QuestionItemResponse>()

                tasks.result.documents.forEach {
                    it.toObject(QuestionItemResponse::class.java)?.let { notNullObject ->
                        listQuestions.add(notNullObject)
                    }
                }

                listQuestions.sortBy { it.id }
                listener.onSuccess(listQuestions)
            } else {
                listener.onError(tasks.exception)
            }
        }
    }
}
