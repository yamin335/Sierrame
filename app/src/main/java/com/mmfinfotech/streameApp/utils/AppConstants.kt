package com.mmfinfotech.streameApp.utils

import io.agora.rtc.Constants
import io.agora.rtc.video.BeautyOptions
import io.agora.rtc.video.VideoEncoderConfiguration

/**
 * Created by Somil Rawal on 10-07-2020.
 */
class AppConstants {


    companion object {
        const val alert = "Alert"
        const val appKey = "streamApp#2020"
        const val appSecret = "SHA15AHYVSGJ3#"
        const val resendOtpTime = 31000L

        const val LINE_CHANNEL_ID = "1654427103"
        const val OS_TYPE = "AN"

        const val PREF_NAME = "com.mmfinfotech.streame"
        const val DEFAULT_PROFILE_IDX = 2
        const val PREF_RESOLUTION_IDX = "pref_profile_index"
        const val PREF_ENABLE_STATS = "pref_enable_stats"
        const val PREF_MIRROR_LOCAL = "pref_mirror_local"
        const val PREF_MIRROR_REMOTE = "pref_mirror_remote"
        const val PREF_MIRROR_ENCODE = "pref_mirror_encode"

        const val KEY_CLIENT_ROLE = "key_client_role"

        // Permission request code of any integer value
        const val PERMISSION_REQ_CODE = 1 shl 4 //16


        const val BEAUTY_EFFECT_DEFAULT_CONTRAST = BeautyOptions.LIGHTENING_CONTRAST_NORMAL
        const val BEAUTY_EFFECT_DEFAULT_LIGHTNESS = 0.7f
        const val BEAUTY_EFFECT_DEFAULT_SMOOTHNESS = 0.5f
        const val BEAUTY_EFFECT_DEFAULT_REDNESS = 0.1f


        val DEFAULT_BEAUTY_OPTIONS = BeautyOptions(
            BEAUTY_EFFECT_DEFAULT_CONTRAST,
            BEAUTY_EFFECT_DEFAULT_LIGHTNESS,
            BEAUTY_EFFECT_DEFAULT_SMOOTHNESS,
            BEAUTY_EFFECT_DEFAULT_REDNESS
        )

        @JvmField
        val VIDEO_DIMENSIONS = arrayOf(
            VideoEncoderConfiguration.VD_320x240,
            VideoEncoderConfiguration.VD_480x360,
            VideoEncoderConfiguration.VD_640x360,
            VideoEncoderConfiguration.VD_640x480,
            VideoEncoderConfiguration.VideoDimensions(960, 540),
            VideoEncoderConfiguration.VD_1280x720
        )

        @JvmField
        var VIDEO_MIRROR_MODES = intArrayOf(
            Constants.VIDEO_MIRROR_MODE_AUTO,
            Constants.VIDEO_MIRROR_MODE_ENABLED,
            Constants.VIDEO_MIRROR_MODE_DISABLED
        )
    }

    interface AssertName {
        companion object {
            const val assertCountryCode = "countrycode.json"
        }
    }

    interface Defaults {
        companion object {
            const val string = "N/A"
            const val integer = 0
            const val float = -1.0f
            const val bool = false
        }
    }

    interface FragmentTag {
        companion object {
            const val TagHomeLive = "tag_home"
            const val TagProfilePage = "tag_profile_page"
            const val TagSearch = "tag_Search"
            const val TagTimeLine = "tag_time_line"
            const val TagSreame = "tag_sreame"
        }
    }

    interface FragmentIndex {
        companion object {
            const val FragmentHomeLive = 0
            const val FragmentSearch = 1
            const val FragmentTimeLine = 2
            const val FragmentProfilePage = 3
            const val FragmentStreame = 4
        }
    }

     interface FragmentsProfileIndex{
         companion object{
             const val FragmentPost=1
             const val FragmentClips=2
             const val FragmentRecording=3
         }
     }

