package com.nguyennhatminh614.motobikedriverlicenseapp.utils

interface IDataConverter<S, D> {
    fun convert(source: S) : D
}