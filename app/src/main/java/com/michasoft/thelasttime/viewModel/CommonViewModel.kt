package com.michasoft.thelasttime.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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