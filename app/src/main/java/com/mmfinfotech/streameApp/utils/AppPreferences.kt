package com.mmfinfotech.streameApp.utils

import android.content.Context
import android.content.SharedPreferences
import com.mmfinfotech.streameApp.BuildConfig
import com.mmfinfotech.streameApp.util.ShowAlertInformation
import com.mmfinfotech.streameApp.util.defaultFlashIndex

class AppPreferences {
    private val prefConstAuthToken = "authToken"
    private val prefConstIsUserLoggedIn = "is_user_logged_in"
    private val prefConstIsAppIntroduced = "is_introduced"
    private val prefConstFCMToken = "fcm_token"
    private val prefConstRememberMe = "user_remember_me"
    private val prefConstUserStremeID = "user_Strem_id"
    private val prefConstUserID = "user_id"
    private val prefConstFirstName = "user_firstName"
    private val prefConstLastName = "user_lastName"
    private val prefConstFullName = "user_fullName"
    private val prefConstEmail = "user_email"
    private val prefConstPassword = "user_password"
    private val prefConstCountryCode = "user_countryCode"
    private val prefConstPhone = "user_phone"
    private val prefConstProfileImage = "user_profileImage"
    private val prefConstGander = "user_gander"
    private val prefConstBirthday = "user_birthday"
    private val prefConstWeb = "user_web"
    private val prefConstMultilineComment = "user_multiline_comment"
    private val prefConstComment = "user_comment"
    private val prefConstProfileImageThumb = "user_profileImageThumb"
    private val prefConstPhoneAuthenticate = "user_phone_authenticate"
    private val prefConstPrivateFollowList = "user_private_follow_list"
    private val prefConstSecretMode = "user_secret_mode"

    private val prefConstFlash = "flash"

    /**
     * @param context Instance of the Activity.
     * @return Object of SharedPreference for CRUD operation.
     * */
    private fun getSharedPreference(context: Context?): SharedPreferences? {
        return context?.getSharedPreferences("${BuildConfig.APPLICATION_ID}.preference", Context.MODE_PRIVATE)
    }

    /**
     * @param context Instance of the Activity.
     * To Clear the SharePreference data according to the remebere me option and also FCM Token is saved again.
     * */
    fun clearPreferences(context: Context?) {
        val remberme: Boolean? = getRememberMe(context)
        val fcmToken: String? = getFCMToken(context)
        if (remberme == true) {
//            val email = getEmail(context)
            val code = getCountryCode(context)
            val number = getPhone(context)
            val password = getPassword(context)
            clear(context)
            setRememberMe(context, true)
//            setEmail(context, email)
            setCountryCode(context, code)
            setPhone(context, number)
            setPassword(context, password)
        } else
            clear(context)

        setFCMToken(context, fcmToken)
    }

    /**
     * @param context Instance of the Activity.
     * Clear the SharedPreference Data Completely.
     * */
    private fun clear(context: Context?) {
        val editor = getSharedPreference(context)?.edit()
        editor?.clear()
        editor?.apply()
    }

    /**
     * @param context Instance of the Activity.
     * @return Object of SharedPreference for CRUD operation.
     * */
    private fun getSettingPreference(context: Context?): SharedPreferences? {
        return context?.getSharedPreferences(
            "${BuildConfig.APPLICATION_ID}.settings",
            Context.MODE_PRIVATE
        )
    }

    /**
     * @param context Instance of the Activity.
     * @param shouldRemember Boolean to remeber the User credentials for login again.
     * */
    fun setRememberMe(context: Context?, shouldRemember: Boolean?) {
        val editor = getSharedPreference(context)?.edit()
        editor?.putBoolean(prefConstRememberMe, shouldRemember!!)
        editor?.apply()
    }

    /**
     * @param context Instance of the Activity.
     * @return Boolean User has choosed to remeber the User credentials or not.
     * */
    fun getRememberMe(context: Context?): Boolean? {
        return getSharedPreference(context)?.getBoolean(prefConstRememberMe, AppConstants.Defaults.bool)
    }

