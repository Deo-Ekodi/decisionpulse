package com.decisionpulse.demo.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.decisionpulse.demo.data.model.AppView
import com.decisionpulse.demo.data.mock.MockRepository

object AppState {
    var currentView by mutableStateOf(AppView.EXTENSION_OFFICER)
    var selectedSaccoId by mutableStateOf(MockRepository.defaultSaccoId)
}