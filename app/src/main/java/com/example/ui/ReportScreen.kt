package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DeviceTelemetryManager
import com.example.data.DeviceVitalsState
import com.example.ui.theme.AshGrey
import com.example.ui.theme.BladeSteel
import com.example.ui.theme.BloodCrimson
import com.example.ui.theme.BloodCrimsonGlow
import com.example.ui.theme.BoneWhite
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.CrimsonTypography
import com.example.ui.theme.DarkAsh
import com.example.ui.theme.DeepGoreRed
import com.example.ui.theme.DragonSlayerIron
import com.example.ui.theme.EclipseGold
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.LiquidGlassBgHigh
import com.example.ui.theme.SoulCyan
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ReportScreen(
    vitals: DeviceVitalsState,
    telemetryManager: DeviceTelemetryManager,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isCopied by remember { mutableStateOf(false) }

    val reportText = remember(vitals) {
        telemetryManager.generateAuditReport()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("report_screen_scroll"),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            top = 20.dp,
            bottom = 100.dp,
            start = 16.dp,
            end = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- HEADER ---
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BrandOfSacrificeCanvas(modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "HARDWARE DOSSIER",
                        style = CrimsonTypography.labelMedium.copy(
                            color = EclipseGold,
                            letterSpacing = 1.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "SPECIFICATION AUDIT",
                    style = CrimsonTypography.displayMedium.copy(
                        fontFamily = CinzelFontFamily,
                        letterSpacing = 1.2.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Exportable cryptographic-style device breakdown. Formatted for copy or dispatch.",
                    style = CrimsonTypography.bodyMedium.copy(color = AshGrey)
                )
            }
        }

        // --- HARDWARE TIER RATING CARD ---
        item {
            LiquidGlassCard(testTag = "hardware_tier_card") {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "BERSERK POWER TIER",
                            style = CrimsonTypography.labelSmall.copy(
                                color = EclipseGold,
                                letterSpacing = 1.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(DeepGoreRed)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "VERIFIED LOCAL",
                                style = CrimsonTypography.labelSmall.copy(color = BoneWhite)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (vitals.cpu.coreCount >= 8 && vitals.display.refreshRateHz >= 90f) {
                            "BERSERKER TITAN (S+ TIER)"
                        } else if (vitals.cpu.coreCount >= 6) {
                            "DRAGON SLAYER (S TIER)"
                        } else {
                            "BLACK SWORDSMAN (A TIER)"
                        },
                        style = CrimsonTypography.titleLarge.copy(
                            fontFamily = CinzelFontFamily,
                            color = BloodCrimsonGlow,
                            fontSize = 22.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Spec Highlights
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TierPill(label = "${vitals.cpu.coreCount} CORES", modifier = Modifier.weight(1f))
                        TierPill(label = vitals.memory.totalFormatted, modifier = Modifier.weight(1f))
                        TierPill(label = "${vitals.display.refreshRateHz.toInt()} Hz", modifier = Modifier.weight(1f))
                        TierPill(label = "BATT: ${vitals.battery.level}%", modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // --- EXPORT & COPY ACTION BUTTONS ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Copy Button
                LiquidGlassButton(
                    text = if (isCopied) "Copied!" else "Copy Dossier",
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Device Hardware Audit", reportText)
                        clipboard.setPrimaryClip(clip)
                        isCopied = true
                        scope.launch {
                            delay(2500)
                            isCopied = false
                        }
                    },
                    icon = if (isCopied) Icons.Default.Check else Icons.Default.ContentCopy,
                    modifier = Modifier.weight(1f),
                    testTag = "copy_report_button"
                )

                // Share Button
                LiquidGlassButton(
                    text = "Share Report",
                    onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, reportText)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, "Share Hardware Audit Report")
                        context.startActivity(shareIntent)
                    },
                    icon = Icons.Default.Share,
                    isPrimary = false,
                    modifier = Modifier.weight(1f),
                    testTag = "share_report_button"
                )
            }
        }

        // Copy Feedback Notification
        if (isCopied) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0x3338EF7D),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SoulCyan)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = SoulCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Hardware dossier copied to system clipboard.",
                            style = CrimsonTypography.bodyMedium.copy(color = BoneWhite)
                        )
                    }
                }
            }
        }

        // --- FULL TEXT REPORT PREVIEW CONTAINER ---
        item {
            SectionHeader(title = "Hardware Dossier Log")
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("report_text_box"),
                shape = RoundedCornerShape(16.dp),
                color = Color(0x990F070D),
                border = androidx.compose.foundation.BorderStroke(1.dp, BladeSteel.copy(alpha = 0.25f))
            ) {
                Text(
                    text = reportText,
                    style = CrimsonTypography.bodyMedium.copy(
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 11.sp,
                        lineHeight = 16.sp,
                        color = AshGrey
                    ),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // --- PRIVACY STATEMENT BADGE ---
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = LiquidGlassBgHigh,
                border = androidx.compose.foundation.BorderStroke(1.dp, DragonSlayerIron)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = SoulCyan,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "AIRGAP PRIVACY VERIFIED",
                            style = CrimsonTypography.titleMedium.copy(fontSize = 13.sp, color = BoneWhite)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Crimson Vitals is 100% offline. No telemetry, identifiers, advertising SDKs, or analytics trackers are present. All audit evaluations are computed in volatile device RAM.",
                            style = CrimsonTypography.bodyMedium.copy(fontSize = 11.sp, color = DarkAsh)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TierPill(label: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = Color(0x4D2C2428),
        border = androidx.compose.foundation.BorderStroke(1.dp, DragonSlayerIron)
    ) {
        Box(
            modifier = Modifier.padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = CrimsonTypography.labelSmall.copy(
                    fontFamily = JetBrainsMonoFontFamily,
                    color = BoneWhite,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
