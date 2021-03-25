package com.zelyder.stocksapp.data.network

import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class PriceWebSocketListener(): WebSocketListener() {


    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
//        webSocket.send("type\":\"subscribe\",\"symbol\":\"AAPL\"}")
//        webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !")
        Log.d(this::class.simpleName, "onOpen: $response")

    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Log.d(this::class.simpleName, "Receive: $text")
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)
        Log.d(this::class.simpleName, "Receive bytes: $bytes")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        Log.d(this::class.simpleName, "Closing: $code / $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        Log.d(this::class.simpleName, "Failure: ${t.message}")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        Log.d(this::class.simpleName, "Closed: $code / $reason")
    }
}

private const val NORMAL_CLOSURE_STATUS = 1000