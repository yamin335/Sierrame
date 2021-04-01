package com.mmfinfotech.streameApp.ui


import android.content.Context
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.mmfinfotech.streameApp.R


/**
 * Created by Somil Rawal on Sat-05-September-2020.
 */
class MessageInputLayout : ConstraintLayout, View.OnClickListener {
    private val TAG = MessageInputLayout::class.java.simpleName
     private var editText: EditText? = null
    private var addAttachment: ImageButton? = null
    private var addCamera: ImageButton? = null
    private var addEmojKeyboard: ImageButton? = null
    private var sendMessage: ImageButton? = null
    private var countDownTimer: CountDownTimer? = null
    private var isTyping = false
    private var onTypingListener: OnTypingListener? = null
    private var onMessageInputLayoutListener: OnMessageInputLayoutListener? = null
    private var translationBoolean: Boolean? = null
    private var destinationLanguage:String?=null

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
        addView(inflate(context, R.layout.partial_message_input, null).apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            editText = findViewById(R.id.messageInputLayout_Message)
            addAttachment = findViewById(R.id.messageInputLayout_Attachment)
            addCamera = findViewById(R.id.messageInputLayout_camera)
            sendMessage = findViewById(R.id.messageInputLayout_send)
            addEmojKeyboard = findViewById(R.id.messageInputLayout_emojikeyboard)
        })

        countDownTimer = object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                if (onTypingListener != null) onTypingListener?.onTyping(false)
                isTyping = false
            }
        }

        editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!isTyping) {
                    if (onTypingListener != null) onTypingListener?.onTyping(true)
                }
                if (isTyping) countDownTimer?.cancel()
                countDownTimer?.start()
                isTyping = true
                if (s.isEmpty()) {
                    addAttachment?.visibility = VISIBLE
                    addCamera?.visibility = VISIBLE
                    sendMessage?.visibility = GONE
                } else {
                    addAttachment?.visibility = GONE
                    addCamera?.visibility = GONE
                    sendMessage?.visibility = VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        if (translationBoolean == true) {

        }
        else {
        }
        addAttachment?.setOnClickListener(this@MessageInputLayout)
        addCamera?.setOnClickListener(this@MessageInputLayout)
        addEmojKeyboard?.setOnClickListener(this@MessageInputLayout)
        sendMessage?.setOnClickListener(this@MessageInputLayout)
        editText?.showSoftInputOnFocus
        invalidate()
    }

    fun getEditText(): EditText? {
        return editText
    }

    fun foucs() {
        editText?.requestFocus()
        val imm: InputMethodManager =
            this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    fun removefocus() {
        val imm: InputMethodManager =
            this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText?.windowToken, 0)
    }

//    fun EditText.showSoftKeyboard(){
//        (this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
//            .showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
//    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.messageInputLayout_Attachment -> onMessageInputLayoutListener?.onAttachmentClick()
            R.id.messageInputLayout_camera -> onMessageInputLayoutListener?.onCameraClick()
            R.id.messageInputLayout_emojikeyboard -> onMessageInputLayoutListener?.onEmojiKeyboardClick()
            R.id.messageInputLayout_send -> {
                Log.v(TAG, "On CLick send message text ${editText?.text}")
                if (editText?.text?.trim()?.isEmpty() == false) {
                    onMessageInputLayoutListener?.onSendMessageClick(
                        editText?.text.toString().trim()
                    )
                }
                editText?.text?.clear()
            }
            else -> {
            }
        }
    }

    fun setOnTypingListener(onTypingListener: OnTypingListener?) {
        this@MessageInputLayout.onTypingListener = onTypingListener
    }

    fun setOnMessageInputLayoutListener(
        onMessageInputLayoutListener: OnMessageInputLayoutListener?,
        translation: Boolean? = false, destinatnLanguage:String?=""
    ) {
        this@MessageInputLayout.onMessageInputLayoutListener = onMessageInputLayoutListener
        translationBoolean = translation
        destinationLanguage= destinatnLanguage
        Log.d("djkjh", "ktranslationBoolean $translationBoolean ")
        editText?.setText(destinationLanguage)
    }
}