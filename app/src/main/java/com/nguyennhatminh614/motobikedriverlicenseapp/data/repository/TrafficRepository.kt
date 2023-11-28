package com.nguyennhatminh614.motobikedriverlicenseapp.data.repository

import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.trafficsign.TrafficSignsEntity
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.interfaces.IResponseListener

class TrafficRepository(
    private val remote: ITrafficSignalDataSource.Remote,
) : ITrafficSignalDataSource.Remote {
    override suspend fun getAllTrafficSignal() = remote.getAllTrafficSignal()
}
