package com.decisionpulse.demo.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decisionpulse.demo.data.model.AppView
import com.decisionpulse.demo.ui.theme.*

@Composable
fun ViewSwitcher(
    current: AppView,
    onSelect: (AppView) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        AppView.FARMER          to "Farmer",
        AppView.EXTENSION_OFFICER to "Officer",
        AppView.EXECUTIVE       to "Executive"
    )

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(BgSurface2)
            .border(1.dp, Border2, RoundedCornerShape(10.dp))
            .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items.forEach { (view, label) ->
            val selected = current == view
            val accent = when (view) {
                AppView.FARMER           -> DPGreen
                AppView.EXTENSION_OFFICER -> DPBlue
                AppView.EXECUTIVE        -> DPPurple
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (selected) accent.copy(alpha = 0.18f) else androidx.compose.ui.graphics.Color.Transparent)
                    .clickable { onSelect(view) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text  = label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                        letterSpacing = 0.4.sp
                    ),
                    color = if (selected) accent else TextMuted
                )
            }
        }
    }
}
