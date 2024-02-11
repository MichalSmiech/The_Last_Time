package com.michasoft.thelasttime.util

import org.joda.time.DateTime

val DateTime.isNotAfterNow: Boolean
    get() = this.isAfterNow.not()