package com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.remote.trafficsign

import arrow.core.Either
import arrow.core.right
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.trafficsign.TrafficSignDataConverter
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.trafficsign.TrafficSigns
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.trafficsign.TrafficSignsEntity
import com.nguyennhatminh614.motobikedriverlicenseapp.data.repository.ITrafficSignalDataSource
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.processDoubleQuotes
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.processEndline
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.interfaces.IResponseListener
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TrafficSignRemoteDataSource(
    private val fireStoreDB: FirebaseFirestore,
) : ITrafficSignalDataSource.Remote {

    private val trafficSignsCollections by lazy {
        fireStoreDB.collection(AppConstant.TRAFFIC_SIGN_COLLECTION)
    }

    override suspend fun getAllTrafficSignal(): Either<Exception, List<TrafficSigns>> =
        suspendCoroutine { cont ->
            trafficSignsCollections.get().addOnCompleteListener { tasks ->
                if (tasks.isSuccessful) {
                    val result =
                        tasks.result.documents.mapNotNull { it.toObject(TrafficSignsEntity::class.java) }
                            .map {
                                val trafficSigns = TrafficSignDataConverter.convert(it)
                                trafficSigns.copy(
                                    title = trafficSigns.title.processDoubleQuotes(),
                                    description = trafficSigns.description.processEndline(),
                                )
                            }

                    cont.resume(Either.Right(result))
                } else {
                    cont.resume(Either.Left(Exception(tasks.exception?.message)))
                }
            }
        }
}
