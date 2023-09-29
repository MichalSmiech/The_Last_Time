package com.michasoft.thelasttime.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.michasoft.thelasttime.LastTimeApplication
import com.michasoft.thelasttime.R
import com.michasoft.thelasttime.databinding.ActivityEventInstanceBinding
import com.michasoft.thelasttime.util.ShowDeleteConfirmationDialog
import com.michasoft.thelasttime.viewModel.CommonViewModel
import com.michasoft.thelasttime.viewModel.EventInstanceViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.DateTimeFieldType
import javax.inject.Inject


class EventInstanceActivity : UserSessionActivity() {
    @Inject
    lateinit var viewModelFactory: EventInstanceViewModel.Factory
    private val viewModel: EventInstanceViewModel by viewModels { viewModelFactory }

    override fun onActivityCreate(savedInstanceState: Bundle?) {
        (application as LastTimeApplication).userSessionComponent!!.inject(this)
        val binding: ActivityEventInstanceBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_event_instance)
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.eventName.observe(this) {
            supportActionBar?.title = it
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.delete -> {
            viewModel.delete(true)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.saveIfNeeded()
        }
        finishAfterTransition()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.saveIfNeeded()
        }
    }

    override fun onStart() {
        super.onStart()
        val eventId = intent.getStringExtra(ARG_EVENT_ID)!!
        val instanceId = intent.getStringExtra(ARG_EVENT_INSTANCE_ID)!!
        viewModel.setInitData(eventId, instanceId)
        viewModel.flowEventBus.observe(this) {
            when (it) {
                is EventInstanceViewModel.ShowDatePicker -> {
                    DatePickerFragment().show(supportFragmentManager, DatePickerFragment.TAG)
                }
                is EventInstanceViewModel.ShowTimePicker -> {
                    TimePickerFragment().show(supportFragmentManager, TimePickerFragment.TAG)
                }
                is CommonViewModel.Finish -> {
                    finishAfterTransition()
                }
                is ShowDeleteConfirmationDialog -> {
                    AlertDialog.Builder(this)
                        .setTitle(R.string.delete_confirmation_title)
                        .setPositiveButton(R.string.action_ok) { dialog, _ ->
                            viewModel.delete()
                            dialog.dismiss()
                        }
                        .setNegativeButton(R.string.action_cancel) { dialog, _ ->
                            dialog.cancel()
                        }
                        .show()
                }
            }
        }
    }

    class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
        private val instanceViewModel: EventInstanceViewModel by viewModels(ownerProducer = this::requireActivity)

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val dateTime = instanceViewModel.timestamp.value ?: DateTime.now()
            val hour = dateTime.get(DateTimeFieldType.clockhourOfDay())
            val minute = dateTime.get(DateTimeFieldType.minuteOfHour())
            return TimePickerDialog(
                activity,
                this,
                hour,
                minute,
                DateFormat.is24HourFormat(activity)
            )
        }

        override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
            val timestamp = instanceViewModel.timestamp.value ?: DateTime.now()
            val newTimestamp = timestamp.withTime(hourOfDay, minute, 0, 0)
            instanceViewModel.timestamp.value = newTimestamp
        }

        companion object {
            const val TAG = "timePicker"
        }
    }

    class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
        private val instanceViewModel: EventInstanceViewModel by viewModels(ownerProducer = this::requireActivity)

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

        companion object {
            const val TAG = "datePicker"
        }
    }

    companion object {
        const val ARG_EVENT_ID = "EVENT_ID"
        const val ARG_EVENT_INSTANCE_ID = "EVENT_INSTANCE_ID"

        fun start(activity: AppCompatActivity, eventId: String, instanceId: String) {
            val intent = Intent(activity, EventInstanceActivity::class.java)
            intent.putExtra(ARG_EVENT_ID, eventId)
            intent.putExtra(ARG_EVENT_INSTANCE_ID, instanceId)
            activity.startActivity(intent)
        }
    }
}