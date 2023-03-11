package com.empty.heartmonitor.di

import com.empty.heartmonitor.messaging.MessagingManager
import com.empty.heartmonitor.messaging.MessagingManagerBase
import com.empty.heartmonitor.messaging.net.AuthorithationInterceptor
import com.empty.heartmonitor.messaging.net.MessagingService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val messagingModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com/fcm/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<MessagingService> {
        get<Retrofit>().create(MessagingService::class.java)
    }
    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(get<AuthorithationInterceptor>()).build()
    }
    single {
        AuthorithationInterceptor()
    }
    single<MessagingManager> {
        MessagingManagerBase(get())
    }

}