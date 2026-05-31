package com.decisionpulse.demo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.decisionpulse.demo.ui.theme.*

@Composable
fun LiveMetricTile(
    label: String,
    value: Double,
    suffix: String = "",
    decimals: Int = 0,
    accent: Color = DPGreen,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(BgSurface2)
            .padding(14.dp)
    ) {
        Text(label.uppercase(), style = MaterialTheme.typography.labelSmall)
        Spacer(Modifier.height(6.dp))
        CountingNumber(
            target   = value,
            suffix   = suffix,
            decimals = decimals,
            style    = MaterialTheme.typography.headlineLarge,
            color    = accent
        )
    }
}