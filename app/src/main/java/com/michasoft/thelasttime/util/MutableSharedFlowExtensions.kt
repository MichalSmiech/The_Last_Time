package com.michasoft.thelasttime.util

import kotlinx.coroutines.flow.MutableSharedFlow

suspend fun MutableSharedFlow<Unit>.notify() {
    emit(Unit)
}