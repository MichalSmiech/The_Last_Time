package com.michasoft.thelasttime.eventLabels

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.michasoft.thelasttime.view.LoadingView
import com.michasoft.thelasttime.view.UserSessionActivity
import com.michasoft.thelasttime.view.theme.LastTimeTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EventLabelsActivity : UserSessionActivity() {
    private lateinit var eventId: String
    private val viewModel by viewModels<EventLabelsViewModel>(factoryProducer = {
        EventLabelsViewModel.Factory(
            eventId
        )
    })

    override fun onActivityCreate(savedInstanceState: Bundle?) {
        eventId = intent.getStringExtra(ARG_EVENT_ID) ?: throw IllegalStateException("No event id")
        setContent {
            LastTimeTheme(window = window) {
                EventLabelsScreen(viewModel)
            }
            LaunchedEffect(Unit) {
                viewModel.actions.onEach {
                    when (it) {
                        is EventLabelsAction.Finish -> finishAfterTransition()
                    }
                }.launchIn(lifecycleScope)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    companion object {
        private const val ARG_EVENT_ID = "eventId"

        fun getLaunchIntent(context: Context, eventId: String): Intent {
            val intent = Intent(context, EventLabelsActivity::class.java)
            intent.putExtra(ARG_EVENT_ID, eventId)
            return intent
        }
    }
}

@Composable
fun EventLabelsScreen(viewModel: EventLabelsViewModel) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    Scaffold(
        topBar = {
            TopBar(
                onDiscardClick = viewModel::onDiscardButtonClicked,
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
                EventLabelsContent(
                    labelItems = state.labelItems,
                    onLabelItemCheckedChange = viewModel::changeLabelItemChecked
                )
            }
        }
    }
}