    interface FragmentLiveIndex {
        companion object {
            const val FragmentLive = 1
            const val FragmentLiveHotTheme = 2
            const val FragmentLiveStremePlus = 3
            const val FragmentLiveStremeMusic = 4
            const val FragmentNotification = 5
            const val FragmentLeaderBoard = 6
        }
    }

    interface Baseurl {
        companion object {
//          const val url = "https://developer.mmfinfotech.com/streame/"
            const val url = "http://staging-api.streame-live.jp/"
        }
    }

    interface Endpoint {
        companion object {
            const val checkUpdate = "" //check update available or not
            const val signup = "api/register"
            const val signin = "api/login"
            const val changePassword = "Users/ChangePassword"
            const val socialLogin = "api/sociallogin"
            const val verifyEmail = "Users/ForgotPassword"
            const val anotherDevice = "Users/AnotherDevice"
            const val tokenRefresh = "Token/Refresh"
            const val changeStreameId = "api/user/changeusername"
            const val scheduleMy = "api/schedule/my"
            const val UserUpdateinfo = "api/user/updateinfo"
            const val scheduleRemove = "api/schedule/remove/{id}"
            const val scheduleAdd = "api/schedule/add"
            const val verifycode = "api/stream/verifycode"
            const val startverification = "api/stream/startverification"
            const val streamStartData = "api/stream/startdata"
            const val streamLive = "api/stream/live"
            const val hotLive = "api/live/hottheme"
            const val mypagFollowing = "api/mypage/following"
            const val mypagFollower = "api/mypage/followers"
            const val unfolllowFollow = "api/streamer/follow/{follow_id}"
            const val hotConnectLive = "api/live/connect/{stream_id}"
            const val addNoteStremerMemo = "api/streamer/addnote"
            const val liveSchedule = "api/mypage/liveschedule"
            const val getProfile = "api/user/profile"
            const val updateProfile = "api/user/updateprofile"
            const val apiStreamerProfile = "api/streamer/profile/{stream_id}"
            const val apiStreamerPost = "api/streamer/post/{stream_id}"
            const val apiStreamerCliP = "api/clips/fetch/{streamer_id}"
            const val apiMyClips = "api/mypage/myClips"
            const val apiAddMyPost = "api/stream/post"
            const val apiAddMyClip = "api/clips/add"
            const val apiLatestClip = "api/clips/latest"
            const val endBroadCasting = "api/stream/end/{stream_id}"
            const val stremerFollower = "api/streamer/followers/{streamer_id}"
            const val stremerFollowing = "api/streamer/following/{streamer_id}"
            const val settingSecretMode = "api/setting/secret"
            const val settingOriginalPhoto= "api/setting/originalphoto"
            const val settingPrivateFollowerList = "api/setting/privaltefollowlist"
            const val settingPrivateAccount= "api/setting/privateaccount"
            const val userLogout = "api/logout"
            const val settingContactUs = "api/setting/contact"
            const val apiSearchLivers = "api/search"
            const val apiLikeDissLike = "api/likes/{refrence_id}/{type}"
            const val apiAddComment = "api/comment/add"
            const val apipostDetails = "api/post/details/{post_id}"
            const val apiAllComents = "api/comments/show/{refrence_id}/{type}"
            const val apiClipsDetails = "api/clip/details/{clip_id}"
            const val apiTimeLine= "api/post/timeLine"
            const val apiLiveInviteSearch= "api/stream/search"
            const val apiStreamInvite= "api/stream/invite"
            const val apiStreamAccept= "api/stream/accept"
            const val apiStreamReject= "api/stream/reject"

            const val giftFetch = "api/gift/fetch"
            const val likesAndComments = "api/notification/LikesAndComments"
            const val leaderBoard = "api/rank/leaderBoard"
            const val giftSend = "api/gift/send"
            const val subscriptionPlanList = "api/subscriptionPlan/list"
            const val subscriptionPlanBuy = "api/subscriptionPlan/buy/{plainId}"
            const val myCoins = "api/coins/myCoins"
            const val resetPassword = "api/reset/create"
            const val usersLeaderBoard = "api/rank/users_leaderBoard?page=1"
            const val otherUsersLeaderBoard = "api/rank/otherUsers_leaderBoard?page=1"
            const val cheeringLive = "api/cheering/live"
            const val cheeringAnonymous = "api/cheering/anonymous"

            const val acceptInvitation = "api/stream/accept"
            const val rejectInvitation = "api/stream/reject"
            const val postDelete = "api/post/delete/{post_id}"
            const val clipDelete = "api/clip/delete/{clip_id}"
        }
    }

