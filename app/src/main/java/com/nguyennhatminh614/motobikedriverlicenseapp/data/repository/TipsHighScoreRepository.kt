package com.nguyennhatminh614.motobikedriverlicenseapp.data.repository

class TipsHighScoreRepository(
    private val remote: ITipsHighScoreDataSource.Remote,
) : ITipsHighScoreDataSource.Remote {

    override suspend fun getTipsHighScore() = remote.getTipsHighScore()
}
