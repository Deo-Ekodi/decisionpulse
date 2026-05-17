package com.decisionpulse.demo.ui.screens.farms

import androidx.lifecycle.ViewModel
import com.decisionpulse.demo.data.mock.MockRepository
import com.decisionpulse.demo.data.model.Farm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FarmRosterViewModel : ViewModel() {
    private val _farms = MutableStateFlow(MockRepository.farms)
    val farms: StateFlow<List<Farm>> = _farms
}