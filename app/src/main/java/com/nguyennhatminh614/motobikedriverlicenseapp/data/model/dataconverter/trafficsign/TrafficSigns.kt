package com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.trafficsign

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TrafficSigns(
    val id: String,
    val title: String,
    val description: String,
    val thumbnail: String,
    val category: TrafficSignCategory,
) : Parcelable {

    companion object {
        fun getDiffUtilCallback() =
            object : DiffUtil.ItemCallback<TrafficSigns>() {
                override fun areItemsTheSame(
                    oldItem: TrafficSigns,
                    newItem: TrafficSigns,
                ): Boolean = oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: TrafficSigns,
                    newItem: TrafficSigns,
                ): Boolean = oldItem == newItem
            }
    }
}

enum class TrafficSignCategory(val value: String) {
    RESTRICT_TRAFFIC_SIGNAL(Constant.RESTRICT_TRAFFIC_SIGNAL_VALUE),
    COMMAND_TRAFFIC_SIGNAL(Constant.COMMAND_TRAFFIC_SIGNAL_VALUE),
    INSTRUCTION_TRAFFIC_SIGNAL(Constant.INSTRUCTION_TRAFFIC_SIGNAL_VALUE),
    SUB_TRAFFIC_SIGNAL(Constant.SUB_TRAFFIC_SIGNAL_VALUE),
    WARNING_TRAFFIC_SIGNAL(Constant.WARNING_TRAFFIC_SIGNAL_VALUE);

    object Constant {
        const val RESTRICT_TRAFFIC_SIGNAL_VALUE = "RestrictTrafficSign"
        const val COMMAND_TRAFFIC_SIGNAL_VALUE = "CommandTrafficSignal"
        const val INSTRUCTION_TRAFFIC_SIGNAL_VALUE = "InstructionTrafficSignal"
        const val SUB_TRAFFIC_SIGNAL_VALUE = "SubTrafficSignal"
        const val WARNING_TRAFFIC_SIGNAL_VALUE = "WarningTrafficSignal"
    }
}
