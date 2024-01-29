package com.michasoft.thelasttime.labelsEdit

import com.michasoft.thelasttime.model.Label

/**
 * Created by mśmiech on 27.11.2023.
 */
data class LabelsEditState(
    val isLoading: Boolean,
    val labels: List<Label>
)