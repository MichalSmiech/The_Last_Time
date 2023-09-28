package com.michasoft.thelasttime.eventAdd

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
import com.michasoft.thelasttime.eventDetails.EventDetailsActivity
import com.michasoft.thelasttime.view.theme.LastTimeTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EventAddActivity : AppCompatActivity() {
    private val viewModel by viewModels<EventAddViewModel>(factoryProducer = {
        EventAddViewModel.Factory()
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LastTimeTheme {
                EventAddScreen(viewModel)
            }
        }
        viewModel.actions.onEach {
            when (it) {
                is EventAddAction.Finish -> finish()
                is EventAddAction.FinishAndNavigateToEventDetails -> {
                    finish()
                    launchEventDetailsActivity(it.eventId)
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun launchEventDetailsActivity(eventId: String) {
        startActivity(EventDetailsActivity.getLaunchIntent(this, eventId))
    }
}

@Composable
fun EventAddScreen(viewModel: EventAddViewModel) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                onDiscardClick = viewModel::onDiscardButtonClicked,
                onSaveClick = viewModel::onSave
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            EventAddContent(
                state.eventName,
                onEventNameChange = viewModel::changeEventName
            )
        }
    }
}