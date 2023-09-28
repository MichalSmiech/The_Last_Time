package com.michasoft.thelasttime.eventList

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.michasoft.thelasttime.eventDetails.EventDetailsActivity
import com.michasoft.thelasttime.eventInstanceAdd.EventInstanceAddBottomSheet
import com.michasoft.thelasttime.view.LoadingView
import com.michasoft.thelasttime.view.theme.LastTimeTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class EventListActivity : AppCompatActivity() {
    private val viewModel by viewModels<EventListViewModel>(factoryProducer = {
        EventListViewModel.Factory()
    })

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val bottomSheetState = rememberModalBottomSheetState(
                initialValue = ModalBottomSheetValue.Hidden,
                skipHalfExpanded = true,
            )
            LastTimeTheme {
                EventListScreen(viewModel, bottomSheetState)
            }
            val scope = rememberCoroutineScope()
            LaunchedEffect(Unit) {
                viewModel.actions.onEach {
                    when (it) {
                        is EventListAction.NavigateToEventDetails -> launchEventDetailsActivity(
                            it.eventId
                        )
                        is EventListAction.ShowEventInstanceAddBottomSheet -> {
                            scope.launch {
                                bottomSheetState.show()
                            }
                        }

                        is EventListAction.HideEventInstanceAddBottomSheet -> {
                            scope.launch {
                                bottomSheetState.hide()
                            }
                        }
                    }
                }.launchIn(lifecycleScope)
            }
        }
    }

    private fun launchEventDetailsActivity(eventId: String) {
        startActivity(EventDetailsActivity.getLaunchIntent(this, eventId))
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EventListScreen(viewModel: EventListViewModel, bottomSheetState: ModalBottomSheetState) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        scrimColor = MaterialTheme.colors.surface.copy(alpha = 0.60f),
        sheetContent = {
            EventInstanceAddBottomSheet(viewModel.eventInstanceAddViewModel)
        }) {
        val scaffoldState = rememberScaffoldState()
        Scaffold(
            scaffoldState = scaffoldState
        ) {
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
}