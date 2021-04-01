package com.mmfinfotech.streameApp.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.widget.DatePicker
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mmfinfotech.streameApp.BuildConfig
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.utils.AppConstants
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("HardwareIds")
fun getDeviceId(context: Context?): String? {
    return Settings.Secure.getString(context?.contentResolver, Settings.Secure.ANDROID_ID)
}

/**
 * @param ctx Context of the activity
 * @return value of country iso code from the device.
 */
fun getIsoCountryCode(ctx: Context?): String? {
    var locale: String? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            ctx?.resources?.configuration?.locales?.get(0)?.country
        else
            ctx?.resources?.configuration?.locale?.country
    if (TextUtils.isEmpty(locale)) locale = "US"
    return locale
}

/**
 * DatePicker
 * @param context instance of the class
 * @param textView view to set the data
 * */
fun getDate(context: Context?, textView: TextView?): String? {
    var Date: String? = null
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val datePickerDialog = DatePickerDialog(context!!, object : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            Date = "$dayOfMonth - ${month.plus(1)} - $year"
            textView?.text = Date
        }
    }, year, month, day)
    datePickerDialog.show()
    return Date
}

/**
 * @param context instance of the class
 * @param permission premission to be taken from the user
 * @return boolean that use has already given the permission or not.
 */
fun checkHasPermission(context: Context?, permission: String?): Boolean? {
    var ret: Boolean? = false
    // If android sdk version is bigger than 23 the need to check run time permission.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        // return phone read contacts permission grant status.
        val hasPremission = ContextCompat.checkSelfPermission(context!!, permission!!)
        // If permission is granted then return true.
        if (hasPremission == PackageManager.PERMISSION_GRANTED) {
            ret = true
        }
    } else ret = true
    return ret
}

/***
 * @param activity activity from which the function to be called.
 * @param permission  premission to be taken from the user
 * @param requestCode to handle the result of the permission requested.
 */
// Request a runtime permission to app user.
fun requestPermission(activity: Activity?, permission: String?, requestCode: Int?) {
    val requestPermissionArray = arrayOf<String?>(permission)
    ActivityCompat.requestPermissions(activity!!, requestPermissionArray, requestCode!!)
}

/***
 * @param activity activity from which the function to be called.
 * @param permission  premission to be taken from the user
 * @param requestCode to handle the result of the permission requested.
 */
// Request a runtime permission to app user.
fun requestMultiplePermissions(
    activity: Activity?,
    requestPermissionArray: Array<String?>,
    requestCode: Int?
) {
    ActivityCompat.requestPermissions(activity!!, requestPermissionArray, requestCode!!)
}

fun getRealPathFromURI(context: Context?, uri: Uri?): String {
    var path = ""
    if (context?.contentResolver != null) {
        val cursor = context.contentResolver.query(uri!!, null, null, null, null)
        cursor?.moveToFirst()
        val index = cursor?.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        path = cursor!!.getString(index ?: 0)
        cursor.close()
    }
    return path
}

/**
 *
 * @return current Date from Calendar in dd/MM/yyyy format
 * @param cal adding 1 into month because Calendar month starts from zero
 */
fun getDateAndMonthe(DayAfter: Int?): String? {
    val cal: Calendar = Calendar.getInstance()
    cal.add(Calendar.DATE, DayAfter ?: 0)
//     return "" + cal.get(Calendar.DATE) +"/" +
//             (cal.get(Calendar.MONTH)+1) +"/"+(cal.get(Calendar.YEAR))
    return "" + cal.get(Calendar.DATE) + "/" +
            (cal.get(Calendar.MONTH) + 1)
}

/**
 *
 * @return current Date from Calendar in dd/MM/yyyy format
 * @param cal adding 1 into month because Calendar month starts from zero
 */
