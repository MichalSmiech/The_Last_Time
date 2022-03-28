package com.michasoft.thelasttime.view.bottomSheet

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.michasoft.thelasttime.R
import com.michasoft.thelasttime.databinding.BottomSheetAddEventInstanceBinding
import com.michasoft.thelasttime.view.EventInstanceActivity
import com.michasoft.thelasttime.viewModel.CommonViewModel
import com.michasoft.thelasttime.viewModel.EventInstanceViewModel
import dagger.android.support.AndroidSupportInjection
import org.joda.time.DateTime
import org.joda.time.DateTimeFieldType
import javax.inject.Inject

/**
 * Created by mśmiech on 28.03.2022.
 */
class AddEventInstanceBottomSheet(private val eventId: String) : BottomSheetDialogFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: EventInstanceViewModel by viewModels{ viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: BottomSheetAddEventInstanceBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.bottom_sheet_add_event_instance,
            container,
            false
        )
        viewModel.setInitData(eventId)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.flowEventBus.observe(this) {
            when(it) {
                is EventInstanceViewModel.ShowDatePicker -> {
                    DatePickerFragment().show(childFragmentManager, "datePicker")
                }
                is EventInstanceViewModel.ShowTimePicker -> {
                    TimePickerFragment().show(childFragmentManager, "timePicker")
                }
                is CommonViewModel.Finish -> {
                    dismissAllowingStateLoss()
                }
            }
        }

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this@AddEventInstanceBottomSheet)
    }

    class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
        private val instanceViewModel: EventInstanceViewModel by viewModels(ownerProducer = this::requireParentFragment)

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val dateTime = instanceViewModel.timestamp.value ?: DateTime.now()
            val hour = dateTime.get(DateTimeFieldType.clockhourOfDay())
            val minute = dateTime.get(DateTimeFieldType.minuteOfHour())
            return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
        }

        override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
            val timestamp = instanceViewModel.timestamp.value ?: DateTime.now()
            val newTimestamp = timestamp.withTime(hourOfDay, minute, 0, 0)
            instanceViewModel.timestamp.value = newTimestamp
        }
    }

    class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
        private val instanceViewModel: EventInstanceViewModel by viewModels(ownerProducer = this::requireParentFragment)

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val dateTime = instanceViewModel.timestamp.value ?: DateTime.now()
            val year = dateTime.get(DateTimeFieldType.year())
            val month = dateTime.get(DateTimeFieldType.monthOfYear()) - 1
            val day = dateTime.get(DateTimeFieldType.dayOfMonth())
            return DatePickerDialog(requireActivity(), this, year, month, day)
        }

        override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
            val timestamp = instanceViewModel.timestamp.value ?: DateTime.now()
            val newTimestamp = timestamp.withDate(year, month + 1, day)
            instanceViewModel.timestamp.value = newTimestamp
        }
    }

    companion object {
        const val TAG = "AddEventInstanceBottomSheet"

        fun show(fragmentManager: FragmentManager, eventId: String) {
            val bottomSheet = AddEventInstanceBottomSheet(eventId)
            bottomSheet.show(fragmentManager, TAG)
        }
    }
}