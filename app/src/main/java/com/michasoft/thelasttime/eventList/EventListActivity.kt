package com.michasoft.thelasttime.eventList

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.michasoft.thelasttime.eventAdd.EventAddActivity
import com.michasoft.thelasttime.eventDetails.EventDetailsActivity
import com.michasoft.thelasttime.eventInstanceAdd.EventInstanceAddBottomSheet
import com.michasoft.thelasttime.view.LoadingView
import com.michasoft.thelasttime.view.MainActivity
import com.michasoft.thelasttime.view.UserSessionActivity
import com.michasoft.thelasttime.view.theme.LastTimeTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class EventListActivity : UserSessionActivity() {
    private val viewModel by viewModels<EventListViewModel>(factoryProducer = {
        EventListViewModel.Factory()
    })

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onActivityCreate(savedInstanceState: Bundle?) {
        setContent {
            val bottomSheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true,
            )
            LastTimeTheme(window = window) {
                EventListScreen(viewModel, bottomSheetState)
            }
            val coroutineScope = rememberCoroutineScope()
            LaunchedEffect(Unit) {
                viewModel.actions.onEach {
                    when (it) {
                        is EventListAction.NavigateToEventDetails -> launchEventDetailsActivity(
                            it.eventId
                        )

                        is EventListAction.HideEventInstanceAddBottomSheet -> {
                            coroutineScope.launch {
                                bottomSheetState.hide()
                            }.invokeOnCompletion {
                                if (!bottomSheetState.isVisible) {
                                    viewModel.onBottomSheetHidden()
                                }
                            }
                        }

                        is EventListAction.NavigateToEventAdd -> {
                            launchEventAddActivity()
                        }

                        is EventListAction.NavigateToSettings -> {
                            //TODO
                        }

                        is EventListAction.NavigateToDebug -> {
                            launchEventMainActivity()
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

    private fun launchEventDetailsActivity(eventId: String) {
        startActivity(
            EventDetailsActivity.getLaunchIntent(this, eventId),
            ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
        )
    }

    private fun launchEventAddActivity() {
        startActivity(
            Intent(this, EventAddActivity::class.java),
            ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
        )
    }

    private fun launchEventMainActivity() {
        startActivity(
            Intent(this, MainActivity::class.java),
            ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
        )
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(viewModel: EventListViewModel, bottomSheetState: SheetState) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(
                    onMenuItemClick = { viewModel.menuItemClicked(it) },
                    labels = state.labels,
                    onLabelClick = {},
                    onLabelsEditClick = {},
                    onAddNewLabelClick = {}
                )
            }
        }
    ) {
        Scaffold(
            floatingActionButton = {
                AddEventInstanceButton(onClick = { viewModel.onEventAdd() })
            },
            floatingActionButtonPosition = FabPosition.End,
            topBar = {
                TopBar(state.isErrorSync, drawerState, state.userPhotoUrl)
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
                    EventListContent(
                        events = state.events,
                        onEventClick = viewModel::onEventClicked,
                        onInstanceAdd = viewModel::onInstanceAdded
                    )
                    if (state.isBottomSheetShowing) {
                        ModalBottomSheet(
                            sheetState = bottomSheetState,
                            onDismissRequest = { viewModel.onBottomSheetHidden() }
                        ) {
                            EventInstanceAddBottomSheet(viewModel.eventInstanceAddViewModel)
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun AddEventInstanceButton(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "add event instance",
        )
    }
}