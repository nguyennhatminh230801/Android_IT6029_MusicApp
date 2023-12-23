package com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.remote.question

import arrow.core.Either
import com.google.firebase.firestore.FirebaseFirestore
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.questions.Question
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.questions.QuestionDataConverter
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.questions.QuestionItemResponse
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.IQuestionDataSource
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RemoteQuestionDataSource(
    val firebaseFirestore: FirebaseFirestore,
) : IQuestionDataSource.Remote {

    private val questionCollections by lazy {
        firebaseFirestore.collection(AppConstant.NEW_QUESTION_COLLECTION)
    }

    override suspend fun getQuestions(): Either<Exception, List<Question>> = suspendCoroutine {
        questionCollections.get().addOnCompleteListener { tasks ->
            if (tasks.isSuccessful) {
                val result = tasks.result.documents
                    .mapNotNull { it.toObject(QuestionItemResponse::class.java) }
                    .map { QuestionDataConverter.convert(it) }
                    .sortedBy { it.id }

                it.resume(Either.Right(result))
            } else {
                it.resume(Either.Left(Exception(tasks.exception?.message)))
            }
        }
    }

    override fun getQuestionsAsFlow(): Flow<Either<Exception, List<Question>>> {
        return callbackFlow {
            questionCollections.get().addOnCompleteListener { tasks ->
                if (tasks.isSuccessful) {
                    val result = tasks.result.documents
                        .mapNotNull { it.toObject(QuestionItemResponse::class.java) }
                        .map { QuestionDataConverter.convert(it) }
                        .sortedBy { it.id }

                    trySend(Either.Right(result))
                } else {
                    trySend(Either.Left(Exception(tasks.exception?.message)))
                }
            }

            awaitClose {
                channel.close()
            }
        }
    }
}