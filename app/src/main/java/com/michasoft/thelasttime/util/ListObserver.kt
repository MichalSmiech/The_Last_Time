package com.michasoft.thelasttime.util

/**
 * Created by mśmiech on 12.11.2021.
 */

interface ListObserver<T> {
    fun onChanged(entry: T)
}