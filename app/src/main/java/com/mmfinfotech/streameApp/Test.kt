package com.mmfinfotech.streameApp

/**
 * Created by Somil Rawal on Fri-18-September-2020.
 */

inline fun higherfunc( str : String, mycall :(String)-> Unit){
    //inovkes the print() by passing the string str
    mycall(str)
}

fun main() {

    var list = (1..20).toList()
    println("List $list")

    list = list.filter { it % 2 == 0 }
    println("List after filter $list")

    print("GeeskforGeeks: ")
    higherfunc("A Computer Science portal for Geeks", ::print)
}

//interface Function1<in P1, out R> : Function<R> {
//    public operator fun invoke(p1: P1): R
//}