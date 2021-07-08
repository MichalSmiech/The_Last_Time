package com.michasoft.thelasttime.viewModel

import androidx.lifecycle.ViewModel
import com.michasoft.thelasttime.util.FlowEvent
import com.michasoft.thelasttime.util.SingleLiveEvent

/**
 * Created by mśmiech on 08.07.2021.
 */
open class CommonViewModel: ViewModel() {
    var flowEventBus = SingleLiveEvent<FlowEvent>()

    class Finish : FlowEvent()
}