    interface IntentActions {
        companion object {
            const val actionSocial = "actionSocial"
            const val actionVerifyOtp = "actionVerifyOtp"
            const val actionCreatePostOne = "actionCreatePostOne"
            const val actionPostDetails = "actionPostDetails"
            const val actionFamilyContact = "actionFamilyContact"
            const val actionEnterpriseContact = "actionEnterpriseContact"
            const val actionProductDetail = "actionProductDetail"
            const val actionNotification = "com.action.ActionNotification"
            const val actionNotificationAccept = "actionNotificationAccept"
        }
    }

    interface IntentExtras {
        companion object {
            const val email = "intentExtraEmail"
            const val NotifictnRemoteMsg = "intentExtraRemoteMsg"
            const val NotifictnRemoteRefrenceId = "intentExtraRemoteRefrenceId"
            const val country = "intentExtraCountry"
            const val selectedHashTags = "intentHashTags"
            const val selectedStatus = "intentStatus"
            const val addNewHashTag = "intentNewTag"
            const val HotStremeId = "streme_id"
            const val LiveId = "id"
            const val StremeLiveId = "live_streme_id"
            const val liversProfile = "intentExtraLiversProfile"
            const val MyPostPath = "myPostPath"
            const val MyClipPath = "myClipPath"
            const val MyClipsDetails = "MyClipsLiversDetail"
            const val PostDetails = "MyPostsDetail"
            const val selectCommentsRefrenceId = "SelectCommentsId"

        }
    }

    interface RequestCode {
        companion object {
            const val activityResetPassword = 101
            const val GoogleLoginRequestCode = 1
            const val LineLoginRequestCode = 3
            const val activityCountryList = 100
            const val activityHashTags = 200
            const val activityAddHashTags = 201
            const val imagePicker = 300
            const val videoPicker = 310
            const val imageCamera = 400
        }
    }

    interface PermissionRequestCode {
        companion object {
            const val CameraImage = 1001
            const val CameraVideo = 1002
            const val RecordAudio = 1003
            const val ReadWriteExternalStorage = 1004
        }
    }

    interface Permission {
        companion object {
            const val ReadStorage = 1
            const val WriteStorage = 2
            const val multiplePermissions = 202
        }
    }

    /**
     * Social Login Types */
    interface LoginTypes {
        companion object {
            const val FacebookLogin = "5"
            const val GoogleLogin = "2"
            const val LineLogin = "3"
        }
    }

    /**
     * Refrence Type for LikeDissLike
     *
     * type (1=post,2= clips,3 = recording,4= live stream,5= user profile).
     * */
    interface LikeTypes {
        companion object {
            const val TypePost = "1"
            const val TypeClips = "2"
            const val TypeRecording = "3"
            const val TypeLiveStream = "4"
            const val TypeUserProfile = "5"
        }
    }

    /**
     * App Folders
     * */
    interface FolderName {
        companion object {
            const val appFolder = "/Streme"
        }
    }

    /**
     * Broadcast Access Options
     * */
    interface BroadCastAccessOption {
        companion object {
            const val Public = "1"
            const val Private = "2"
        }
    }

    /**
     * BroadCast Message Types.
     * */
    interface MessageType {
        companion object {
            const val normalMessage = 1
            const val GiftMessage = 2
            const val heartMessage = 3
        }
    }
}