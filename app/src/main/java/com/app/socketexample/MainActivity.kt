package com.app.socketexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.socketexample.databinding.ActivityMainBinding
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private val onSocketConnect = Emitter.Listener {
        println("Socket Connected")
        println("Socket is connected id -> ${mSocket?.id()}")
        connectUser()
    }

    private val onSocketDisconnect = Emitter.Listener {
        println("Socket Disconnected")
    }

    private val onSocketConnectError = Emitter.Listener { args ->
        run {
            println("Socket Connect Error")
            args?.isNotEmpty().let {
                if (it == true) {
                    val jsonObject: JSONObject = args[0] as JSONObject
                    println(jsonObject)
                }
            }
        }
    }

    private var listenerUserConnect = Emitter.Listener { args ->
        run {
            println("User connect")
            args?.isNotEmpty().let {
                if (it == true) {
                    val jsonObject: JSONObject = args[0] as JSONObject
                    println(jsonObject)
                }
            }
        }
    }

    private var mSocket: Socket? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSocket()
    }

    private fun connectUser() {
        if (mSocket?.connected() == true) {
            val jsonObject = JSONObject().apply {
                put("userId", "176")
            }
            mSocket?.run {
                emit(Constants.CONNECT_USER_EMITTER, jsonObject)
                off(Constants.CONNECT_USER_LISTENER, listenerUserConnect)
                on(Constants.CONNECT_USER_LISTENER, listenerUserConnect)
            }
        }
    }

    private fun initSocket() {
        mSocket = SocketManager.getMSocket()
        if (mSocket?.isActive == true) {
            println("Socket connection is active")
        } else {
            mSocket?.run {
                connect()
                on(Socket.EVENT_CONNECT, onSocketConnect)
                on(Socket.EVENT_DISCONNECT, onSocketDisconnect)
                on(Socket.EVENT_CONNECT_ERROR, onSocketConnectError)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket!!.disconnect()
        mSocket!!.off(Constants.CONNECT_USER_EMITTER, listenerUserConnect)
    }
}