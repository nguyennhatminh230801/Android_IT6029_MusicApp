package com.nguyennhatminh614.motobikedriverlicenseapp.data.repository

import arrow.core.Either
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.trafficsign.TrafficSigns

interface ITrafficSignalDataSource {
    interface Remote {
        suspend fun getAllTrafficSignal(): Either<Exception, List<TrafficSigns>>
    }
}
