package com.decisionpulse.demo.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import com.decisionpulse.demo.data.mock.MockRepository
import com.decisionpulse.demo.data.model.SaccoSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DashboardViewModel : ViewModel() {
    private val _summary = MutableStateFlow(MockRepository.saccoSummary)
    val summary: StateFlow<SaccoSummary> = _summary
}
