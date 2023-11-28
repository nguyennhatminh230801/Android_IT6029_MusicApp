package com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.trafficsign

import com.nguyennhatminh614.motobikedriverlicenseapp.utils.IDataConverter

object TrafficSignDataConverter : IDataConverter<TrafficSignsEntity, TrafficSigns> {
    override fun convert(source: TrafficSignsEntity): TrafficSigns {
        return TrafficSigns(
            id = source.id,
            title = source.title,
            description = source.description,
            thumbnail = source.imageUrl,
            category = TrafficSignCategory.values().first { it.value == source.category }
        )
    }
}