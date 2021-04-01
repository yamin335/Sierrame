package com.mmfinfotech.streameApp.ui.creditCard

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

/**
 * Created By Somil Rawal on Wed-30-12-2020-December-2020.
 */

class CreditCardExpireEditText : AppCompatEditText, TextWatcher {
    private val INITIAL_MONTH_ADD_ON = "0"
    private val DEFAULT_MONTH = "01"
    private val DEFAULT_YEAR = "01"
    private val SPACE = '/'
    private val mLength = 0
    private var mPreviousText = ""
    private var mIsChangedInside = false


    private var listener: Listener? = null

    constructor(context: Context) : super(context) {
        init(context, null, -1)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, -1)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        this.addTextChangedListener(this@CreditCardExpireEditText)
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun afterTextChanged(s: Editable?) {
        val isDeleteMode = mPreviousText.length > (s?.length ?: 0)
        if (isDeleteMode && !mIsChangedInside) {
//            Log.v("tag", "afterTextChanged length = ${s?.length}  modules = ${s?.length!! % 5} / ${(s?.length ?: 0 % 5) == 0}")
            // Remove spacing char at the end
            if ((s?.length ?: 0) > 0 && (s?.length ?: 0) == 3) {
                val c = s?.get(s.length - 1)
                if (SPACE == c) {
                    mIsChangedInside = true;
                    s.delete(s.length - 1, s.length)
                }
            }

            //remove char between
            val cardNumberParts = TextUtils.split(s.toString(), SPACE.toString())
            for (i in 0 until cardNumberParts.size) {
                val cardPart: String = cardNumberParts[i]
                if (cardPart.length > 4) {
                    val removedCharIndex: Int = (i * 5) + 3
                    mIsChangedInside = true
                    s?.delete(removedCharIndex, removedCharIndex + 1)
                }
            }
        } else {
            mIsChangedInside = false;
        }


        // Insert char where needed.
        for (i in 0 until (s?.length ?: 0)) {
            val c = s?.get(i)
            if ((i + 1) == 3) {
                if (Character.isDigit(c ?: "0"[0]) && TextUtils.split(s.toString(), SPACE.toString()).size <= 2) {
                    mIsChangedInside = true
                    s?.insert(i, SPACE.toString())
                }
            } else if (!Character.isDigit(c ?: "0"[0])) {
                mIsChangedInside = true
                s?.delete(i, (i + 1))
            }
        }
        mPreviousText = s.toString()

        listener?.onTextChange(text.toString())
    }

    override fun onTextContextMenuItem(id: Int): Boolean {
        val consumed: Boolean = super.onTextContextMenuItem(id)
        when (id) {
            android.R.id.cut -> {
            }
            android.R.id.copy -> {
            }
            android.R.id.paste -> {
            }
        }
        return consumed
    }

    private fun isCharValidMonth(charFromString: Char): Boolean {
        var month = 0
        if (Character.isDigit(charFromString)) {
            month = charFromString.toString().toInt()
        }
        return month <= 1
    }

    private fun isNumberChar(string: String): Boolean {
        return string.matches(Regex(".*\\d.*"))
    }

    fun getExpireDate() : String = mPreviousText.replace("/".toRegex(), "")

    fun setListener(listener: Listener?) {
        this.listener = listener
    }

    interface Listener{
        fun onTextChange(text: String?)
    }

}