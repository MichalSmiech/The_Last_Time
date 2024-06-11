package com.michasoft.thelasttime.util

fun convertFloatRange(
    value: Float,
    originalRangeFrom: Float,
    originalRangeTo: Float,
    targetRangeFrom: Float,
    targetRangeTo: Float
): Float {
    val ratio = (value - originalRangeFrom) / (originalRangeTo - originalRangeFrom)
    return (ratio * (targetRangeTo - targetRangeFrom)) + targetRangeFrom
}