fun getSendDate(DayAfter: Int?): String? {
    val cal: Calendar = Calendar.getInstance()
    cal.add(Calendar.DATE, DayAfter ?: 0)
    return "" + cal.get(Calendar.DATE) + "-" +
            (cal.get(Calendar.MONTH) + 1) + "-" + (cal.get(Calendar.YEAR))
}

/**
 * @return current Date from Calendar in dd/MM/yyyy format
 * @param cal adding 1 into month because Calendar month starts from zero
 *
 */
fun getweek(context: Context?, DayAfter: Int?): String? {
    val cal: Calendar = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_WEEK, DayAfter ?: 0)
    val weekDayNo = cal.get(Calendar.DAY_OF_WEEK)
    return when (weekDayNo) {
        1 -> context?.getString(R.string.txt_sunday) // "(Sun)"
        2 -> context?.getString(R.string.txt_monday) // "(Mon)"
        3 -> context?.getString(R.string.txt_tuesday) // "(Tue)"
        4 -> context?.getString(R.string.txt_wednesday) // "(Wed)"
        5 -> context?.getString(R.string.txt_thursday) // "(Thu)"
        6 -> context?.getString(R.string.txt_friday) // "(Fri)"
        7 -> context?.getString(R.string.txt_saturday) // "(Sat)"
        else -> context?.getString(R.string.defaultString) // "()"
    }.toString()
}

/**
 * this method is use to change the Draowable Color
 * @param textView view of drawable situated
 * @param color colore to be set
 *
 * */
fun setTextViewDrawableColor(textView: TextView, color: Int) {
    for (drawable in textView.compoundDrawables) {
        if (drawable != null) {
            drawable.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(textView.context, color), PorterDuff.Mode.SRC_IN)
        }
    }
}

/**
 * Function to get the app folder as a file
 * @param isVideo if video is saving then its generate the file in video folder.
 * @return file of video /image.
 **/
fun getAppFolder(): File? {
    val appFolder = File("${Environment.getExternalStorageDirectory()}${AppConstants.FolderName.appFolder}")
    if (!appFolder.exists()) appFolder.mkdirs()
    return appFolder
}

/**
 * this function used for  get the time duration
 *
 * @param context=  instance of activity
 * @param selectedVideoFile = selected file  to check the duratn
 * @return videoDuration= get the duration from the file throught the  MediaMetadataRetriever.
 * */
fun getVideoDuration(context: Context, selectedVideoFile: File): Long {
    var videoDuration = java.lang.Long.MAX_VALUE
    try {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, Uri.fromFile(selectedVideoFile))
        val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        videoDuration = java.lang.Long.parseLong(time)


        retriever.release()
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
    } catch (e: SecurityException) {
        e.printStackTrace()
    }

    return videoDuration
}


/**
 * get current time
 * */
fun getFormattedDate(context: Context?, commentTime: Long?): String? {
    Log.v(Function::class.java.simpleName, "getFormattedDate $commentTime")
    val smsTime: Calendar = Calendar.getInstance()
    smsTime.timeInMillis = commentTime!!
    val now: Calendar = Calendar.getInstance()

    return when {
        now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) -> {
            val dateFormat: DateFormat = SimpleDateFormat(BuildConfig.CommentDateTimeFormat, Locale.getDefault())
//            "Today" //+ dateFormat.format(commentTime)
            context?.getString(R.string.today)
        }
        now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1 -> {
            val dateFormat: DateFormat = SimpleDateFormat(BuildConfig.CommentDateTimeFormat, Locale.getDefault())
//            "Yesterday" //+ dateFormat.format(commentTime)
            context?.getString(R.string.yesterday)
        }
        now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR) -> {
            val dateFormat: DateFormat = SimpleDateFormat(BuildConfig.CommentDateTimeFormat, Locale.getDefault())
            dateFormat.format(commentTime)
        }
        else -> {
            val dateFormat: DateFormat = SimpleDateFormat(BuildConfig.CommentDateTimeFormat, Locale.getDefault())
            dateFormat.format(commentTime)
        }
    }
}
