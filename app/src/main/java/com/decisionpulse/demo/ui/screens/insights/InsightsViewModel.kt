package com.decisionpulse.demo.ui.screens.insights

import androidx.lifecycle.ViewModel
import com.decisionpulse.demo.data.mock.MockRepository
import com.decisionpulse.demo.data.model.AiInsight
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class InsightsViewModel : ViewModel() {
    private val _insights = MutableStateFlow(MockRepository.insights)
    val insights: StateFlow<List<AiInsight>> = _insights
}
