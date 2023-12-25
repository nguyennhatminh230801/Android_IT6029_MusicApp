package com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.remote.tipshighscore

import arrow.core.Either
import com.google.firebase.firestore.FirebaseFirestore
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.tiphighscore.TipHighScore
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.tiphighscore.TipHighScoreEntityToTipHighScore
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.tiphighscore.TipsHighScoreItemResponse
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.ITipsHighScoreDataSource
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RemoteTipsHighScoreDataSource(
    private val fireStoreDatabase: FirebaseFirestore,
) : ITipsHighScoreDataSource.Remote {

    private val tipsHighScoreCollection by lazy {
        fireStoreDatabase.collection(AppConstant.TIPS_HIGH_SCORE_COLLECTION)
    }

    override suspend fun getTipsHighScore(): Either<Exception?, List<TipHighScore>> =
        suspendCoroutine { continuation ->
            tipsHighScoreCollection.get().addOnCompleteListener { tasks ->
                if (tasks.isSuccessful) {
                    val result: List<TipHighScore> =
                        tasks.result.documents.mapNotNull { it.toObject(TipsHighScoreItemResponse::class.java) }
                            .map { TipHighScoreEntityToTipHighScore.convert(it) }

                    continuation.resume(Either.Right(result))
                } else {
                    continuation.resume(Either.Left(tasks.exception))
                }
            }
        }
}
