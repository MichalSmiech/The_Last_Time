package com.michasoft.thelasttime.labelsEdit

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
import com.michasoft.thelasttime.view.LoadingView
import com.michasoft.thelasttime.view.UserSessionActivity
import com.michasoft.thelasttime.view.theme.LastTimeTheme
import kotlinx.coroutines.flow.onEach

class LabelsEditActivity : UserSessionActivity() {
    private val viewModel by viewModels<LabelsEditViewModel>()

    override fun onActivityCreate(savedInstanceState: Bundle?) {
        setContent {
            LastTimeTheme(window = window) {
                LabelsEditScreen(viewModel)
            }
            LaunchedEffect(Unit) {
                viewModel.actions.onEach {
                    when (it) {
                        is LabelsEditAction.Finish -> finishAfterTransition()
                    }
                }
            }
        }
    }
}

@Composable
fun LabelsEditScreen(
    viewModel: LabelsEditViewModel
) {
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
                LabelsEditContent()
            }
        }
    }
}