    /**
     * @param context Instance of the Activity.
     * @param isUserLoggedIn Boolean to set user is loggedIn in or not.
     * */
    fun setUserLoggedIn(context: Context?, isUserLoggedIn: Boolean?) {
        val editor = getSharedPreference(context)?.edit()
        editor?.putBoolean(prefConstIsUserLoggedIn, isUserLoggedIn!!)
        editor?.apply()
    }

    /**
     * @param context Instance of the Activity.
     * @return Boolean User is loggedIn or not.
     * */
    fun isUserLoggedIn(context: Context?): Boolean? {
        return getSharedPreference(context)?.getBoolean(prefConstIsUserLoggedIn, AppConstants.Defaults.bool)
    }

    fun setIsAppIntroduced(context: Context?, isAppIntroduced: Boolean) {
        val editor = getSharedPreference(context)?.edit()
        editor?.putBoolean(prefConstIsAppIntroduced, isAppIntroduced)
        editor?.apply()
    }

    fun isAppIntroduced(context: Context?): Boolean? {
        return getSharedPreference(context)?.getBoolean(prefConstIsAppIntroduced, false)
    }

    /**
     * @param context Instance of the Activity.
     * @param authToken API authentication token.
     * */
    fun setAuthToken(context: Context?, authToken: String?) {
        val editor = getSharedPreference(context)?.edit()
        editor?.putString(prefConstAuthToken, authToken)
        editor?.apply()
    }

    /**
     * @param context Instance of the Activity.
     * @return String API authentication token.
     * */
    fun getAuthToken(context: Context?): String? {
        return getSharedPreference(context)?.getString(prefConstAuthToken, AppConstants.Defaults.string)
    }

    /**
     * @param context Instance of the Activity.
     * @param token FCM Token for push notifications.
     * */
    fun setFCMToken(context: Context?, token: String?) {
        val editor = getSharedPreference(context)?.edit()
        editor?.putString(prefConstFCMToken, token)
        editor?.apply()
    }

    /**
     * @param context Instance of the Activity.
     * @return String of FCM token for puch notification.
     * */
    fun getFCMToken(context: Context?): String? {
        val token = getSharedPreference(context)?.getString(prefConstFCMToken, AppConstants.Defaults.string)
//        if (token.equals(AppConstants.Defaults.string, ignoreCase = true))
//            ShowAlertInformation(context, "FCM Token Not Found. Please Inform")
        return token
    }

    /**
     * @param context Instance of the Activity.
     * @param id Save User streme Id Generated by Server.
     * */
    fun setStremeID(context: Context?, id: String?) {
        val editor = getSharedPreference(context)?.edit()
        editor?.putString(prefConstUserStremeID, id)
        editor?.apply()
    }

    /**
     * @param context Instance of the Activity.
     * @return String of User streme ID used to perform other oprations.
     * */
    fun getStremeId(context: Context?): String? {
        return getSharedPreference(context)?.getString(prefConstUserStremeID, AppConstants.Defaults.string)
    }

    /**
     * @param context Instance of the Activity.
     * @param id Save User Id Generated by Server.
     * */
    fun setUserID(context: Context?, id: String?) {
        val editor = getSharedPreference(context)?.edit()
        editor?.putString(prefConstUserID, id)
        editor?.apply()
    }

    /**
     * @param context Instance of the Activity.
     * @return String of User ID used to perform other oprations.
     * */
    fun getUserId(context: Context?): String? {
        return getSharedPreference(context)?.getString(prefConstUserID, AppConstants.Defaults.string)
    }

    /**
     * @param context Instance of the Activity.
     * @param firstName Save User First Name.
     * */
    fun setFirstName(context: Context?, firstName: String?) {
        val editor = getSharedPreference(context)?.edit()
        editor?.putString(prefConstFirstName, firstName)
        editor?.apply()
    }

    /**
     * @param context Instance of the Activity.
     * @return String of User First Name.
     * */
    fun getFirstName(context: Context?): String? {
        return getSharedPreference(context)?.getString(prefConstFirstName, AppConstants.Defaults.string)
    }

