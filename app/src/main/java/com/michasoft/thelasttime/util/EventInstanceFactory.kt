package com.michasoft.thelasttime.util

import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstance
import org.joda.time.DateTime

/**
 * Created by mśmiech on 12.11.2021.
 */
object EventInstanceFactory {
    fun createEmptyEventInstance(event: Event): EventInstance {
        return EventInstance(
            IdGenerator.autoId(),
            event.id,
            DateTime.now(),
            ArrayList()
        )
    }
}