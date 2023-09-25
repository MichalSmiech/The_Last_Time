package com.michasoft.thelasttime.eventlist

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
import com.michasoft.thelasttime.eventdetails.EventDetailsActivity
import com.michasoft.thelasttime.view.LoadingView
import com.michasoft.thelasttime.view.theme.LastTimeTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EventListActivity : AppCompatActivity() {
    private val viewModel by viewModels<EventListViewModel>(factoryProducer = {
        EventListViewModel.Factory()
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LastTimeTheme {
                EventListScreen(viewModel)
            }
        }

        viewModel.actions.onEach {
            when (it) {
                is EventListAction.NavigateToEventDetails -> launchEventDetailsActivity(
                    it.eventId
                )
            }
        }.launchIn(lifecycleScope)
    }

    private fun launchEventDetailsActivity(eventId: String) {
        startActivity(EventDetailsActivity.getLaunchIntent(this, eventId))
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }
}

@Composable
fun EventListScreen(viewModel: EventListViewModel) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val scaffoldState = rememberScaffoldState()
    Scaffold(scaffoldState = scaffoldState) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (state.isLoading) {
                LoadingView()
            } else {
                EventListContent(
                    events = state.events,
                    onEventClick = viewModel::onEventClicked,
                    onInstanceAdd = viewModel::onInstanceAdded
                )
            }
        }
    }
}