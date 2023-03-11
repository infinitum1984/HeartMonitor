package com.empty.heartmonitor.messaging

import com.empty.heartmonitor.messaging.net.MessagingService
import com.empty.heartmonitor.messaging.net.model.Message
import com.empty.heartmonitor.messaging.net.model.Notification

interface MessagingManager {

    suspend fun sendMessage(token: String, title: String, text: String)

}

class MessagingManagerBase(private val netService: MessagingService) : MessagingManager {
    override suspend fun sendMessage(token: String, title: String, text: String) {
        netService.sendMessage(Message(token, Notification(title, text)))
    }
}