package com.mmfinfotech.streameApp.models

import android.os.Parcel
import android.os.Parcelable
import com.mmfinfotech.streameApp.utils.AppConstants
import io.agora.rtm.RtmMessage

data class SocialLogin(
    val socialType: String?,
    val socialId: String?,
    val socialFirstName: String?,
    val socialLastName: String?,
    val socialFullName: String?,
    val socialemail: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(socialType)
        parcel.writeString(socialId)
        parcel.writeString(socialFirstName)
        parcel.writeString(socialLastName)
        parcel.writeString(socialFullName)
        parcel.writeString(socialemail)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SocialLogin> {
        override fun createFromParcel(parcel: Parcel): SocialLogin {
            return SocialLogin(parcel)
        }

        override fun newArray(size: Int): Array<SocialLogin?> {
            return arrayOfNulls(size)
        }
    }
}

data class LiveSChedule(
    val id: String?,
    val user_id: String?,
    val title: String?,
    val description: String?,
    val date: String?,
    val date_time: String?,
    val status: String?,
    val added_on: String?,
    val update_on: String?,
    val name: String?,
    val profile: String?,
    val time: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(user_id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(date)
        parcel.writeString(date_time)
        parcel.writeString(status)
        parcel.writeString(added_on)
        parcel.writeString(update_on)
        parcel.writeString(name)
        parcel.writeString(profile)
        parcel.writeString(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LiveSChedule> {
        override fun createFromParcel(parcel: Parcel): LiveSChedule {
            return LiveSChedule(parcel)
        }

        override fun newArray(size: Int): Array<LiveSChedule?> {
            return arrayOfNulls(size)
        }
    }
}

data class HomeLive(
    val Id: String? = AppConstants.Defaults.string,
    val Name: String? = AppConstants.Defaults.string,
    val Time: String? = AppConstants.Defaults.string,
    val Image: String? = AppConstants.Defaults.string
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(Id)
        parcel.writeString(Name)
        parcel.writeString(Time)
        parcel.writeString(Image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HomeLive> {
        override fun createFromParcel(parcel: Parcel): HomeLive {
            return HomeLive(parcel)
        }

        override fun newArray(size: Int): Array<HomeLive?> {
            return arrayOfNulls(size)
        }
    }
}

data class Schedule(
    val id: String?, val user_id: String?, val title: String?, val description: String?, val date: String?,
    val date_time: String?, val status: String?, val added_on: String?, val update_on: String?, val expire_status: String?,
    val staticMember: Boolean? = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(user_id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(date)
        parcel.writeString(date_time)
        parcel.writeString(status)
        parcel.writeString(added_on)
        parcel.writeString(update_on)
        parcel.writeString(expire_status)
        parcel.writeValue(staticMember)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Schedule> {
        override fun createFromParcel(parcel: Parcel): Schedule {
            return Schedule(parcel)
        }

        override fun newArray(size: Int): Array<Schedule?> {
            return arrayOfNulls(size)
        }
    }
}

/**
 * Hot theme Filters */
data class HotThemeFilter(
    val id: Int?,
    val name: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id!!)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HotThemeFilter> {
        override fun createFromParcel(parcel: Parcel): HotThemeFilter {
            return HotThemeFilter(parcel)
        }

        override fun newArray(size: Int): Array<HotThemeFilter?> {
            return arrayOfNulls(size)
        }
    }
}

/**
 * Data class for HotTheme
 * */
data class HotTheme(
    val id: String? = AppConstants.Defaults.string,
    val user_id: String? = AppConstants.Defaults.string,
    val title: String? = AppConstants.Defaults.string,
    val channel_id: String? = AppConstants.Defaults.string,
    val hashtags: String? = AppConstants.Defaults.string,
    val status: String? = AppConstants.Defaults.string,
    val start_time: String? = AppConstants.Defaults.string,
    val user_name: String? = AppConstants.Defaults.string,
    val user_score: Int? = AppConstants.Defaults.integer,
    val user_profile: String? = AppConstants.Defaults.string,
    val rtcToken: String? = AppConstants.Defaults.string,
    val rtmToken: String? = AppConstants.Defaults.string,
    val accessOption: String? = AppConstants.Defaults.string
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(user_id)
        parcel.writeString(title)
        parcel.writeString(channel_id)
        parcel.writeString(hashtags)
        parcel.writeString(status)
        parcel.writeString(start_time)
        parcel.writeString(user_name)
        parcel.writeValue(user_score)
        parcel.writeString(user_profile)
        parcel.writeString(rtcToken)
        parcel.writeString(rtmToken)
        parcel.writeString(accessOption)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HotTheme> {
        override fun createFromParcel(parcel: Parcel): HotTheme {
            return HotTheme(parcel)
        }

        override fun newArray(size: Int): Array<HotTheme?> {
            return arrayOfNulls(size)
        }
    }

}

data class Following(
    val id: String? = AppConstants.Defaults.string,
    val user_id: String? = AppConstants.Defaults.string,
    val follow_id: String? = AppConstants.Defaults.string,
    val added_on: String? = AppConstants.Defaults.string,
    val name: String? = AppConstants.Defaults.string,
    val profile: String? = AppConstants.Defaults.string,
    val profile_status: String? = AppConstants.Defaults.string
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(user_id)
        parcel.writeString(follow_id)
        parcel.writeString(added_on)
        parcel.writeString(name)
        parcel.writeString(profile)
        parcel.writeString(profile_status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Following> {
        override fun createFromParcel(parcel: Parcel): Following {
            return Following(parcel)
        }

        override fun newArray(size: Int): Array<Following?> {
            return arrayOfNulls(size)
        }
    }

}

/**
 * live User ConnectedModel
 */

data class LiveUserConnected(
    val id: String? = AppConstants.Defaults.string,
    val user_id: String? = AppConstants.Defaults.string,
    val user_name: String? = AppConstants.Defaults.string,
    val user_profile: String? = AppConstants.Defaults.string
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(user_id)
        parcel.writeString(user_name)
        parcel.writeString(user_profile)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LiveUserConnected> {
        override fun createFromParcel(parcel: Parcel): LiveUserConnected {
            return LiveUserConnected(parcel)
        }

        override fun newArray(size: Int): Array<LiveUserConnected?> {
            return arrayOfNulls(size)
        }
    }
}

/**
 * autoMsg
 * */
data class AutoMessage(
    val id: String?,
    val message: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(message)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AutoMessage> {
        override fun createFromParcel(parcel: Parcel): AutoMessage {
            return AutoMessage(parcel)
        }

        override fun newArray(size: Int): Array<AutoMessage?> {
            return arrayOfNulls(size)
        }
    }
}

/**
 * LiveSchedule schedule of hot themer
 *
 * */
data class LiveHotThemeSchedule(
    val id: String?,
    val date_time: String?,
    val day: String?,
    val date: String?,
    val time: String?
) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(date_time)
        parcel.writeString(day)
        parcel.writeString(date)
        parcel.writeString(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LiveHotThemeSchedule> {
        override fun createFromParcel(parcel: Parcel): LiveHotThemeSchedule {
            return LiveHotThemeSchedule(parcel)
        }

        override fun newArray(size: Int): Array<LiveHotThemeSchedule?> {
            return arrayOfNulls(size)
        }
    }
}

/**
 * Streme Post Data
 *
 * */

data class PostResponse(val status: String?, val message: String?, val record: PostRecord?)
data class PostRecord(val data: List<Post>?, val currentPage: Int?, val last_page: Int?, val total_record: Int?, val per_page: Int?)

data class Post(
    val id: Int?, val user_id: String?, val title: String?,
    val description: String?, val file: String?, val thumb: String?,
    val file_type: String?, val status: Int?, val added_on: String?,
    val update_on: String?, val user_name: String?, val user_profile: String?,
    val profile_status: String?, val like_status: String?, val like_count: Int?,
    val comment_count: Int?

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id ?: 0)
        parcel.writeString(user_id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(file)
        parcel.writeString(thumb)
        parcel.writeString(file_type)
        parcel.writeInt(status ?: 0)
        parcel.writeString(added_on)
        parcel.writeString(update_on)
        parcel.writeString(user_name)
        parcel.writeString(user_profile)
        parcel.writeString(profile_status)
        parcel.writeString(like_status)
        parcel.writeInt(like_count ?: 0)
        parcel.writeInt(comment_count ?: 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Post> {
        override fun createFromParcel(parcel: Parcel): Post {
            return Post(parcel)
        }

        override fun newArray(size: Int): Array<Post?> {
            return arrayOfNulls(size)
        }
    }

}


/**
 *
 * this Model For Clips
 * */
data class Clips(
    val id: Int?, val user_id: String?, val title: String?,
    val description: String?, val file: String?, val thumb: String?,
    val file_type: String?, val category: String?, val status: Int?,
    val added_on: String?, val update_on: String?, val user_name: String?,
    val user_profile: String?, val profile_status: String?, val like_status: String?,
    val follow_status: String?, val like_count: Int?, val comment_count: Int?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id ?: 0)
        parcel.writeString(user_id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(file)
        parcel.writeString(thumb)
        parcel.writeString(file_type)
        parcel.writeString(category)
        parcel.writeInt(status ?: 0)
        parcel.writeString(added_on)
        parcel.writeString(update_on)
        parcel.writeString(user_name)
        parcel.writeString(user_profile)
        parcel.writeString(profile_status)
        parcel.writeString(like_status)
        parcel.writeString(follow_status)
        parcel.writeInt(like_count ?: 0)
        parcel.writeInt(comment_count ?: 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Clips> {
        override fun createFromParcel(parcel: Parcel): Clips {
            return Clips(parcel)
        }

        override fun newArray(size: Int): Array<Clips?> {
            return arrayOfNulls(size)
        }
    }

}

data class CommentsResponse(val status: String?, val message: String?, val record: CommentsRecord?)

data class CommentsRecord(val data: List<CommentsChet>?, val currentPage: Int?,
                          val last_page: Int?, val total_record: Int?, val per_page: Int?)

data class CommentsChet(
    val id: Int?, val user_id: Int?, val refrence_id: String?,
    val comment: String?, val owner_id: Int?, val type: Int?,
    val status: Int?, val added_on: String?, val update_on: String?,
    val user_name: String?, val user_profile: String?, val profile_status: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id ?: 0)
        parcel.writeInt(user_id ?: 0)
        parcel.writeString(refrence_id)
        parcel.writeString(comment)
        parcel.writeInt(owner_id ?: 0)
        parcel.writeInt(type ?: 0)
        parcel.writeInt(status ?: 0)
        parcel.writeString(added_on)
        parcel.writeString(update_on)
        parcel.writeString(user_name)
        parcel.writeString(user_profile)
        parcel.writeString(profile_status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CommentsChet> {
        override fun createFromParcel(parcel: Parcel): CommentsChet {
            return CommentsChet(parcel)
        }

        override fun newArray(size: Int): Array<CommentsChet?> {
            return arrayOfNulls(size)
        }
    }
}

/**
 * To keep the data of liver from api.
 * */
data class LiversProfile(
    var id: String?,
    var channelId: String?,
    var userId: String?,
    var title: String?,
    var description: String?,
    var hashTags: String?,
    var category: String?,
    var startTime: String?,
    var endTime: String?,
    var status: String?,
    var addedOn: String?,
    var updateOn: String?,
    var streamerRanking: String?,
    var streamerStatus: String?,
    var followStatus: String?,
    var like_status: String?,
    var profileNotes: String?,
    var totalGifts: String?,
    var streamerId: String?,
    var streamerName: String?,
    var streamerProfile: String?,
    var rtcToken: String?,
    var rtmToken: String?,
    var userAccount: String?,
    var arrLiveUserConnected: ArrayList<LiveUserConnected?>?,
    val arrAutoMessage: ArrayList<AutoMessage?>?,
    val arrLiveScheduleHotTheme: ArrayList<LiveHotThemeSchedule?>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),    //  id
        parcel.readString(),    //  channelId
        parcel.readString(),    //  userId
        parcel.readString(),    //  title
        parcel.readString(),    //  description
        parcel.readString(),    //  hashTags
        parcel.readString(),    //  category
        parcel.readString(),    //  startTime
        parcel.readString(),    //  endTime
        parcel.readString(),    //  status
        parcel.readString(),    //  addedOn
        parcel.readString(),    //  updateOn
        parcel.readString(),    //  streamerRanking
        parcel.readString(),    //  streamerStatus
        parcel.readString(),    //  followStatus
        parcel.readString(),    //  like_status
        parcel.readString(),    //  profileNotes
        parcel.readString(),    //  totalGifts
        parcel.readString(),    //  streamerId
        parcel.readString(),    //  streamerName
        parcel.readString(),    //  streamerProfile
        parcel.readString(),    //  rtcToken
        parcel.readString(),    //  rtmToken
        parcel.readString(),    //  userAccount
        parcel.createTypedArrayList(LiveUserConnected.CREATOR),    //  arrLiveUserConnected
        parcel.createTypedArrayList(AutoMessage.CREATOR),    //  arrAutoMsg
        parcel.createTypedArrayList(LiveHotThemeSchedule.CREATOR),    //  arrLiveScheduleHotTheme
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)  //  id
        parcel.writeString(channelId)   //  channelId
        parcel.writeString(userId)  //  userId
        parcel.writeString(title)   //  title
        parcel.writeString(description) //  description
        parcel.writeString(hashTags)    //  hashTags
        parcel.writeString(category)    //  category
        parcel.writeString(startTime)   //  startTime
        parcel.writeString(endTime) //  endTime
        parcel.writeString(status)  //  status
        parcel.writeString(addedOn) //  addedOn
        parcel.writeString(updateOn)    //  updateOn
        parcel.writeString(streamerRanking) //  streamerRanking
        parcel.writeString(streamerStatus)  //  streamerStatus
        parcel.writeString(followStatus)    //  followStatus
        parcel.writeString(like_status)    //  like_status
        parcel.writeString(profileNotes)    //  profileNotes
        parcel.writeString(totalGifts)  //  totalGifts
        parcel.writeString(streamerId)  //  streamerId
        parcel.writeString(streamerName)    //  streamerName
        parcel.writeString(streamerProfile) //  streamerProfile
        parcel.writeString(rtcToken)    //  rtcToken
        parcel.writeString(rtmToken)    //  rtmToken
        parcel.writeString(userAccount) //  userAccount
        parcel.writeTypedList(arrLiveUserConnected) //  arrLiveUserConnected
        parcel.writeTypedList(arrAutoMessage) //  arrAutoMsg
        parcel.writeTypedList(arrLiveScheduleHotTheme) //  arrLiveScheduleHotTheme
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LiversProfile> {
        override fun createFromParcel(parcel: Parcel): LiversProfile {
            return LiversProfile(parcel)
        }

        override fun newArray(size: Int): Array<LiversProfile?> {
            return arrayOfNulls(size)
        }
    }
}

data class MessageBean(
    var account: String?,
    val rtmMessage: RtmMessage?,
    val beSelf: Boolean?,
    val messageType: Int?,
    val message: String?,
    val profileImage: String?,
    val profileName: String?
)

/**
 * data class for live  persons
 *
 * */
data class Liver(
    val id: String?,
    val username: String?,
    val name: String?,
    val profile: String?,
    val my_status: String?,
    var isSeleted: Boolean? = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(username)
        parcel.writeString(name)
        parcel.writeString(profile)
        parcel.writeString(my_status)
        parcel.writeValue(isSeleted)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Liver> {
        override fun createFromParcel(parcel: Parcel): Liver {
            return Liver(parcel)
        }

        override fun newArray(size: Int): Array<Liver?> {
            return arrayOfNulls(size)
        }
    }
}

data class Gift(
    val id: Int?,
    val title: String?,
    val image: String?,
    val coinPrice: Int?,
    val sendPrice: Int?,
    val description: String?,
    val category: Int?,
    val duration: String?,
    val status: Int?,
    val addedOn: String?,
    val updateOn: String?,
    val categoryName: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(title)
        parcel.writeString(image)
        parcel.writeValue(coinPrice)
        parcel.writeValue(sendPrice)
        parcel.writeString(description)
        parcel.writeValue(category)
        parcel.writeString(duration)
        parcel.writeValue(status)
        parcel.writeString(addedOn)
        parcel.writeString(updateOn)
        parcel.writeString(categoryName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Gift> {
        override fun createFromParcel(parcel: Parcel): Gift {
            return Gift(parcel)
        }

        override fun newArray(size: Int): Array<Gift?> {
            return arrayOfNulls(size)
        }
    }
}

data class Rank(
    val id: Int? = AppConstants.Defaults.integer,
    val spentCoins: String? = AppConstants.Defaults.string,
    val username: String? = AppConstants.Defaults.string,
    val score: Int? = AppConstants.Defaults.integer,
    val userProfile: String? = AppConstants.Defaults.string,
    val updatedAt: String? = AppConstants.Defaults.string,
    val rank: Int? = AppConstants.Defaults.integer
)

data class Coin(
    val id: Int? = AppConstants.Defaults.integer,
    val title: String? = AppConstants.Defaults.string,
    val icon: String? = AppConstants.Defaults.string,
    val price: Int? = AppConstants.Defaults.integer,
    val currency: String? = AppConstants.Defaults.string,
    val coins: Int? = AppConstants.Defaults.integer,
    val description: String? = AppConstants.Defaults.string,
    val condition_description: String? = AppConstants.Defaults.string,
    val type: Int? = AppConstants.Defaults.integer,
    val status: Int? = AppConstants.Defaults.integer,
    val added_on: String? = AppConstants.Defaults.string,
    val update_on: String? = AppConstants.Defaults.string,
    var isSelected: Boolean? = AppConstants.Defaults.bool
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(title)
        parcel.writeString(icon)
        parcel.writeValue(price)
        parcel.writeString(currency)
        parcel.writeValue(coins)
        parcel.writeString(description)
        parcel.writeString(condition_description)
        parcel.writeValue(type)
        parcel.writeValue(status)
        parcel.writeString(added_on)
        parcel.writeString(update_on)
        parcel.writeValue(isSelected)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Coin> {
        override fun createFromParcel(parcel: Parcel): Coin {
            return Coin(parcel)
        }

        override fun newArray(size: Int): Array<Coin?> {
            return arrayOfNulls(size)
        }
    }
}

data class BabyCoin(
    val id: Int? = AppConstants.Defaults.integer,
    val coins: Int? = AppConstants.Defaults.integer,
    val giftedBy: Int? = AppConstants.Defaults.integer,
    val planId: Int? = AppConstants.Defaults.integer,
    val status: Int? = AppConstants.Defaults.integer,
    val addedOn: String? = AppConstants.Defaults.string,
    val senderName: String? = AppConstants.Defaults.string,
    val senderImage: String? = AppConstants.Defaults.string,
    val planName: String? = AppConstants.Defaults.string,
    val streamerId: Int? = AppConstants.Defaults.integer
)

data class CheeringResponse(val status: String?, val message: String?, val record: CheeringRecord?)

data class CheeringRecord(val data: List<Cheering>?, val currentPage: Number?, val last_page: Number?, val total_record: Number?, val per_page: Number?)

data class Cheering(val id: Int?, val secret_mode: String?, val my_status: String?,
                    val like_count: Int?, val spent_coins: String?, val username: String?,
                    val user_profile: String?, val score: Double?)

data class AnonymousCheeringResponse(val status: String?, val message: String?, val record: AnonymousCheeringRecord?)

data class AnonymousCheeringRecord(val data: List<AnonymousCheering>?, val currentPage: Number?,
                                   val last_page: Number?, val total_record: Number?, val per_page: Number?)

data class AnonymousCheering(val spent_coins: String?, val id: Int?, val my_status: String?,
                             val username: String?, val user_profile: String?, val score: Double?, val updated_at: String?)

data class BlockedUser(
    val id: String?,
    val blockedUserId: String?,
    val blockedBy: String?,
    val status: String?,
    val blockedUserProfile: String?,
    val blockedUserName: String?,
    var blockedUserProfileStatus: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(), // id
        parcel.readString(), //user id
        parcel.readString(), // by
        parcel.readString(), //status
        parcel.readString(), //userp
        parcel.readString(), //username
        parcel.readString(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(blockedUserId)
        parcel.writeString(blockedBy)
        parcel.writeString(status)
        parcel.writeString(blockedUserProfile)
        parcel.writeValue(blockedUserName)
        parcel.writeValue(blockedUserProfileStatus)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Liver> {
        override fun createFromParcel(parcel: Parcel): Liver {
            return Liver(parcel)
        }

        override fun newArray(size: Int): Array<Liver?> {
            return arrayOfNulls(size)
        }
    }
}

data class CommonResponse(val status: String?, val message: String?)
data class ClipDeleteResponse(val success: String?, val message: String?)

data class FollowUnFollowResponse(val status: String?, val message: String?, val record: FollowUnFollowRecord?)
data class FollowUnFollowRecord(val follow_status: String?)
