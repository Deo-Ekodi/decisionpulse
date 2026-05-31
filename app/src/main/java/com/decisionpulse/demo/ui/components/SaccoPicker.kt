package com.decisionpulse.demo.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decisionpulse.demo.data.model.Sacco
import com.decisionpulse.demo.ui.theme.*

@Composable
fun SaccoPicker(
    saccos: List<Sacco>,
    selectedId: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    showAll: Boolean = false,
    allLabel: String = "All SACCOs"
) {
    Row(
        modifier            = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (showAll) {
            val allSelected = selectedId == "ALL"
            FilterChip(
                selected = allSelected,
                onClick  = { onSelect("ALL") },
                label    = { Text(allLabel, style = MaterialTheme.typography.labelSmall) },
                colors   = FilterChipDefaults.filterChipColors(
                    selectedContainerColor  = DPPurple.copy(alpha = 0.18f),
                    selectedLabelColor      = DPPurple,
                    containerColor          = BgSurface2,
                    labelColor              = TextMuted
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled          = true,
                    selected         = allSelected,
                    borderColor      = Border2,
                    selectedBorderColor = DPPurple.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(8.dp)
            )
        }
        saccos.forEach { sacco ->
            val selected = selectedId == sacco.id
            FilterChip(
                selected = selected,
                onClick  = { onSelect(sacco.id) },
                label    = { Text(sacco.id, style = MaterialTheme.typography.labelSmall) },
                colors   = FilterChipDefaults.filterChipColors(
                    selectedContainerColor  = DPGreen.copy(alpha = 0.18f),
                    selectedLabelColor      = DPGreen,
                    containerColor          = BgSurface2,
                    labelColor              = TextMuted
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled             = true,
                    selected            = selected,
                    borderColor         = Border2,
                    selectedBorderColor = DPGreen.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(8.dp)
            )
        }
    }
}