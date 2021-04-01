package com.mmfinfotech.streameApp.utils

import android.os.AsyncTask
import android.util.Log
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.translate.Translate
import java.util.*

class GoogleTranslate : AsyncTask<String, Void, String>() {

    /*
     * Your Google API Key here
     */

    /*
     * Your Google API Key here
     */
    private val API_KEY = "AIzaSyAaBRtLk65Wm_7dmTvPtk8FLfKjQYrMHis"
    override fun doInBackground(vararg params: String?): String {


        /*
         *  The text which will be translated
         */
        val textToTranslate = params[0]!!

        /*
         * The source language to be translated
         */


        /*
         * The source language to be translated
         */
        val SOURCE_LANGUAGE = params[1]!!

        /*
         * The wished language to be translated to
         */


        /*
         * The wished language to be translated to
         */
        val TARGET_LANGUAGE = params[2]!!

        return try {

            /*
             * Objects needed for the translate object
             */
            val netHttpTransport = NetHttpTransport()
            val jacksonFactory = JacksonFactory()

            /*
             * Creating the Google Translate object
             */
            val translate = Translate.Builder(netHttpTransport, jacksonFactory, null).build()

            /*
             * Setting the textToTranslate, the API_KEY and TARGET_LANGUAGE
             */
            val listToTranslate = translate.Translations().list(
                    Arrays.asList(textToTranslate), TARGET_LANGUAGE).setKey(API_KEY)

            /*
             * If you want to let Google detects the language automatically, remove the next line
             * This line set the source language of the translated text
             */listToTranslate.source = SOURCE_LANGUAGE

            /*
             * Executing the translation and saving the response in the response object
             */
            val response = listToTranslate.execute()

            /*
             * The response has the form of: {"translatedText":"blabla"}
             * We need only the translated text between the second double quotes pair
             * therefore using getTranslatedText
             */response.translations[0].translatedText
        } catch (e: Exception) {
            Log.e("Google Response ", e.message ?: "")

            /*
             * I would return empty string if there is an error
             * to let the method which invoked the translating method know that there is an error
             * and subsequently it deals with it
             */""
        }
    }
}