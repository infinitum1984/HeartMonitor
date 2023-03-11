package com.empty.heartmonitor.messaging.net

import okhttp3.Interceptor
import okhttp3.Response

class AuthorithationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(chain.request().newBuilder().header("Authorization", "key=${token}").build())

}

private val token =
    "AAAAKmvYwak:APA91bETeU9DRHjDB8n9NfPdVv1AMlc82o0LyAxBs3uyk05XjAVT4sJwcIBxkZLQod3Za_7Pe9KMP_42Ky2PDJ11AsP8ErgrpYyT2GJgW9Yn0Gdue4KSqy7sqc7wzIKzhr6OyjO5oHfq"