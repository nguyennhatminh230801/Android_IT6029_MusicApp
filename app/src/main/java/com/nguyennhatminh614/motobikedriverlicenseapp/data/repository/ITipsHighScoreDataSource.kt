package com.nguyennhatminh614.motobikedriverlicenseapp.data.repository

import arrow.core.Either
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.tiphighscore.TipHighScore

interface ITipsHighScoreDataSource {
    interface Remote {
        suspend fun getTipsHighScore() : Either<Exception?, List<TipHighScore>>
    }
}
