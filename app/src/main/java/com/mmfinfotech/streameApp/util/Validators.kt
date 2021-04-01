package com.mmfinfotech.streameApp.util

import android.content.Context
import android.text.TextUtils
import android.widget.CheckBox
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import com.mmfinfotech.streameApp.R
import java.util.regex.Matcher
import java.util.regex.Pattern

fun validateEmail(ctx: Context?, data: Any): Boolean? {
    val tempEmail: String? = getText(data)

    if (TextUtils.isEmpty(tempEmail)) {
        setError(
            data,
            ctx?.resources?.getString(R.string.edt_error_field_required)
        )
        return false
    } else {
        val emailPattern: String? = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@" +
                "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" +
                "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\." +
                "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" +
                "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|" +
                "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"

        val pattern: Pattern = Pattern.compile(emailPattern!!, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(tempEmail!!)
        return if (!matcher.matches()) {
            setError(data, ctx?.getString(R.string.enter_valid_email_address))
            false
        } else true
    }
}

/**
 * Checks if the name is valid.
 * @param data - can be EditText or String
 * @return - true if the email is valid.
 */
fun validatePassword(ctx: Context?, data: Any): Boolean? {
    val tempPassword: String? = getText(data)

    return if (TextUtils.isEmpty(tempPassword)) {
        setError(
            data,
            ctx?.resources?.getString(R.string.edt_error_field_required)
        )
        false
    } else if (tempPassword?.length!! < 6) {
        setError(data, ctx?.getString(R.string.password_must_have_characters))
        false
    } else true
}

/**
 * check the entered password are same
 * @param data1 - can be EditText or String of password
 * @param data2 - can be EditText or String of confirm password
 */
fun validatePasswordMatcher(ctx: Context?, data1: Any, data2: Any): Boolean? {
    val tempPassword1: String? = getText(data1)
    val tempPassword2: String? = getText(data2)

    return if (!tempPassword1.equals(tempPassword2, ignoreCase = true)) {
        setError(data1, ctx?.resources?.getString(R.string.edt_error_password_miss_match))
        setError(data2, ctx?.resources?.getString(R.string.edt_error_password_miss_match))
        false
    } else true
}

fun validateOtp(ctx: Context?, data: Any): Boolean? {
    val tempOtp: String? = getText(data)
    return when {
        TextUtils.isEmpty(tempOtp) -> {
            setError(data, ctx?.resources?.getString(R.string.edt_error_field_required))
            false
        }
        tempOtp?.length != 6 -> {
            setError(data, ctx?.resources?.getString(R.string.edt_error_otp_length_is_incorrect))
            false
        }
        else -> true
    }
}

fun validateMobileNumber(ctx: Context?, data: Any?): Boolean? {
    val tempMObileNumber: String? = getText(data)

    if (TextUtils.isEmpty(tempMObileNumber)) {
        setError(data, ctx?.resources?.getString(R.string.edt_error_field_required))
        return false
    } else {
        val emailPattern: String? = "^[0-9]{10,13}$"

        val pattern: Pattern = Pattern.compile(emailPattern!!, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(tempMObileNumber!!)
        return if (!matcher.matches()) {
            setError(data, ctx?.getString(R.string.enter_valid_mobile_number))
            false
        } else true
    }
}

fun validateCheckBox(ctx: Context?, data: Any?): Boolean? {
    return if (data is CheckBox) {
        if (!data.isChecked) {
            setError(data, "Please scheckl")
            false
        } else true
    } else false
}

fun validateFirstName(ctx: Context?, data: Any?): Boolean? {
    val tempFirstName: String? = getText(data)

    if (TextUtils.isEmpty(tempFirstName)) {
        setError(data, ctx?.resources?.getString(R.string.edt_error_field_required))
        return false
    } else {
        val emailPattern: String? = "(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$"

        val pattern: Pattern = Pattern.compile(emailPattern!!, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(tempFirstName!!)
        return if (!matcher.matches()) {
            setError(data, ctx?.getString(R.string.enter_valid_name))
            false
        } else true
    }
}

fun validateLastName(ctx: Context?, data: Any?): Boolean? {
    val tempLastName: String? = getText(data)

    return if (TextUtils.isEmpty(tempLastName)) {
        setError(data, ctx?.resources?.getString(R.string.edt_error_field_required))
        false
    } else {
        val emailPattern: String? = "(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$"

        val pattern: Pattern = Pattern.compile(emailPattern!!, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(tempLastName!!)
        return if (!matcher.matches()) {
            setError(data, ctx?.getString(R.string.enter_valid_name))
            false
        } else true
    }
}

fun validatePrice(ctx: Context?, data: Any?): Boolean? {
    val tempPrice: String? = getText(data)

    if (TextUtils.isEmpty(tempPrice)) {
        setError(data, ctx?.resources?.getString(R.string.edt_error_field_required))
        return false
    } else {
        val pricePattern: String? = "^\\d{1,8}(?:\\.\\d{1,4})?$"//"[0-9]+([,.][0-9]{1,2})?"

        val pattern: Pattern = Pattern.compile(pricePattern!!, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(tempPrice!!)
        return if (!matcher.matches()) {
            setError(data, ctx?.getString(R.string.enter_validp_rice_number))
            false
        } else true
    }
}
/**
 * validation Empty edit text
 * @param context - context of  data
 * @param data - can be EditText or String  to be check
 * */
fun validationEmptyField(context: Context?, data: Any?): Boolean? {
    val tempText: String? = getText(data)

    return if (TextUtils.isEmpty(tempText)) {
        setError(data, context?.resources?.getString(R.string.edt_error_field_required))
        false
    } else true

}

/**
 * Retrieve string data from the parameter.
 * @param data - can be EditText or String
 * @return - String extracted from EditText or data if its data type is Strin.
 */
private fun getText(data: Any?): String {
    var str = ""
    if (data is EditText) {
        str = data.text.toString()
    } else if (data is String) {
        str = data
    }
    return str.trim()
}

/**
 * Sets error on EditText or TextInputLayout of the EditText.
 * @param data - Should be EditText
 * @param error - Message to be shown as error, can be null if no error is to be set
 */
private fun setError(data: Any?, error: String?) {
    if (data is EditText) {
        if (data.parent.parent is TextInputLayout) {
            (data.parent.parent as TextInputLayout).error = error
        } else {
            data.error = error
        }
    } else if (data is CheckBox) {
        data.error = error
    }
}
