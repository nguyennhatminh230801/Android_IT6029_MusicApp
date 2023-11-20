package com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.tiphighscore

class TipsHighScoreEntity() {
    var id: Int? = null
    var title: String? = null
    var content: String? = null

    constructor(
        id: Int,
        title: String,
        content: String,
    ) : this() {
        this.id = id
        this.title = title
        this.content = content
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TipsHighScoreEntity

        if (id != other.id) return false
        if (title != other.title) return false
        if (content != other.content) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (content?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "TipsHighScore(id=$id, title=$title, content=$content)"
    }
}
