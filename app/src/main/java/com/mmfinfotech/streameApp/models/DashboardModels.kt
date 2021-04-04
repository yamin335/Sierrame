package com.mmfinfotech.streameApp.models

import android.os.Parcel
import android.os.Parcelable
import com.mmfinfotech.streameApp.utils.AppConstants

data class LatestClipResponse(val status: String?, val message: String?, val record: LatestClipRecord?)

data class LatestClipRecord(val data: List<Clips>?, val currentPage: Int?,
                  val last_page: Int?, val total_record: Int?, val per_page: Int?)

data class LiveStreamHashTagCategoryResponse(val status: String?, val message: String?, val record: LiveStreamHashTagCategoryRecord?)

data class LiveStreamHashTagCategoryRecord(val uuid: String?, val hashtags: List<LiveStreamHashTag>?, val category: List<LiveStreamCategory>?)

/**
 * HashTags for streme
 * */

data class LiveStreamHashTag(
    val id: Int? = 0,
    val tag_name: String? = AppConstants.Defaults.string,
    var selectedValue: Boolean? = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id ?: 0)
        parcel.writeString(tag_name)
        parcel.writeValue(selectedValue)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LiveStreamHashTag> {
        override fun createFromParcel(parcel: Parcel): LiveStreamHashTag {
            return LiveStreamHashTag(parcel)
        }

        override fun newArray(size: Int): Array<LiveStreamHashTag?> {
            return arrayOfNulls(size)
        }
    }
}

data class LiveStreamCategory(val id: Int?, val name: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id ?: 0)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LiveStreamCategory> {
        override fun createFromParcel(parcel: Parcel): LiveStreamCategory {
            return LiveStreamCategory(parcel)
        }

        override fun newArray(size: Int): Array<LiveStreamCategory?> {
            return arrayOfNulls(size)
        }
    }
}

data class SecretModeResponse(val status: String?, val message: String?, val record: SecretModeRecord?)

data class SecretModeRecord(val secret_mode: String?)

data class PrivateFollowerListResponse(val status: String?, val message: String?, val record: PrivateFollowerListRecord?)

data class PrivateFollowerListRecord(val private_follow_list: String?)