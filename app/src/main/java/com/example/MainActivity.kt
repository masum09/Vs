package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.data.DeviceTelemetryManager
import com.example.data.DiagnosticsManager
import com.example.ui.BrandOfSacrificeCanvas
import com.example.ui.DeadPixelTestScreen
import com.example.ui.DiagnosticsScreen
import com.example.ui.MultitouchTestScreen
import com.example.ui.ReportScreen
import com.example.ui.VitalsScreen
import com.example.ui.theme.AshGrey
import com.example.ui.theme.BloodCrimson
import com.example.ui.theme.BloodCrimsonGlow
import com.example.ui.theme.BoneWhite
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.CrimsonTypography
import com.example.ui.theme.DeepGoreRed
import com.example.ui.theme.DragonSlayerIron
import com.example.ui.theme.EclipseGold
import com.example.ui.theme.LiquidGlassBgHigh
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.VoidBlack

enum class AppDestination {
    VITALS,
    DIAGNOSTICS,
    REPORT,
    DEAD_PIXEL_TEST,
    MULTITOUCH_TEST
}

class MainActivity : ComponentActivity() {

    private lateinit var telemetryManager: DeviceTelemetryManager
    private lateinit var diagnosticsManager: DiagnosticsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        telemetryManager = DeviceTelemetryManager(applicationContext)
        diagnosticsManager = DiagnosticsManager(applicationContext)

        telemetryManager.startTelemetry(lifecycleScope)

        setContent {
            MyApplicationTheme {
                val vitals by telemetryManager.vitals.collectAsState()
                var currentScreen by remember { mutableStateOf(AppDestination.VITALS) }

                // Fullscreen tests bypass standard Scaffold
                when (currentScreen) {
                    AppDestination.DEAD_PIXEL_TEST -> {
                        DeadPixelTestScreen(onExit = { currentScreen = AppDestination.DIAGNOSTICS })
                    }
                    AppDestination.MULTITOUCH_TEST -> {
                        MultitouchTestScreen(onExit = { currentScreen = AppDestination.DIAGNOSTICS })
                    }
                    else -> {
                        // Handle back button on sub-screens
                        if (currentScreen != AppDestination.VITALS) {
                            BackHandler {
                                currentScreen = AppDestination.VITALS
                            }
                        }

                        Scaffold(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(VoidBlack),
                            containerColor = VoidBlack,
                            bottomBar = {
                                CrimsonBottomBar(
                                    currentDestination = currentScreen,
                                    onSelectDestination = { currentScreen = it }
                                )
                            }
                        ) { paddingValues ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues)
                            ) {
                                AnimatedContent(
                                    targetState = currentScreen,
                                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                                    label = "screen_transition"
                                ) { screen ->
                                    when (screen) {
                                        AppDestination.VITALS -> {
                                            VitalsScreen(
                                                vitals = vitals,
                                                onNavigateToDiagnostics = {
                                                    currentScreen = AppDestination.DIAGNOSTICS
                                                }
                                            )
                                        }
                                        AppDestination.DIAGNOSTICS -> {
                                            DiagnosticsScreen(
                                                diagnosticsManager = diagnosticsManager,
                                                onLaunchDeadPixelTest = {
                                                    currentScreen = AppDestination.DEAD_PIXEL_TEST
                                                },
                                                onLaunchMultitouchTest = {
                                                    currentScreen = AppDestination.MULTITOUCH_TEST
                                                }
                                            )
                                        }
                                        AppDestination.REPORT -> {
                                            ReportScreen(
                                                vitals = vitals,
                                                telemetryManager = telemetryManager
                                            )
                                        }
                                        else -> Unit
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        telemetryManager.stopTelemetry()
        diagnosticsManager.releaseAll()
    }
}

@Composable
fun CrimsonBottomBar(
    currentDestination: AppDestination,
    onSelectDestination: (AppDestination) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color(0xD912080F),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Brush.horizontalGradient(
                colors = listOf(
                    BloodCrimson.copy(alpha = 0.5f),
                    DragonSlayerIron,
                    BloodCrimson.copy(alpha = 0.3f)
                )
            )
        ),
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CrimsonNavItem(
                title = "Vitals",
                icon = Icons.Default.Speed,
                isSelected = currentDestination == AppDestination.VITALS,
                onClick = { onSelectDestination(AppDestination.VITALS) },
                testTag = "nav_vitals"
            )

            CrimsonNavItem(
                title = "Diagnostics",
                icon = Icons.Default.Build,
                isSelected = currentDestination == AppDestination.DIAGNOSTICS,
                onClick = { onSelectDestination(AppDestination.DIAGNOSTICS) },
                testTag = "nav_diagnostics"
            )

            CrimsonNavItem(
                title = "Dossier",
                icon = Icons.Default.Description,
                isSelected = currentDestination == AppDestination.REPORT,
                onClick = { onSelectDestination(AppDestination.REPORT) },
                testTag = "nav_report"
            )
        }
    }
}

@Composable
fun CrimsonNavItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val bgModifier = if (isSelected) {
        Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(DeepGoreRed.copy(alpha = 0.8f), BloodCrimson.copy(alpha = 0.35f))
                )
            )
            .border(1.dp, BloodCrimsonGlow.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
    } else {
        Modifier
    }

    Box(
        modifier = Modifier
            .testTag(testTag)
            .then(bgModifier)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) BoneWhite else AshGrey,
                modifier = Modifier.size(20.dp)
            )
            if (isSelected) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title.uppercase(),
                    style = CrimsonTypography.labelSmall.copy(
                        fontFamily = CinzelFontFamily,
                        color = BoneWhite,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
            }
        }
    }
}
