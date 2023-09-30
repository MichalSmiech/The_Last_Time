package com.michasoft.thelasttime.eventDetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.michasoft.thelasttime.eventInstanceAdd.EventInstanceAddBottomSheet
import com.michasoft.thelasttime.eventInstanceDetails.EventInstanceDetailsActivity
import com.michasoft.thelasttime.view.DeleteConfirmationDialog
import com.michasoft.thelasttime.view.LoadingView
import com.michasoft.thelasttime.view.theme.LastTimeTheme3
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class EventDetailsActivity : AppCompatActivity() {
    private lateinit var eventId: String
    private val viewModel by viewModels<EventDetailsViewModel>(factoryProducer = {
        EventDetailsViewModel.Factory(
            eventId
        )
    })

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventId = intent.getStringExtra(ARG_EVENT_ID) ?: throw IllegalStateException("No event id")
        setContent {
            val bottomSheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )
            LastTimeTheme3(window = window) {
                EventDetailsScreen(viewModel, bottomSheetState)
            }
            val coroutineScope = rememberCoroutineScope()
            LaunchedEffect(Unit) {
                viewModel.actions.onEach {
                    when (it) {
                        is EventDetailsAction.Finish -> finishAfterTransition()
                        is EventDetailsAction.NavigateToEventInstanceDetails -> launchEventInstanceDetailsActivity(
                            it.instanceId
                        )

                        is EventDetailsAction.HideEventInstanceAddBottomSheet -> {
                            coroutineScope.launch {
                                bottomSheetState.hide()
                            }.invokeOnCompletion {
                                if (!bottomSheetState.isVisible) {
                                    viewModel.onBottomSheetHidden()
                                }
                            }
                        }
                    }
                }.launchIn(lifecycleScope)
            }
            BackHandler(enabled = bottomSheetState.isVisible) {
                coroutineScope.launch {
                    bottomSheetState.hide()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    private fun launchEventInstanceDetailsActivity(instanceId: String) {
//        EventInstanceActivity.start(this, eventId, instanceId)
        startActivity(
            EventInstanceDetailsActivity.getLaunchIntent(this, eventId, instanceId),
            ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
        )
    }

    companion object {
        private const val ARG_EVENT_ID = "eventId"

        fun getLaunchIntent(context: Context, eventId: String): Intent {
            val intent = Intent(context, EventDetailsActivity::class.java)
            intent.putExtra(ARG_EVENT_ID, eventId)
            return intent
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsScreen(
    viewModel: EventDetailsViewModel,
    bottomSheetState: SheetState,
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    Scaffold(
        topBar = {
            TopBar(
                eventName = state.event?.name ?: "",
                onEventNameChange = viewModel::changeName,
                onDiscardClick = viewModel::onDiscardButtonClicked,
                onDelete = viewModel::onDeleteButtonClicked
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onInstanceAdded() }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add event instance",
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (state.isLoading) {
                LoadingView()
            } else {
                EventDetailsContent(
                    eventInstances = state.eventInstances,
                    onEventInstanceClick = viewModel::onEventInstanceClicked
                )
                if (state.isDeleteConfirmationDialogShowing) {
                    DeleteConfirmationDialog(
                        onDismissDialog = viewModel::deleteConfirmationDialogDismissed,
                        onConfirmClicked = viewModel::deleteEvent
                    )
                }
                if (state.isBottomSheetShowing) {
                    ModalBottomSheet(
                        sheetState = bottomSheetState,
                        scrimColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.60f),
                        onDismissRequest = { viewModel.onBottomSheetHidden() }
                    ) {
                        EventInstanceAddBottomSheet(viewModel.eventInstanceAddViewModel)
                    }

                }
            }
        }
    }
}

