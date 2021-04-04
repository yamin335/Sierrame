package com.mmfinfotech.streameApp

import com.mmfinfotech.streameApp.dashBoard.activity.CommentsActivity
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by Somil Rawal on Fri-18-September-2020.
 */

//inline fun higherfunc( str : String, mycall :(String)-> Unit){
//    //inovkes the print() by passing the string str
//    mycall(str)
//}

class Test : ReadWriteProperty<CommentsActivity, Int> {
    override fun setValue(thisRef: CommentsActivity, property: KProperty<*>, value: Int) {

    }

    override fun getValue(thisRef: CommentsActivity, property: KProperty<*>): Int {
        return 45
    }
}