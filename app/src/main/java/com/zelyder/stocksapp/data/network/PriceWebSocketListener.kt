package com.zelyder.stocksapp.data.network

import android.util.Log
import com.zelyder.stocksapp.data.network.dto.finnhub.LastPriceDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class PriceWebSocketListener(val ticker: String): WebSocketListener() {

    val socketEventChannel: Channel<SocketUpdate> = Channel(10)

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        //webSocket.send("{\"type\":\"subscribe\",\"symbol\":\"$ticker\"}")
        Log.d(this::class.simpleName, "onOpen: $response")

    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Log.d(this::class.simpleName, "Receive: $text")
        GlobalScope.launch {
            socketEventChannel.send(SocketUpdate(text))
        }
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)
        Log.d(this::class.simpleName, "Receive bytes: $bytes")
    }

    @ExperimentalCoroutinesApi
    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        Log.d(this::class.simpleName, "Closing: $code / $reason")
        if(!socketEventChannel.isClosedForSend){
            GlobalScope.launch {
                socketEventChannel.send(SocketUpdate(exception = SocketAbortedException()))
            }
            webSocket.close(NORMAL_CLOSURE_STATUS, null)
            socketEventChannel.close()
        }
    }

    @ExperimentalCoroutinesApi
    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        Log.d(this::class.simpleName, "Failure: ${t.message}")
        if(!socketEventChannel.isClosedForSend){
            GlobalScope.launch {
                socketEventChannel.send(SocketUpdate(exception = t))
            }
        }
    }

}

class SocketAbortedException : Exception()

data class SocketUpdate(
    val text: String? = null,
    val byteString: ByteString? = null,
    val exception: Throwable? = null
)

private const val NORMAL_CLOSURE_STATUS = 1000