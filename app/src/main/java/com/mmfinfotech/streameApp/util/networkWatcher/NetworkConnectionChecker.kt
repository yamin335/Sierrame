package com.mmfinfotech.streameApp.util.networkWatcher

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import java.util.concurrent.CopyOnWriteArraySet

/**
 * Created by Somil Rawal on 25-09-2019.
 */

class NetworkConnectionChecker(context: Application) {
    private val connectivityManager: ConnectivityManager
    private val listeners: MutableSet<OnConnectivityChangedListener> =
        CopyOnWriteArraySet()

    fun registerListener(listener: OnConnectivityChangedListener) {
        listeners.add(listener)
        listener.connectivityChanged(isConnectedNow())
    }

    fun unregisterListener(listener: OnConnectivityChangedListener) {
        listeners.remove(listener)
    }

    fun isConnectedNow(): Boolean? {
        val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    interface OnConnectivityChangedListener {
        fun connectivityChanged(availableNow: Boolean?)
    }

    private inner class NetworkStateReceiver : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            val isConnectedNow = isConnectedNow()
            for (listener in listeners) {
                listener.connectivityChanged(isConnectedNow)
            }
        }
    }

    init {
        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val intentFilter =
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(NetworkStateReceiver(), intentFilter)
    }
}