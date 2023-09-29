package com.michasoft.thelasttime.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.michasoft.thelasttime.LastTimeApplication
import com.michasoft.thelasttime.R
import com.michasoft.thelasttime.databinding.ActivityEventBinding
import com.michasoft.thelasttime.util.ShowDeleteConfirmationDialog
import com.michasoft.thelasttime.view.bottomSheet.AddEventInstanceBottomSheet
import com.michasoft.thelasttime.viewModel.CommonViewModel
import com.michasoft.thelasttime.viewModel.EventViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventActivity : UserSessionActivity() {
    @Inject
    lateinit var viewModelFactory: EventViewModel.Factory
    private val viewModel: EventViewModel by viewModels{ viewModelFactory }

    override fun onActivityCreate(savedInstanceState: Bundle?) {
        (application as LastTimeApplication).userSessionComponent!!.inject(this)
        val binding: ActivityEventBinding = DataBindingUtil.setContentView(this, R.layout.activity_event)
        setSupportActionBar(binding.activityEventToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.delete -> {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.deleteEvent(true)
            }
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
        val eventId = intent.getStringExtra(ARG_EVENT_ID)
        viewModel.start(eventId!!)
        viewModel.flowEventBus.observe(this) {
            when(it) {
                is CommonViewModel.Finish -> {
                    finishAfterTransition()
                }
                is EventViewModel.ShowEventInstance -> {
                    EventInstanceActivity.start(this, it.eventId, it.instanceId)
                }
                is EventViewModel.ShowAddEventInstanceBottomSheet -> {
                    AddEventInstanceBottomSheet.show(supportFragmentManager, it.eventId)
                }
                is ShowDeleteConfirmationDialog -> {
                    AlertDialog.Builder(this)
                        .setTitle(R.string.delete_confirmation_title)
                        .setPositiveButton(R.string.action_ok) { dialog, _ ->
                            CoroutineScope(Dispatchers.IO).launch {
                                viewModel.deleteEvent(false)
                            }
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

    companion object {
        const val ARG_EVENT_ID = "ARG_EVENT_ID"

        fun start(activity: AppCompatActivity, eventId: String) {
            val intent = Intent(activity, EventActivity::class.java)
            intent.putExtra(ARG_EVENT_ID, eventId)
            activity.startActivity(intent)
        }
    }
}