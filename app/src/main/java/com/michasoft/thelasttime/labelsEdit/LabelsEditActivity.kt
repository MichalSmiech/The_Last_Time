package com.michasoft.thelasttime.labelsEdit

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.michasoft.thelasttime.view.LoadingView
import com.michasoft.thelasttime.view.UserSessionActivity
import com.michasoft.thelasttime.view.theme.AppTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LabelsEditActivity : UserSessionActivity() {
    private val viewModel by viewModels<LabelsEditViewModel>(factoryProducer = { LabelsEditViewModel.Factory() })

    override fun onActivityCreate(savedInstanceState: Bundle?) {
        val withNewLabelTextFieldFocus = intent.getBooleanExtra(ARG_WITH_NEW_LABEL_TEXT_FIELD_FOCUS, false)
        setContent {
            AppTheme(window = window) {
                LabelsEditScreen(viewModel, withNewLabelTextFieldFocus)
            }
            LaunchedEffect(Unit) {
                viewModel.actions.onEach {
                    when (it) {
                        is LabelsEditAction.Finish -> finishAfterTransition()
                    }
                }.launchIn(lifecycleScope)
            }
        }
    }

    companion object {
        private const val ARG_WITH_NEW_LABEL_TEXT_FIELD_FOCUS = "withNewLabelTextFieldFocus"

        fun getLaunchIntent(context: Context, withNewLabelTextFieldFocus: Boolean = false): Intent {
            val intent = Intent(context, LabelsEditActivity::class.java)
            intent.putExtra(ARG_WITH_NEW_LABEL_TEXT_FIELD_FOCUS, withNewLabelTextFieldFocus)
            return intent
        }
    }
}

@Composable
fun LabelsEditScreen(
    viewModel: LabelsEditViewModel,
    withNewLabelTextFieldFocus: Boolean
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
                val newLabelFocusRequester by remember { mutableStateOf(FocusRequester()) }
                LabelsEditContent(
                    state = state,
                    onLabelNameChange = viewModel::onLabelNameChange,
                    newLabelFocusRequester = newLabelFocusRequester,
                    onNewLabelNameChange = viewModel::onNewNameChange,
                    onNewLabelAdd = viewModel::onNewLabelAdd,
                    onLabelDelete = viewModel::onLabelDelete,
                )
                if (withNewLabelTextFieldFocus) {
                    LaunchedEffect(key1 = Unit) {
                        newLabelFocusRequester.requestFocus()
                    }
                }
            }
        }
    }
}