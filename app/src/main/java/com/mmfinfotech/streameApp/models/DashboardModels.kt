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

data class PrivateAccountResponse(val status: String?, val message: String?, val record: PrivateAccountRecord?)

data class PrivateAccountRecord(val private_account: String?)

data class ProfileResponse(val status: String?, val message: String?, val record: ProfileRecord?)

data class ProfileRecord(val id: Int?, val name: String?, val username: String?, val country_code: String?,
                  val phone: String?, val email: String?, val stream_id: Int?, val profile: String?,
                  val phone_authentication: String?, val secret_mode: String?, val orignal_photo: String?,
                  val private_follow_list: String?, val private_account: String?, val followings: Int?,
                  val followers: Int?, val ranking: Double?, val like_count: Int?, val lavel: String?)

data class AddScheduleResponse(val status: String?, val message: String?, val record: List<Schedule>?, val error: List<Any>?)

data class ScheduleResponse(val status: String?, val message: String?, val record: List<Schedule>?)

data class ClipDetailsResponse(val status: String?, val message: String?, val record: ClipDetailsRecord?)

data class ClipDetailsRecord(val id: Int?, val user_id: String?, val title: String?, val description: String?,
                  val file: String?, val thumb: String?, val file_type: String?, val category: String?,
                  val status: Int?, val added_on: String?, val update_on: String?, val user_name: String?,
                  val user_profile: String?, val profile_status: String?, val users_score: Double?,
                  val follow_status: String?, val like_status: String?, val like_count: Int?, val comment_count: Int?)

data class LikeDisLikeResponse(val status: String?, val message: String?, val status_changed: String?)

data class NotificationResponse(val status: String?, val message: String?, val record: NotificationRecord?)

data class Notification(val id: Int?, val receiver: String?, val payload: String?, val type: Int?,
                            val added_on: String?, val sender_id: Int?, val sender_name: String?,
                            val sender_profile: String?, val notification: String?,
                            val reference_id: String?, val content: Int?)

data class NotificationRecord(val data: List<Notification>?)

data class FollowerListResponse(val status: String?, val message: String?, val record: FollowerListRecord?)

data class FollowerData(val id: Int?, val user_id: String?, val added_on: String?,
                        val name: String?, val profile: String?, val profile_status: String?)

data class FollowerListRecord(val data: List<FollowerData>?, val currentPage: Int?,
                              val last_page: Int?, val total_record: Int?, val per_page: Int?)