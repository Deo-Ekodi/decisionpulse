package com.decisionpulse.demo.ui.screens.farmdetail

import androidx.lifecycle.ViewModel
import com.decisionpulse.demo.data.mock.MockRepository
import com.decisionpulse.demo.data.model.DailyReading
import com.decisionpulse.demo.data.model.Farm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FarmDetailViewModel : ViewModel() {
    private val _farm = MutableStateFlow<Farm?>(null)
    val farm: StateFlow<Farm?> = _farm

    private val _history = MutableStateFlow<List<DailyReading>>(emptyList())
    val history: StateFlow<List<DailyReading>> = _history

    fun load(code: String) {
        val f = MockRepository.getFarmByCode(code)
        _farm.value = f
        _history.value = f?.let { MockRepository.getHistory(it) } ?: emptyList()
    }
}