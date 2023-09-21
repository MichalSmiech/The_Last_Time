package com.michasoft.thelasttime.eventdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.michasoft.thelasttime.view.EventInstanceActivity
import com.michasoft.thelasttime.view.LoadingView
import com.michasoft.thelasttime.view.theme.LastTimeTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EventDetailsActivity : AppCompatActivity() {
    private lateinit var eventId: String
    private val viewModel by viewModels<EventDetailsViewModel>(factoryProducer = {
        EventDetailsViewModel.Factory(
            eventId
        )
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventId = intent.getStringExtra(ARG_EVENT_ID) ?: throw IllegalStateException("No event id")
        setContent {
            LastTimeTheme {
                EventDetailsScreen(viewModel)
            }
        }

        viewModel.actions.onEach {
            when (it) {
                is EventDetailsAction.Finish -> finish()
                is EventDetailsAction.NavigateToEventInstanceDetails -> launchEventInstanceDetailsActivity(
                    it.instanceId
                )
            }
        }.launchIn(lifecycleScope)
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    private fun launchEventInstanceDetailsActivity(instanceId: String) =
        EventInstanceActivity.start(this, eventId, instanceId)

    companion object {
        private const val ARG_EVENT_ID = "eventId"

        fun getLaunchIntent(context: Context, eventId: String): Intent {
            val intent = Intent(context, EventDetailsActivity::class.java)
            intent.putExtra(ARG_EVENT_ID, eventId)
            return intent
        }
    }
}

@Composable
fun EventDetailsScreen(viewModel: EventDetailsViewModel) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val scaffoldState = rememberScaffoldState()
    Scaffold(scaffoldState = scaffoldState) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (state.isLoading) {
                LoadingView(modifier = Modifier.padding(it))
            } else {
                EventDetailsContent(
                    modifier = Modifier.padding(it),
                    event = state.event!!,
                    onEventNameChange = viewModel::changeName,
                    onDiscardClick = viewModel::onDiscardButtonClicked,
                    onDelete = {},
                    eventInstances = state.eventInstances,
                    onEventInstanceClick = viewModel::onEventInstanceClicked
                )
            }
        }
    }
}

