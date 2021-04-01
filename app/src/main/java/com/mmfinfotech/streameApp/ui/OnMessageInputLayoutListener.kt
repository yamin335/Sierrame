package com.mmfinfotech.streameApp.ui

/**
 * Created by Somil Rawal on Mon-07-September-2020.
 */
interface OnMessageInputLayoutListener {
    fun onAttachmentClick()
    fun onCameraClick()
    fun onEmojiKeyboardClick()
    fun onSendMessageClick(message: String?)
}