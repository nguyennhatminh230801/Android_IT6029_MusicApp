package com.nguyennhatminh614.motobikedriverlicenseapp.data.repository

class TrafficRepository(
    private val remote: ITrafficSignalDataSource.Remote,
) : ITrafficSignalDataSource.Remote {
    override suspend fun getAllTrafficSignal() = remote.getAllTrafficSignal()
}
