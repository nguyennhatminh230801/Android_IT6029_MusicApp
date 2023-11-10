package com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions

import android.content.SharedPreferences
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.LicenseType
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant

fun SharedPreferences.setCurrentLicenseType(currentLicenseType: String) {
    this.edit().putString(AppConstant.CURRENT_LICENSE_TYPE, currentLicenseType).apply()
}

fun SharedPreferences.getCurrentLicenseType() : LicenseType {
    val licenseType = this.getString(AppConstant.CURRENT_LICENSE_TYPE, LicenseType.A1.type)
    return enumValues<LicenseType>().first { it.type == licenseType }
}
