package com.michasoft.thelasttime.model

/**
 * Created by mśmiech on 12.11.2021.
 */
class EventLive(event: Event): Event(event.id, event.displayName, event.createTimestamp) {

}