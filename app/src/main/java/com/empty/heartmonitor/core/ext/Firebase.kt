package com.empty.heartmonitor.core.ext

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun FirebaseMessaging.fetchToken(): String =
    suspendCoroutine { cont ->
        this.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                cont.resumeWithException(task.exception ?: Exception("!task.isSuccessful"))
            }
            cont.resume(task.result)

        }).addOnFailureListener {
            cont.resumeWithException(it)
        }
    }