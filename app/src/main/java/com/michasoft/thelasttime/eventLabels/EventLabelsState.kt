package com.michasoft.thelasttime.eventLabels

import com.michasoft.thelasttime.eventLabels.model.LabelItem

/**
 * Created by mśmiech on 06.10.2023.
 */
data class EventLabelsState(
    val isLoading: Boolean,
    val labelItems: List<LabelItem>,
    val newLabelName: String,
    val filterLabelName: String
)