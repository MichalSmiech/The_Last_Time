package com.michasoft.thelasttime.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.michasoft.thelasttime.LastTimeApplication
import com.michasoft.thelasttime.permission.CheckPermissionUseCase
import com.michasoft.thelasttime.permission.EnsurePostNotificationPermissionUseCase
import com.michasoft.thelasttime.permission.NeedRequestPostNotificationPermissionUseCase
import com.michasoft.thelasttime.permission.RequestPermissionActionBus
import com.michasoft.thelasttime.permission.RequestPermissionUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by mśmiech on 03.05.2022.
 */
abstract class UserSessionActivity: AppCompatActivity() {
    private val permissionCoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @Inject
    lateinit var requestPermissionActionBus: RequestPermissionActionBus

    @Inject
    lateinit var checkPermissionUseCase: CheckPermissionUseCase

    @Inject
    lateinit var requestPermissionUseCase: RequestPermissionUseCase

    @Inject
    lateinit var needRequestPostNotificationPermissionUseCase: NeedRequestPostNotificationPermissionUseCase

    @Inject
    lateinit var ensurePostNotificationPermissionUseCase: EnsurePostNotificationPermissionUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userSessionComponent = (application as LastTimeApplication).userSessionComponent
        if (userSessionComponent == null) {
            LoginActivity.start(this)
            finishAfterTransition()
        } else {
            userSessionComponent.inject(this)
            onActivityCreate(savedInstanceState)
        }
    }

    abstract fun onActivityCreate(savedInstanceState: Bundle?)

    override fun onResume() {
        super.onResume()
        permissionCoroutineScope.launch {
            requestPermissionActionBus.getRequestFlow().onEach { request ->
                if (checkPermissionUseCase.execute(request.permission)) {
                    requestPermissionActionBus.response(request.permission, true)
                } else {
                    val granted =
                        requestPermissionUseCase.execute(lifecycle, activityResultRegistry, request.permission)
                    requestPermissionActionBus.response(request.permission, granted)
                }
            }.launchIn(this)
            if (needRequestPostNotificationPermissionUseCase.execute()) {
                Timber.tag("asd").d("needRequestPostNotificationPermissionUseCase true")
                ensurePostNotificationPermissionUseCase.execute()
            } else {
                Timber.tag("asd").d("needRequestPostNotificationPermissionUseCase false")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        permissionCoroutineScope.coroutineContext.cancelChildren()
    }
}