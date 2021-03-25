package com.zelyder.stocksapp.data.network

import com.zelyder.stocksapp.data.FINNHUB_API_KEY
import com.zelyder.stocksapp.data.FINNHUB_SECRET
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class WebServicesProvider {

    private var _webSocket: WebSocket? = null

    private val socketOkHttpClient = OkHttpClient.Builder()
        .readTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .hostnameVerifier { _, _ -> true }
        .build()

    private var _webSocketListener: PriceWebSocketListener? = null

    fun startSocket(ticker: String): Channel<SocketUpdate> =
        with(PriceWebSocketListener(ticker)) {
            startSocket(this)
            this@with.socketEventChannel
        }

    fun startSocket(webSocketListener: PriceWebSocketListener) {
        _webSocketListener = webSocketListener
        _webSocket = socketOkHttpClient.newWebSocket(
            Request.Builder().url("wss://ws.finnhub.io?token=$FINNHUB_API_KEY").build(),
            webSocketListener
        )
        _webSocket?.send("{\"type\":\"subscribe\",\"symbol\":\"${webSocketListener.ticker}\"}")
        socketOkHttpClient.dispatcher.executorService.shutdown()

    }

    fun stopSocket() {
        try {
            _webSocket?.close(NORMAL_CLOSURE_STATUS, null)
            _webSocket = null
            _webSocketListener?.socketEventChannel?.close()
            _webSocketListener = null
        } catch (ex: Exception) {
        }
    }

    companion object {
        const val NORMAL_CLOSURE_STATUS = 1000
    }

}