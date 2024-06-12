package com.michasoft.thelasttime.eventList

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.DoDisturb
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material3.DrawerState
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
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.michasoft.thelasttime.calendarWidget.monthCount.CalendarModel
import com.michasoft.thelasttime.calendarWidget.monthCount.DateRange
import com.michasoft.thelasttime.calendarWidget.monthCount.MonthCountWidget
import com.michasoft.thelasttime.eventAdd.EventAddActivity
import com.michasoft.thelasttime.eventDetails.EventDetailsActivity
import com.michasoft.thelasttime.eventInstanceAdd.EventInstanceAddBottomSheet
import com.michasoft.thelasttime.labelsEdit.LabelsEditActivity
import com.michasoft.thelasttime.view.LoadingView
import com.michasoft.thelasttime.view.LoginActivity
import com.michasoft.thelasttime.view.MainActivity
import com.michasoft.thelasttime.view.UserSessionActivity
import com.michasoft.thelasttime.view.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.joda.time.LocalDate

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
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            AppTheme(window = window) {
                MonthCountWidget(
                    calendarModel = CalendarModel(
                        DateRange(
                            LocalDate.now().minusYears(1),
                            LocalDate.now()
                        )
                    ),
                )
//                EventListScreen(viewModel, bottomSheetState, drawerState)
            }
            val coroutineScope = rememberCoroutineScope()
            LaunchedEffect(Unit) {
                viewModel.actions.onEach { action ->
                    when (action) {
                        is EventListAction.NavigateToEventDetails -> launchEventDetailsActivity(
                            action.eventId
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
                            launchMainActivity()
                        }

                        is EventListAction.NavigateToLabelsEdit -> {
                            launchLabelsEditActivity(action.withNewLabelFocus)
                        }

                        is EventListAction.CloseDrawer -> {
                            coroutineScope.launch {
                                drawerState.close()
                            }.invokeOnCompletion {
                                action.deferred.complete(Unit)
                            }
                        }

                        is EventListAction.SignOut -> {
                            runBlocking {
                                withContext(Dispatchers.IO) {
                                    viewModel.singOut()
                                }
                            }
                            Intent(this@EventListActivity, LoginActivity::class.java).also {
                                startActivity(
                                    it,
                                    ActivityOptionsCompat.makeSceneTransitionAnimation(this@EventListActivity)
                                        .toBundle()
                                )
                            }
                            finishAfterTransition()
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

    private fun launchMainActivity() {
        startActivity(
            Intent(this, MainActivity::class.java),
            ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
        )
    }

    private fun launchLabelsEditActivity(withNewLabelFocus: Boolean) {
        startActivity(
            LabelsEditActivity.getLaunchIntent(this, withNewLabelFocus),
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
fun EventListScreen(
    viewModel: EventListViewModel,
    bottomSheetState: SheetState,
    drawerState: DrawerState
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(
                    onMenuItemClick = { viewModel.menuItemClicked(it) },
                    activeMenuItem = if (state.labelFilter == null) MenuItemType.EVENTS else null,
                    labels = state.labels,
                    activeLabel = state.labelFilter,
                    onLabelClick = { label ->
                        scope.launch {
                            drawerState.close()
                        }.invokeOnCompletion {
                            viewModel.changeLabelFilter(label)
                        }
                    },
                    onLabelsEditClick = { viewModel.onLabelsEditClicked() },
                    onAddNewLabelClick = { viewModel.onAddNewLabelClicked() }
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
                TopBar(state.isErrorSync, drawerState, state.userPhotoUrl, state.labelFilter)
            }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                if (state.isLoading) {
                    LoadingView()
                } else if (state.events.isEmpty() && state.labelFilter != null) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                modifier = Modifier.size(100.dp),
                                imageVector = Icons.Outlined.Label,
                                contentDescription = null
                            )
                            Text(text = "No events with this label")
                            Spacer(modifier = Modifier.height(60.dp))
                        }
                    }
                } else if (state.events.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                modifier = Modifier.size(100.dp),
                                imageVector = Icons.Outlined.DoDisturb,
                                contentDescription = null
                            )
                            Text(text = "No events")
                            Spacer(modifier = Modifier.height(60.dp))
                        }
                    }
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