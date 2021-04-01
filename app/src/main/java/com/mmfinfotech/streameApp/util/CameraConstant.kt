package com.mmfinfotech.streameApp.util

import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.controls.Grid

val defaultFlash: Flash = Flash.OFF
val defaultGrid: Grid = Grid.OFF
const val defaultFlashIndex: Int = 0
const val defaultGridIndex: Int = 0
const val defaultLimit: Int = 5

fun getFlashArrayString(): ArrayList<String?>? {
    return ArrayList<String?>().apply {
        add("Off")
        add("On")
        add("Auto")
    }
}

fun getFlash(index: Int?): Flash? {
    return when (index) {
        0 -> Flash.OFF
        1 -> Flash.ON
        2 -> Flash.AUTO
        else -> defaultFlash
    }
}