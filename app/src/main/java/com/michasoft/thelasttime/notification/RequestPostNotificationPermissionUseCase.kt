package com.michasoft.thelasttime.notification

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RequestPostNotificationPermissionUseCase @Inject constructor() {
    private lateinit var requestPermissions: ActivityResultLauncher<Array<String>>

    private var callback: ((result: Map<String, Boolean>) -> Unit)? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val permission = Manifest.permission.POST_NOTIFICATIONS

    @SuppressLint("InlinedApi")
    suspend fun execute(lifecycle: Lifecycle, registry: ActivityResultRegistry): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return true
        }
        return suspendCoroutine<Boolean> { continuation ->
            callback = { permissions ->
                continuation.resume(
                    permissions.containsKey(permission)
                            && permissions[permission] == true
                )
            }
            lifecycle.addObserver(MyLifecycleObserver(registry, lifecycle))
        }
    }

    inner class MyLifecycleObserver(
        private val registry: ActivityResultRegistry,
        private val lifecycle: Lifecycle
    ) : DefaultLifecycleObserver {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onCreate(owner: LifecycleOwner) {
            requestPermissions = registry.register(
                "RequestPostNotificationPermissionUseCase",
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                requestPermissions.unregister()
                callback?.invoke(permissions)
            }
            requestPermissions.launch(
                arrayOf(
                    permission,
                )
            )
            lifecycle.removeObserver(this)
        }
    }
}