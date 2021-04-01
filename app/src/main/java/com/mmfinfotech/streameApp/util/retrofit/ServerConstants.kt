package com.mmfinfotech.streameApp.util.retrofit

/**
 * Created by Somil Rawal on 21-09-2019.
 */

const val authtoken: String = "authtoken"
const val status: String = "status"
const val message: String = "message"
const val otpType: String = "otp_type"
const val otp: String = "otp"
const val record: String = "record"


const val userId: String = "id"
const val firstName: String = "first_name"
const val lastName: String = "last_name"
const val fullName: String = "full_name"
const val displayName: String = "display_name"
const val email: String = "email"
const val countryCode: String = "country_code"
const val phone: String = "phone"
const val userType: String = "user_type"
const val reference: String = "reference"
const val profileImage: String = "profile_img"
const val profileImageThumb: String = "profile_img_thumb"


const val Sccess = "200"
const val NotFound = "404" //=> not found
const val MethodNotAllowed = "405" //=> Method not allowed
const val TokenExpire = "408" //=> token expire
const val OtpExpire = "410" //=> Otp expire
const val Anauthorized = "401" //=> Anouthorized
const val ValidationError ="400" //=> mismatch (show direct message)
const val NotVerify = "203"// => Not verify
const val PerameterNotProper = "204"// => Perameter not proper(Special for social login)
const val AnotherDevice ="205"// => anouthe device


interface JsonKey {
    companion object {
        const val messageType = "mt"
        const val message = "m"
        const val profileImage = "pi"
        const val giftUrl = "g"
        const val name = "n"
    }
}