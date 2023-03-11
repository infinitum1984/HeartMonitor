package com.empty.heartmonitor.messaging.net

import com.empty.heartmonitor.messaging.net.model.Message
import retrofit2.http.Body
import retrofit2.http.POST

interface MessagingService {

    @POST("send")
    suspend fun sendMessage(
        @Body
        message: Message
    )

}