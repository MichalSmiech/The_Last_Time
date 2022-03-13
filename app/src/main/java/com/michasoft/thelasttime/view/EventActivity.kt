package com.michasoft.thelasttime.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.michasoft.thelasttime.R
import com.michasoft.thelasttime.databinding.ActivityEventBinding
import com.michasoft.thelasttime.viewModel.CommonViewModel
import com.michasoft.thelasttime.viewModel.EventViewModel
import dagger.android.AndroidInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: EventViewModel by viewModels{ viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
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
            Toast.makeText(this@EventActivity, "aaa", Toast.LENGTH_SHORT).show()
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
        finish()
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
                    finish()
                }
                is EventViewModel.ShowEventInstance -> {
                    EventInstanceActivity.start(this, it.eventId)
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