    /**
     * @param context Instance of the Activity.
     * @param lastName User Last Name.
     * */
    fun setLastName(context: Context?, lastName: String?) {
        val editor = getSharedPreference(context)?.edit()
        editor?.putString(prefConstLastName, lastName)
        editor?.apply()
    }

    /**
     * @param context Instance of the Activity.
     * @return String of User Last Name.
     * */
    fun getLastName(context: Context?): String? {
        return getSharedPreference(context)?.getString(prefConstLastName, AppConstants.Defaults.string)
    }

    fun setFullName(context: Context?, fullName: String?) {
        val editor = getSharedPreference(context)?.edit()
        editor?.putString(prefConstFullName, fullName)
        editor?.apply()
    }

    /**
     * @param context Instance of the Activity.
     * @return String of User Full Name.
     * */
    fun getFullName(context: Context?): String? {
        return getSharedPreference(context)?.getString(prefConstFullName, AppConstants.Defaults.string)
    }

    /**
     * @param context Instance of the Activity
     * @param gander the String Value of gander
     *
     * */

    fun setGander(context: Context?, gander: String?) {
        val editor = getSharedPreference(context)?.edit()
        editor?.putString(prefConstGander, gander)
        editor?.apply()
    }

    /**
     * @param context Instance of the Activity.
     * @return String of User gander.
     * */
    fun getGander(context: Context?): String? {
        return getSharedPreference(context)?.getString(prefConstGander, AppConstants.Defaults.string)
    }

    /**
     * @param context Instance of the Activity
     * @param comment the String Value of gander
     *
     * */

    fun setComment(context: Context?, comment: String?) {
        val editor = getSharedPreference(context)?.edit()
        editor?.putString(prefConstComment, comment)
        editor?.apply()
    }

    /**
     * @param context Instance of the Activity.
     * @return String of User gander.
     * */
    fun getComment(context: Context?): String? {
        return getSharedPreference(context)?.getString(prefConstComment, "")
    }

    /**
     * @param context Instance of the Activity
     * @param Web the String Value of gander
     *
     * */

    fun setWeb(context: Context?, Web: String?) {
        val editor = getSharedPreference(context)?.edit()
        editor?.putString(prefConstWeb, Web)
        editor?.apply()
    }

    /**
     * @param context Instance of the Activity.
     * @return String of User gander.
     * */
    fun getWeb(context: Context?): String? {
        return getSharedPreference(context)?.getString(prefConstWeb, "")
    }

    /**
     * @param context Instance of the Activity
     * @param multilineComment the String Value of gander
     *
     * */

    fun setMultilineComment(context: Context?, multilineComment: String?) {
        val editor = getSharedPreference(context)?.edit()
        editor?.putString(prefConstMultilineComment, multilineComment)
        editor?.apply()
    }

    /**
     * @param context Instance of the Activity.
     * @return String of User gander.
     * */
    fun getMultilineComment(context: Context?): String? {
        return getSharedPreference(context)?.getString(prefConstMultilineComment, "")
    }


    /**
     * @param context Instance of the Activity
     * @param birthDay the String Value of gander
     *
     * */

    fun setBirthDay(context: Context?, birthDay: String?) {
        val editor = getSharedPreference(context)?.edit()
        editor?.putString(prefConstBirthday, birthDay)
        editor?.apply()
    }

    /**
     * @param context Instance of the Activity.
     * @return String of User gander.
     * */
    fun getBirthDay(context: Context?): String? {
        return getSharedPreference(context)?.getString(prefConstBirthday, "")
    }

    fun setEmail(context: Context?, email: String?) {
        val editor = getSharedPreference(context)?.edit()
        editor?.putString(prefConstEmail, email)
        editor?.apply()
    }

    fun getEmail(context: Context?): String? {
        return getSharedPreference(context)?.getString(prefConstEmail, AppConstants.Defaults.string)
    }

    fun setPassword(context: Context?, password: String?) {
        val editor = getSharedPreference(context)?.edit()
        editor?.putString(prefConstPassword, password)
        editor?.apply()
    }

    fun getPassword(context: Context?): String? {
        return getSharedPreference(context)?.getString(prefConstPassword, "")
    }

