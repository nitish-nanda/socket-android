package com.app.socketexample

import android.app.Application
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class SocketManager : Application() {

    override fun onCreate() {
        super.onCreate()
        try {
            mSocket = IO.socket(Constants.SOCKET_URL)
            println("mSocket object created")
        } catch (e: URISyntaxException) {
            throw RuntimeException(e.localizedMessage)
        }
    }

    companion object {

        private var mSocket: Socket? = null

        fun getMSocket(): Socket? {
            return mSocket
        }
    }

}