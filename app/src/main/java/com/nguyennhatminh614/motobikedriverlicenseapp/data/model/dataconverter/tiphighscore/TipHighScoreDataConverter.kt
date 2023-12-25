package com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.tiphighscore

import com.nguyennhatminh614.motobikedriverlicenseapp.utils.IDataConverter

object TipHighScoreEntityToTipHighScore : IDataConverter<TipsHighScoreItemResponse, TipHighScore>{
    override fun convert(source: TipsHighScoreItemResponse): TipHighScore {
        return TipHighScore(
            id = source.id ?: 0,
            title = source.title ?: "",
            content = source.content ?: "",
        )
    }
}