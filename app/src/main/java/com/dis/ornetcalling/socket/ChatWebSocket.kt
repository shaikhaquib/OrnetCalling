package com.dis.ornetcalling.socket

import com.dis.ornetcalling.architecture.MainViewModel
import com.dis.ornetcalling.network.model.LastMessage
import com.dis.ornetcalling.network.model.Message
import com.google.gson.Gson
import okhttp3.*
import okio.ByteString
import org.json.JSONObject

class ChatWebSocket(private val chatId: String, private val token: String, private val viewModel: MainViewModel) {

    private lateinit var webSocket: WebSocket

    fun connect() {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("ws://13.234.119.125/ws/normalchat/$chatId/")
            .addHeader("websocket-token", token)
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                println("WebSocket connection opened")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                println("Received message: $text")
               // {"id": "434161", "group": "My New Group", "user": "ornet@example.com", "message": "hi", "timestamp": "2024-07-08T12:33:54.994793Z"}
              viewModel.addMessage1on1(parseJsonToLastMessage(text))
            }

            fun parseJsonToLastMessage(json: String): LastMessage {
                val gson = Gson()
                return gson.fromJson(json, LastMessage::class.java)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                println("Received bytes: ${bytes.hex()}")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                println("WebSocket closing: $code / $reason")
                webSocket.close(1000, null)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                println("WebSocket failure: ${t.message}")
            }
        })
    }

    fun sendMessage(message: String) {
        val jsonMessage = """{"message": "$message"}"""
        webSocket.send(jsonMessage)
    }

    fun disconnect() {
        webSocket.close(1000, "Closing connection")
    }
}
/*

// Usage example
fun main() {
    val chatId = "180837"
    val token = "76b0d655ad9ae10bf2579d6e2c710ff3b726c02f"
    
    val chatWebSocket = GroupChatWebSocket(chatId, token)
    chatWebSocket.connect()
    
    // Send a test message
    chatWebSocket.sendMessage("test")
    
    // Keep the connection open for a while (you might want to handle this differently in a real app)
    Thread.sleep(5000)
    
    chatWebSocket.disconnect()
}*/
