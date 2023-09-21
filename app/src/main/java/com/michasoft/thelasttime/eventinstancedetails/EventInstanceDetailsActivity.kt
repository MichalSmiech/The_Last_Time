package com.michasoft.thelasttime.eventinstancedetails

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
import com.michasoft.thelasttime.view.DeleteConfirmationDialog
import com.michasoft.thelasttime.view.LoadingView
import com.michasoft.thelasttime.view.theme.LastTimeTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EventInstanceDetailsActivity : AppCompatActivity() {
    private lateinit var eventId: String
    private lateinit var instanceId: String
    private val viewModel by viewModels<EventInstanceDetailsViewModel>(factoryProducer = {
        EventInstanceDetailsViewModel.Factory(
            eventId,
            instanceId
        )
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventId = intent.getStringExtra(ARG_EVENT_ID) ?: throw IllegalStateException("No event id")
        instanceId = intent.getStringExtra(ARG_EVENT_INSTANCE_ID)
            ?: throw IllegalStateException("No event instance id")
        setContent {
            LastTimeTheme {
                EventInstanceDetailsScreen(viewModel)
            }
        }

        viewModel.actions.onEach {
            when (it) {
                is EventInstanceDetailsAction.Finish -> finish()
            }
        }.launchIn(lifecycleScope)
    }

    companion object {
        private const val ARG_EVENT_ID = "eventId"
        private const val ARG_EVENT_INSTANCE_ID = "eventInstanceId"

        fun getLaunchIntent(context: Context, eventId: String, instanceId: String): Intent {
            val intent = Intent(context, EventInstanceDetailsActivity::class.java)
            intent.putExtra(ARG_EVENT_ID, eventId)
            intent.putExtra(ARG_EVENT_INSTANCE_ID, instanceId)
            return intent
        }
    }
}

@Composable
fun EventInstanceDetailsScreen(viewModel: EventInstanceDetailsViewModel) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                eventName = state.eventName,
                onDiscardClick = viewModel::onDiscardButtonClicked,
                onDelete = viewModel::onDeleteButtonClicked
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (state.isLoading) {
                LoadingView()
            } else {
                EventInstanceDetailsContent(
                    instance = state.eventInstance!!,
                    onDateChange = viewModel::changeDate,
                    onTimeChange = viewModel::changeTime
                )
                if (state.isDeleteConfirmationDialogShowing) {
                    DeleteConfirmationDialog(
                        onDismissDialog = viewModel::deleteConfirmationDialogDismissed,
                        onConfirmClicked = viewModel::deleteEventInstance
                    )
                }
            }
        }
    }
}