package com.michasoft.thelasttime.permission

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.timeout
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

@Singleton
class RequestPermissionActionBus @Inject constructor() {
    private val actionBus: MutableSharedFlow<Action> = MutableSharedFlow(5)

    suspend fun request(permission: String): Boolean {
        actionBus.emit(Action.Request(permission))
        val response = actionBus.filter { it is Action.Response && it.permission == permission }
            .timeout(30.seconds)
            .catch {
                if (it is TimeoutCancellationException) {
                    emit(Action.Response(permission, false))
                } else {
                    throw it
                }
            }.first()
        return (response as Action.Response).isGranted
    }

    suspend fun response(permission: String, isGranted: Boolean) {
        actionBus.emit(Action.Response(permission, isGranted))
    }

    fun getRequestFlow() = actionBus.filterIsInstance<Action.Request>()

    sealed interface Action {
        data class Request(val permission: String) : Action
        data class Response(val permission: String, val isGranted: Boolean) : Action
    }
}