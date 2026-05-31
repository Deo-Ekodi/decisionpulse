package com.decisionpulse.demo.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import com.decisionpulse.demo.data.mock.MockRepository
import com.decisionpulse.demo.data.model.SaccoSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// class DashboardViewModel : ViewModel() {
//     private val _summary = MutableStateFlow(
//         MockRepository.getSaccoSummary(MockRepository.defaultSaccoId)!!
//     )
//     val summary: StateFlow<SaccoSummary> = _summary

//     fun loadSacco(saccoId: String) {
//         val s = MockRepository.getSaccoSummary(saccoId)
//         if (s != null) _summary.value = s
//     }
// }

class DashboardViewModel : ViewModel() {
    private val _summary = MutableStateFlow(
        MockRepository.getSaccoSummary(MockRepository.defaultSaccoId)!!
    )
    val summary: StateFlow<SaccoSummary> = _summary

    fun loadSacco(saccoId: String) {
        MockRepository.getSaccoSummary(saccoId)?.let { _summary.value = it }
    }
}