    fun setCountryCode(context: Context?, countryCode: String?) {
        val editor = getSharedPreference(context)?.edit()
        editor?.putString(prefConstCountryCode, countryCode)
        editor?.apply()
    }

    fun getCountryCode(context: Context?): String? {
        return getSharedPreference(context)?.getString(prefConstCountryCode, AppConstants.Defaults.string)
    }

    fun setPhone(context: Context?, phone: String?) {
        val editor = getSharedPreference(context)?.edit()
        editor?.putString(prefConstPhone, phone)
        editor?.apply()
    }

    fun getPhone(context: Context?): String? {
        return getSharedPreference(context)?.getString(prefConstPhone, AppConstants.Defaults.string)
    }

    fun setProfileImage(context: Context?, profileImage: String?) {
        val editor = getSharedPreference(context)?.edit()
        editor?.putString(prefConstProfileImage, profileImage)
        editor?.apply()
    }

    fun getProfileImage(context: Context?): String? {
        return getSharedPreference(context)?.getString(prefConstProfileImage, AppConstants.Defaults.string)
    }

    fun setProfileImageThumb(context: Context?, profileImageThumb: String?) {
        val editor = getSharedPreference(context)?.edit()
        editor?.putString(prefConstProfileImageThumb, profileImageThumb)
        editor?.apply()
    }

    fun getProfileImageThumb(context: Context?): String? {
        return getSharedPreference(context)?.getString(prefConstProfileImageThumb, AppConstants.Defaults.string)
    }

    /**
     * This All Prefrences for Settings
     *
     * */
    /**
     * @param context = context of activity
     * @param index index of the selected item from the array.
     * @see com.mmfinfotech.inventory.dashBoard.settings.getFlashArrayString
     * */
    fun setFlashSetting(context: Context?, index: Int?) {
        val editor = getSettingPreference(context)?.edit()
        editor?.putInt(prefConstFlash, index ?: 0)
        editor?.apply()
    }

    /**
     * @param context = context of activity
     * @return index of the selected item from
     * @see com.mmfinfotech.inventory.dashBoard.settings.getFlashArrayString
     * */
    fun getFlashSetting(context: Context?): Int? {
        return getSettingPreference(context)?.getInt(
            prefConstFlash,
            defaultFlashIndex
        )
    }

    /**
     * @param context Instance of the Activity.
     * @param token FCM Token for push notifications.
     * */
    fun setFcmToken(context: Context?, token: String?) {
        val editor = getSharedPreference(context)?.edit()
        editor?.putString(prefConstFCMToken, token)
        editor?.apply()
    }

    /**
     * @param context Instance of the Activity.
     * @return String of FCM token for puch notification.
     * */
    fun getFcmToken(context: Context?): String? {
        val token = getSharedPreference(context)?.getString(prefConstFCMToken, AppConstants.Defaults.string)
        if (token.equals(AppConstants.Defaults.string, ignoreCase = true))
            ShowAlertInformation(context, "FCM Token Not Found. Please Inform")
        return token
    }

    fun setPhoneAuthentication(context: Context?, status: String?) {
        getSharedPreference(context)?.edit()?.apply {
            putString(prefConstPhoneAuthenticate, status)
            apply()
        }
    }

    fun getPhoneAuthentication(context: Context?): String? =
        getSharedPreference(context)?.getString(prefConstPhoneAuthenticate, AppConstants.Defaults.string)

    fun setPrivateFollowList(context: Context?, status: String?) {
        getSharedPreference(context)?.edit()?.apply {
            putString(prefConstPrivateFollowList, status)
            apply()
        }
    }

    fun getPrivateFollowList(context: Context?): String? =
        getSharedPreference(context)?.getString(prefConstPrivateFollowList, AppConstants.Defaults.string)

    fun setSecretMode(context: Context?, status: String?) {
        getSharedPreference(context)?.edit()?.apply {
            putString(prefConstSecretMode, status)
            apply()
        }
    }

    fun getSecretMode(context: Context?): String? =
        getSharedPreference(context)?.getString(prefConstSecretMode, AppConstants.Defaults.string)
}