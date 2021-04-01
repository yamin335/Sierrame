package com.mmfinfotech.streameApp.baseActivity

import com.mmfinfotech.streameApp.agora.base.BaseActivity

open class DashBoardBaseActivity : BaseActivity() {
    /*private val tag: String? = DashBoardBaseActivity::class.java.simpleName

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        Log.v(tag, "onWindowFocusChanged $hasFocus")
        hideSystemUI()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.v(tag, "onConfigurationChanged $newConfig")

        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO){
            Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
        }else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES){
            Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
        }

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show()
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show()
        }
    }*/
}