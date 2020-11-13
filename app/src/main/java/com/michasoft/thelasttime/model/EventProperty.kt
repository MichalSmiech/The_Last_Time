package com.michasoft.thelasttime.model

/**
 * Created by mśmiech on 31.10.2020.
 */

abstract class EventProperty<S : EventPropertySchema, I : EventPropertyInstance> {
    protected var schema: S? = null
    protected var instance: I? = null
}