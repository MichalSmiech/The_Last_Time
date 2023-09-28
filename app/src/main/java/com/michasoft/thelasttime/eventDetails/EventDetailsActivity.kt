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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.michasoft.thelasttime.eventInstanceAdd.EventInstanceAddBottomSheet
import com.michasoft.thelasttime.eventInstanceDetails.EventInstanceDetailsActivity
import com.michasoft.thelasttime.view.DeleteConfirmationDialog
import com.michasoft.thelasttime.view.LoadingView
import com.michasoft.thelasttime.view.theme.LastTimeTheme
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

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventId = intent.getStringExtra(ARG_EVENT_ID) ?: throw IllegalStateException("No event id")
        setContent {
            val bottomSheetState = rememberModalBottomSheetState(
                initialValue = ModalBottomSheetValue.Hidden,
                skipHalfExpanded = true,
            )
            LastTimeTheme {
                EventDetailsScreen(viewModel, bottomSheetState)
            }
            val coroutineScope = rememberCoroutineScope()
            LaunchedEffect(Unit) {
                viewModel.actions.onEach {
                    when (it) {
                        is EventDetailsAction.Finish -> finish()
                        is EventDetailsAction.NavigateToEventInstanceDetails -> launchEventInstanceDetailsActivity(
                            it.instanceId
                        )

                        is EventDetailsAction.ShowEventInstanceAddBottomSheet -> {
                            coroutineScope.launch {
                                bottomSheetState.show()
                            }
                        }

                        is EventDetailsAction.HideEventInstanceAddBottomSheet -> {
                            coroutineScope.launch {
                                bottomSheetState.hide()
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
        startActivity(EventInstanceDetailsActivity.getLaunchIntent(this, eventId, instanceId))
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EventDetailsScreen(viewModel: EventDetailsViewModel, bottomSheetState: ModalBottomSheetState) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        scrimColor = MaterialTheme.colors.surface.copy(alpha = 0.60f),
        sheetContent = {
            EventInstanceAddBottomSheet(viewModel.eventInstanceAddViewModel)
        }) {
        val scaffoldState = rememberScaffoldState()
        Scaffold(
            scaffoldState = scaffoldState,
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
                }
            }
        }
    }
}

