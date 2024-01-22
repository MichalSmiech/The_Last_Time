package com.michasoft.thelasttime.permission

import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RequestPermissionUseCase @Inject constructor() {
    private lateinit var requestPermissions: ActivityResultLauncher<Array<String>>

    private var callback: ((result: Map<String, Boolean>) -> Unit)? = null

    @SuppressLint("InlinedApi")
    suspend fun execute(
        lifecycle: Lifecycle,
        registry: ActivityResultRegistry,
        permission: String
    ): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return true
        }
        return withContext(Dispatchers.Main) {
            suspendCoroutine { continuation ->
                callback = { permissions ->
                    continuation.resume(
                        permissions.containsKey(permission)
                                && permissions[permission] == true
                    )
                }
                lifecycle.addObserver(MyLifecycleObserver(registry, lifecycle, permission))
            }
        }
    }

    inner class MyLifecycleObserver(
        private val registry: ActivityResultRegistry,
        private val lifecycle: Lifecycle,
        private val permission: String
    ) : DefaultLifecycleObserver {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onCreate(owner: LifecycleOwner) {
            requestPermissions = registry.register(
                "RequestPermissionUseCase",
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

