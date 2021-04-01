package com.mmfinfotech.streameApp.util.retrofit

import com.google.gson.JsonObject
import com.mmfinfotech.streameApp.utils.AppConstants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface MyApiEndpointInterface {
    /**
     * API  using for Signup
     * */
    @POST(AppConstants.Endpoint.signup)
    fun callSignup(@Body jsonObject: JsonObject?): Call<JsonObject?>

    /**
     * API  using for Signin
     * */
    @POST(AppConstants.Endpoint.signin)
    fun callSignin(@Body jsonObject: JsonObject?): Call<JsonObject?>

    /**
     * API  using for SocialLogin
     * */
    @POST(AppConstants.Endpoint.socialLogin)
    fun callSocialLogin(@Body jsonObject: JsonObject?): Call<JsonObject?>

    /**
     * Request to refresh the token after token expires.
     * */
    @POST(AppConstants.Endpoint.tokenRefresh)
    fun callTokenRefresh(@Body jsonObject: JsonObject?): Call<JsonObject>

    /**
     * Request for change the the streme Id
     *
     * */
    @POST(AppConstants.Endpoint.changeStreameId)
    fun callChangeStremeId(@HeaderMap headers: Map<String, String>, @Body jsonObject: JsonObject?): Call<JsonObject?>

    /**
     * Request for Get All Schedule My
     * */
    @GET(AppConstants.Endpoint.scheduleMy)
    fun callScheduleMy(@HeaderMap headers: Map<String, String>): Call<JsonObject?>

    /**
     * Request for Update User
     **/
    @POST(AppConstants.Endpoint.UserUpdateinfo)
    fun callUserUpdateinfo(@HeaderMap headers: Map<String, String>, @Body jsonObject: JsonObject?): Call<JsonObject?>

    /**
     * Request for varify no
     **/
    @POST(AppConstants.Endpoint.verifycode)
    fun callVerifyCode(@HeaderMap headers: Map<String, String>, @Body jsonObject: JsonObject?): Call<JsonObject?>

    /**
     * Request for send the varification Code
     **/
    @POST(AppConstants.Endpoint.startverification)
    fun callSendVarification(@HeaderMap headers: Map<String, String>, @Body jsonObject: JsonObject?): Call<JsonObject?>

    /**
     * Request for add schedule
     **/
    @Multipart
    @POST(AppConstants.Endpoint.scheduleAdd)
    fun callAddSchedule(@HeaderMap headers: Map<String, String>, @Part("schedule") schedule: RequestBody): Call<JsonObject?>

    /**
     * Start Live Streming by mySelf***
     **/
    @Multipart
    @POST(AppConstants.Endpoint.streamLive)
    fun callStreamLive(
        @HeaderMap headers: Map<String, String>,
        @Part("channel_id") id: RequestBody?,
        @Part("title") title: RequestBody?,
        @Part("discription") discription: RequestBody?,
        @Part("hashtags") hashtags: RequestBody?,
        @Part("category") category: RequestBody?,
        @Part("access_option") access_option: RequestBody?,
        @Part("password") password: RequestBody?
    ): Call<JsonObject?>

    /**
     * Request for Remove Schedule
     *
     **/
    @GET(AppConstants.Endpoint.scheduleRemove)
    fun callScheduleRemove(@HeaderMap headers: Map<String, String>, @Path("id") id: String?): Call<JsonObject?>

    /**
     * Request for start Streaming Data
     *
     **/
    @GET(AppConstants.Endpoint.streamStartData)
    fun callStremeData(@HeaderMap headers: Map<String, String>): Call<JsonObject?>

    /**
     * Request for HotStream
     *
     **/
    @POST(AppConstants.Endpoint.hotLive)
    fun callHotTheme(@HeaderMap headers: Map<String, String>, @Query("page") page: String?, @Body jsonObj: JsonObject): Call<JsonObject?>

    /**
     * Request for follower
     *
     **/
    @GET(AppConstants.Endpoint.mypagFollower)
    fun callMypagFollower(@HeaderMap headers: Map<String, String>): Call<JsonObject?>

    /**
     * Request for followeing
     *
     **/
    @GET(AppConstants.Endpoint.mypagFollowing)
    fun callMypagFollowing(@HeaderMap headers: Map<String, String>): Call<JsonObject?>

    /**
     * Request for Follow the Streamer and Unfollow the Streamer
     *
     **/
    @GET(AppConstants.Endpoint.unfolllowFollow)
    fun callFollowUnFollow(@HeaderMap headers: Map<String, String>, @Path("follow_id") id: String?): Call<JsonObject?>

    /**
     * Request for connectLive from HotTheme
     *
     **/
    @Multipart
    @POST(AppConstants.Endpoint.hotConnectLive)
    fun callConnectLive(
        @HeaderMap headers: Map<String, String>,
        @Path("stream_id") id: String?,
        @Part("password") password: RequestBody?
    ): Call<JsonObject?>

    /**
     * Request for ending live borqadcasting connectLion
     *
     **/
    @GET(AppConstants.Endpoint.endBroadCasting)
    fun callEndBroadcasting(@HeaderMap headers: Map<String, String>, @Path("stream_id") id: String?): Call<JsonObject?>

    /**
     * Request for Add Memo By user for the stremer
     *
     **/
    @POST(AppConstants.Endpoint.addNoteStremerMemo)
    fun callMemoforStremer(@HeaderMap headers: Map<String, String>, @Body jsonObject: JsonObject?): Call<JsonObject?>

    /**
     * Request for add ProfileImage
     *
     **/
    @Multipart
    @POST(AppConstants.Endpoint.updateProfile)
    fun callUpdateProfile(
        @HeaderMap headers: Map<String, String>, @Part image: MultipartBody.Part,
        @Part("remove") remove: RequestBody?
    ): Call<JsonObject?>


    /**
     * get UserProfile In my page
     *
     **/
    @GET(AppConstants.Endpoint.getProfile)
    fun callGetProfile(@HeaderMap headers: Map<String, String>): Call<JsonObject?>

    /**
     * get Live Schedule of User
     * */
    @POST(AppConstants.Endpoint.liveSchedule)
    fun callGetLiveSchedul(@HeaderMap headers: Map<String, String>, @Body jsonObject: JsonObject?): Call<JsonObject?>

    /**
     * This Calling For Stremer Details Api
     * */
    @GET(AppConstants.Endpoint.apiStreamerProfile)
    fun callStreamProfile(@HeaderMap header: Map<String, String>, @Path("stream_id") stremerId: String): Call<JsonObject?>

    /**
     * This Calling For Stremer All Post
     * */
    @GET(AppConstants.Endpoint.apiStreamerPost)
    fun callStreamPost(
        @HeaderMap header: Map<String, String>,
        @Path("stream_id") stremerId: String,
        @Query("page") page: String
    ): Call<JsonObject?>

    /**
     * This Calling For Stremer All Clips
     * */
    @GET(AppConstants.Endpoint.apiStreamerCliP)
    fun callStreamClip(
        @HeaderMap header: Map<String, String>,
        @Path("streamer_id") stremerId: String,
        @Query("page") page: String
    ): Call<JsonObject?>

    /**
     * This Calling For My All Clips
     * */
    @GET(AppConstants.Endpoint.apiMyClips)
    fun callMyClips(
        @HeaderMap header: Map<String, String>,
        @Query("page") page: String
    ): Call<JsonObject?>


    @Multipart
    @POST(AppConstants.Endpoint.apiAddMyPost)
    fun callMyPost(
        @HeaderMap headers: Map<String, String>,
        @Part file: MultipartBody.Part,
        @Part("file_type") file_type: RequestBody?,
        @Part("title") title: RequestBody?,
        @Part("description") description: RequestBody?
    ): Call<JsonObject?>

    @Multipart
    @POST(AppConstants.Endpoint.apiAddMyClip)
    fun callAddMyClip(
        @HeaderMap headers: Map<String, String>,
        @Part file: MultipartBody.Part,
        @Part("file_type") file_type: RequestBody?,
        @Part("title") title: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("category") category: RequestBody?
    ): Call<JsonObject?>


    @GET(AppConstants.Endpoint.stremerFollowing)
    fun callStremerFollowing(
        @HeaderMap header: Map<String, String>,
        @Path("streamer_id") stremerId: String
    ): Call<JsonObject?>

    @GET(AppConstants.Endpoint.stremerFollower)
    fun callstremerFollower(
        @HeaderMap header: Map<String, String>,
        @Path("streamer_id") stremerId: String
    ): Call<JsonObject?>

    /**
     * Setting Section Api SecretMode
     **/
    @GET(AppConstants.Endpoint.settingSecretMode)
    fun callsettingSecret(@HeaderMap header: Map<String, String>): Call<JsonObject?>

    /**
     * setting Original Photos
     **/
    @GET(AppConstants.Endpoint.settingOriginalPhoto)
    fun callsettingOriginalPhoto(@HeaderMap header: Map<String, String>): Call<JsonObject?>

    /**
     * setting Private FollowerList
     * */
    @GET(AppConstants.Endpoint.settingPrivateFollowerList)
    fun callsettingPrivateFollowerList(@HeaderMap header: Map<String, String>): Call<JsonObject?>

    /**
     * Setting private account
     * */
    @GET(AppConstants.Endpoint.settingPrivateAccount)
    fun callsettingPrivateAccount(@HeaderMap header: Map<String, String>): Call<JsonObject?>

    /**
     * Setting Private
     **/
    @GET(AppConstants.Endpoint.userLogout)
    fun callLogout(@HeaderMap header: Map<String, String>): Call<JsonObject?>

    /**
     * Setting  ContactUs
     * */
    @POST(AppConstants.Endpoint.settingContactUs)
    fun callsettingContactUs(@HeaderMap header: Map<String, String>, @Body jsonObject: JsonObject?): Call<JsonObject?>

    /**
     * Search Live stremers
     * */
    @POST(AppConstants.Endpoint.apiSearchLivers)
    fun callSearchLivers(
        @HeaderMap header: Map<String, String>,
        @Query("page") page: String?, @Body jsonObject: JsonObject?
    ): Call<JsonObject?>
/**
     * Search Live stremers
     * */
    @POST(AppConstants.Endpoint.apiLiveInviteSearch)
    fun callSearchInviteLivers(
    @HeaderMap header: Map<String, String>,
    @Query("page") page: String?, @Body jsonObject: JsonObject?
    ): Call<JsonObject?>

    /**
     * Search  comments stremers
     *
     **/
    @POST(AppConstants.Endpoint.apiAddComment)
    fun callAddComments(
        @HeaderMap header: Map<String, String>,
        @Body jsonObject: JsonObject?): Call<JsonObject?>


    /**
     * Like Disslike APi
     *
     **/
    @GET(AppConstants.Endpoint.apiLikeDissLike)
    fun callLikeDisslike(
        @HeaderMap header: Map<String, String>,
        @Path("refrence_id") refrence_id: String?, @Path("type") type: String?
    ): Call<JsonObject?>

    /**
     * Search All Comments
     *
     * */
    @GET(AppConstants.Endpoint.apiAllComents)
    fun callGetAllComments(
        @HeaderMap header: Map<String, String>, @Path("refrence_id") refrence_id: String?, @Path("type") type: String?
    ): Call<JsonObject?>

    /**
     * This Calling For singlePost Details
     * */
    @GET(AppConstants.Endpoint.apipostDetails)
    fun callPostDetails(
        @HeaderMap header: Map<String, String>,
        @Path("post_id") post_id: String
    ): Call<JsonObject?>

    /**
     * This Calling For singlePost Details
     * */
    @GET(AppConstants.Endpoint.apiClipsDetails)
    fun callClipsDetails(
        @HeaderMap header: Map<String, String>,
        @Path("clip_id") page: String
    ): Call<JsonObject?>

    /**
     * This Calling For My All Clips
     * */
    @GET(AppConstants.Endpoint.apiTimeLine)
    fun callTimeLines(
        @HeaderMap header: Map<String, String>,
        @Query("page") page: String
    ): Call<JsonObject?>


    /**
     * This Calling ForLatest Clips In Search Section
     *
     * */
    @GET(AppConstants.Endpoint.apiLatestClip)
    fun callLatestClips(
        @HeaderMap header: Map<String, String>,
        @Query("page") page: String
    ): Call<JsonObject?>
    /**
     * send Invition to add my  Live
     **/
    @Multipart
    @POST(AppConstants.Endpoint.apiStreamInvite)
    fun callStreamersInvitation(
        @HeaderMap headers: Map<String, String>,
        @Part("streaming_id") streaming_id: RequestBody?,
        @Part("message") message: RequestBody?,
        @Part("invitee_id") invitee_id: RequestBody?,

    ): Call<JsonObject?>

    @POST(AppConstants.Endpoint.apiStreamAccept)
    fun callAcceptInvitation(@HeaderMap headers: Map<String, String>, @Body jsonObject: JsonObject?):Call<JsonObject?>

    /**
     * Search reject Invetatn stremers
     *
     **/
    @POST(AppConstants.Endpoint.apiStreamReject)
    fun callRejectStreame(
        @HeaderMap header: Map<String, String>,
        @Body jsonObject: JsonObject?): Call<JsonObject?>

    /**
     * Request for Get all gifts
     * */
    @GET(AppConstants.Endpoint.giftFetch)
    fun callFetchGifts(@HeaderMap headers: Map<String, String>): Call<JsonObject?>

    /**
     * This Calling For All notification (Home Screen)
     * */
    @GET(AppConstants.Endpoint.likesAndComments)
    fun callAllNotification(@HeaderMap header: Map<String, String>): Call<JsonObject?>

    /**
     * This Calling For Main Leader Board
     * */
    @Multipart
    @POST(AppConstants.Endpoint.leaderBoard)
    fun callMainLeaderBoard(@HeaderMap header: Map<String, String>, @Part("fetch") fetch: RequestBody?): Call<JsonObject?>

    @Multipart
    @POST(AppConstants.Endpoint.giftSend)
    fun callSendGift(
        @HeaderMap header: Map<String, String>,
        @Part("streaming_id") streamingId: RequestBody?,
        @Part("gift_id") giftId: RequestBody?
    ): Call<JsonObject?>

    @GET(AppConstants.Endpoint.subscriptionPlanList)
    fun callSubscriptionPlanList(@HeaderMap header: Map<String, String>): Call<JsonObject?>

    @Multipart
    @POST(AppConstants.Endpoint.subscriptionPlanBuy)
    fun callSubscriptionPlanBuy(
        @HeaderMap header: Map<String, String>,
        @Path("plainId") plainId: String?,
        @Part("price") price: RequestBody?,
        @Part("currency") currency: RequestBody?,
        @Part("coins") coins: RequestBody?,
        @Part("CardNo") CardNo: RequestBody?,
        @Part("Expire") Expire: RequestBody?,
        @Part("cardholdername") cardHolderName: RequestBody?,
        @Part("email") email: RequestBody?
    ): Call<JsonObject?>

    @GET(AppConstants.Endpoint.myCoins)
    fun callMyCoins(@HeaderMap header: Map<String, String>): Call<JsonObject?>

    @Multipart
    @POST(AppConstants.Endpoint.resetPassword)
    fun callResetPassword(
        @HeaderMap header: Map<String, String>,
        @Part("email") email: RequestBody?
    ): Call<JsonObject?>

    @Multipart
    @POST(AppConstants.Endpoint.usersLeaderBoard)
    fun callUserLeaderBoard(
        @HeaderMap header: Map<String, String>,
        @Part("fetch") fetch: RequestBody?
    ): Call<JsonObject?>

    @Multipart
    @POST(AppConstants.Endpoint.otherUsersLeaderBoard)
    fun callOtherUsersLeaderBoard(
        @HeaderMap header: Map<String, String>,
        @Part("fetch") fetch: RequestBody?,
        @Part("user_id") userId: RequestBody?
    ): Call<JsonObject?>

    @Multipart
    @POST(AppConstants.Endpoint.cheeringLive)
    fun callCheeringLive(
        @HeaderMap header: Map<String, String>,
        @Part("fetch") fetch: RequestBody?
    ): Call<JsonObject?>

    @POST(AppConstants.Endpoint.cheeringAnonymous)
    fun callCheeringAnonymous(@HeaderMap header: Map<String, String>): Call<JsonObject?>

    @Multipart
    @POST(AppConstants.Endpoint.rejectInvitation)
    fun callRejectInvitation(
        @HeaderMap header: Map<String, String>,
        @Part("streaming_id") streamingId: RequestBody?
    ): Call<JsonObject?>
    @Multipart
    @POST(AppConstants.Endpoint.acceptInvitation)
    fun callAcceptInvitation(
        @HeaderMap header: Map<String, String>,
        @Part("streaming_id") streamingId: RequestBody?
    ): Call<JsonObject?>

    @GET(AppConstants.Endpoint.postDelete)
    fun callDeletePost(
        @HeaderMap header: Map<String, String>,
        @Path("post_id") postId: String?
    ): Call<JsonObject?>

    @GET(AppConstants.Endpoint.clipDelete)
    fun callDeleteClip(
        @HeaderMap header: Map<String, String>,
        @Path("clip_id") postId: String?
    ): Call<JsonObject?>
}