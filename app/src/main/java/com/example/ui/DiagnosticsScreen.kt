package com.example.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AudioChannel
import com.example.data.DiagnosticsManager
import com.example.data.TorchMode
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

@Composable
fun DiagnosticsScreen(
    diagnosticsManager: DiagnosticsManager,
    onLaunchDeadPixelTest: () -> Unit,
    onLaunchMultitouchTest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by diagnosticsManager.state.collectAsState()
    val scope = rememberCoroutineScope()

    var strobeSpeed by remember { mutableFloatStateOf(6f) }

    DisposableEffect(Unit) {
        onDispose {
            diagnosticsManager.releaseAll()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("diagnostics_screen_scroll"),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            top = 20.dp,
            bottom = 100.dp,
            start = 16.dp,
            end = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- SCREEN TITLE ---
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BrandOfSacrificeCanvas(modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "THE RECKONING FORGE",
                        style = CrimsonTypography.labelMedium.copy(
                            color = EclipseGold,
                            letterSpacing = 1.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "HARDWARE DIAGNOSTICS",
                    style = CrimsonTypography.displayMedium.copy(
                        fontFamily = CinzelFontFamily,
                        letterSpacing = 1.2.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Stress test physical sub-components: OLED pixels, digitizer, flashlight, haptics & acoustic drivers.",
                    style = CrimsonTypography.bodyMedium.copy(color = AshGrey)
                )
            }
        }

        // --- FULLSCREEN VISUAL & TOUCH TESTS ---
        item {
            SectionHeader(title = "Visual & Input Calibration")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Dead Pixel Test Card
                LiquidGlassCard(
                    modifier = Modifier.weight(1f),
                    onClick = onLaunchDeadPixelTest,
                    testTag = "launch_dead_pixel_card"
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(DeepGoreRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tv,
                                contentDescription = null,
                                tint = BoneWhite
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "DEAD PIXEL TEST",
                            style = CrimsonTypography.titleMedium.copy(fontSize = 13.sp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Full-panel color cycle & OLED uniformity inspection",
                            style = CrimsonTypography.labelSmall.copy(color = AshGrey),
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "START TEST →",
                            style = CrimsonTypography.labelSmall.copy(
                                color = BloodCrimsonGlow,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                // Multitouch Test Card
                LiquidGlassCard(
                    modifier = Modifier.weight(1f),
                    onClick = onLaunchMultitouchTest,
                    testTag = "launch_multitouch_card"
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(DragonSlayerIron),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.TouchApp,
                                contentDescription = null,
                                tint = EclipseGold
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "MULTITOUCH ARENA",
                            style = CrimsonTypography.titleMedium.copy(fontSize = 13.sp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "10-finger coordinate tracking & digitizer latency",
                            style = CrimsonTypography.labelSmall.copy(color = AshGrey),
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "LAUNCH ARENA →",
                            style = CrimsonTypography.labelSmall.copy(
                                color = EclipseGold,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }

        // --- FLASHLIGHT & TORCH CONTROLS ---
        item {
            SectionHeader(
                title = "Flashlight & Optical Torch",
                badgeText = if (state.isTorchOn) "ACTIVE" else "STANDBY"
            )
            LiquidGlassCard(testTag = "torch_card") {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FlashlightOn,
                                contentDescription = null,
                                tint = if (state.isTorchOn) EclipseGold else AshGrey
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "TORCH OPERATING MODE",
                                style = CrimsonTypography.titleMedium.copy(fontSize = 15.sp)
                            )
                        }

                        if (state.torchError != null) {
                            Text(
                                text = "Virtual Mode",
                                style = CrimsonTypography.labelSmall.copy(color = EclipseGold)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Mode Selection Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TorchModeButton(
                            label = "OFF",
                            isSelected = state.torchMode == TorchMode.OFF,
                            onClick = { diagnosticsManager.setTorchMode(TorchMode.OFF, scope = scope) },
                            modifier = Modifier.weight(1f)
                        )
                        TorchModeButton(
                            label = "STEADY",
                            isSelected = state.torchMode == TorchMode.SOLID,
                            onClick = { diagnosticsManager.setTorchMode(TorchMode.SOLID, scope = scope) },
                            modifier = Modifier.weight(1f)
                        )
                        TorchModeButton(
                            label = "STROBE",
                            isSelected = state.torchMode == TorchMode.STROBE,
                            onClick = {
                                diagnosticsManager.setTorchMode(
                                    TorchMode.STROBE,
                                    strobeSpeedHz = strobeSpeed.toInt(),
                                    scope = scope
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                        TorchModeButton(
                            label = "SOS",
                            isSelected = state.torchMode == TorchMode.SOS,
                            onClick = { diagnosticsManager.setTorchMode(TorchMode.SOS, scope = scope) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (state.torchMode == TorchMode.STROBE) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Strobe Frequency: ${strobeSpeed.toInt()} Hz",
                                style = CrimsonTypography.bodyMedium.copy(color = EclipseGold)
                            )
                        }
                        Slider(
                            value = strobeSpeed,
                            onValueChange = {
                                strobeSpeed = it
                                diagnosticsManager.setTorchMode(
                                    TorchMode.STROBE,
                                    strobeSpeedHz = it.toInt(),
                                    scope = scope
                                )
                            },
                            valueRange = 1f..15f,
                            steps = 13,
                            colors = SliderDefaults.colors(
                                thumbColor = BloodCrimson,
                                activeTrackColor = BloodCrimson,
                                inactiveTrackColor = DragonSlayerIron
                            )
                        )
                    }
                }
            }
        }

        // --- HAPTIC & VIBRATION FORGE ---
        item {
            SectionHeader(
                title = "Haptic & Vibration Forge",
                badgeText = if (state.isVibrating) "PULSING" else "READY"
            )
            LiquidGlassCard(testTag = "vibration_card") {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Vibration,
                                contentDescription = null,
                                tint = BloodCrimson
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "TACTILE CADENCE PATTERNS",
                                style = CrimsonTypography.titleMedium.copy(fontSize = 15.sp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            VibrationPatternButton(
                                title = "DRAGONSLAYER SLASH",
                                subtitle = "Heavy recoil impact",
                                onClick = { diagnosticsManager.testVibration("IMPACT", scope) },
                                modifier = Modifier.weight(1f)
                            )
                            VibrationPatternButton(
                                title = "BRAND HEARTBEAT",
                                subtitle = "Double throbbing pulse",
                                onClick = { diagnosticsManager.testVibration("HEARTBEAT", scope) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            VibrationPatternButton(
                                title = "BERSERK ROAR",
                                subtitle = "Ramping frequency wave",
                                onClick = { diagnosticsManager.testVibration("ROAR", scope) },
                                modifier = Modifier.weight(1f)
                            )
                            VibrationPatternButton(
                                title = "TACTICAL TICK",
                                subtitle = "Sharp discrete tap",
                                onClick = { diagnosticsManager.testVibration("TICK", scope) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        // --- ACOUSTIC & SPEAKER TONE TEST ---
        item {
            SectionHeader(
                title = "Acoustic & Speaker Tone Test",
                badgeText = if (state.isAudioPlaying) "PLAYING (${state.audioFrequencyHz}Hz)" else "MUTED"
            )
            LiquidGlassCard(testTag = "audio_test_card") {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = null,
                                tint = if (state.isAudioPlaying) SoulCyan else AshGrey
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "SINE WAVE SYNTHESIZER",
                                style = CrimsonTypography.titleMedium.copy(fontSize = 15.sp)
                            )
                        }

                        // Play / Stop Toggle
                        Box(
                            modifier = Modifier
                                .testTag("toggle_audio_button")
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (state.isAudioPlaying) BloodCrimson else DeepGoreRed)
                                .clickable { diagnosticsManager.toggleAudioTone(scope) }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (state.isAudioPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = BoneWhite,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (state.isAudioPlaying) "STOP" else "START TONE",
                                    style = CrimsonTypography.labelSmall.copy(
                                        color = BoneWhite,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Equalizer Visualizer Bars if playing
                    if (state.isAudioPlaying) {
                        AudioEqualizerAnimation(
                            frequency = state.audioFrequencyHz,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Frequency Quick Preset Chips
                    Text(
                        text = "Frequency Presets:",
                        style = CrimsonTypography.labelSmall.copy(color = DarkAsh)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(100 to "100Hz", 250 to "250Hz", 440 to "440Hz", 1000 to "1kHz", 4000 to "4kHz").forEach { (freq, label) ->
                            val isCurrent = state.audioFrequencyHz == freq
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        diagnosticsManager.setAudioFrequency(freq)
                                        if (state.isAudioPlaying) {
                                            diagnosticsManager.startAudioTone(scope)
                                        }
                                    },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isCurrent) BloodCrimson else LiquidGlassBgHigh,
                                border = androidx.compose.foundation.BorderStroke(1.dp, DragonSlayerIron)
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        style = CrimsonTypography.labelSmall.copy(
                                            color = if (isCurrent) BoneWhite else AshGrey,
                                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Frequency Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Target Frequency",
                            style = CrimsonTypography.bodyMedium.copy(color = AshGrey)
                        )
                        Text(
                            text = "${state.audioFrequencyHz} Hz",
                            style = CrimsonTypography.bodyLarge.copy(
                                color = EclipseGold,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Slider(
                        value = state.audioFrequencyHz.toFloat(),
                        onValueChange = {
                            diagnosticsManager.setAudioFrequency(it.toInt())
                        },
                        valueRange = 80f..8000f,
                        colors = SliderDefaults.colors(
                            thumbColor = BloodCrimson,
                            activeTrackColor = BloodCrimson,
                            inactiveTrackColor = DragonSlayerIron
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Stereo Channel Routing
                    Text(
                        text = "Acoustic Channel Balance:",
                        style = CrimsonTypography.labelSmall.copy(color = DarkAsh)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AudioChannelButton(
                            label = "LEFT ONLY",
                            isSelected = state.audioChannel == AudioChannel.LEFT_ONLY,
                            onClick = { diagnosticsManager.setAudioChannel(AudioChannel.LEFT_ONLY) },
                            modifier = Modifier.weight(1f)
                        )
                        AudioChannelButton(
                            label = "STEREO (BOTH)",
                            isSelected = state.audioChannel == AudioChannel.STEREO,
                            onClick = { diagnosticsManager.setAudioChannel(AudioChannel.STEREO) },
                            modifier = Modifier.weight(1f)
                        )
                        AudioChannelButton(
                            label = "RIGHT ONLY",
                            isSelected = state.audioChannel == AudioChannel.RIGHT_ONLY,
                            onClick = { diagnosticsManager.setAudioChannel(AudioChannel.RIGHT_ONLY) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TorchModeButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) BloodCrimson else LiquidGlassBgHigh,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) BloodCrimsonGlow else DragonSlayerIron)
    ) {
        Box(
            modifier = Modifier.padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = CrimsonTypography.labelMedium.copy(
                    color = if (isSelected) BoneWhite else AshGrey,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            )
        }
    }
}

@Composable
fun VibrationPatternButton(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = LiquidGlassBgHigh,
        border = androidx.compose.foundation.BorderStroke(1.dp, DragonSlayerIron)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                style = CrimsonTypography.titleMedium.copy(fontSize = 12.sp, color = BoneWhite)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = CrimsonTypography.labelSmall.copy(color = AshGrey)
            )
        }
    }
}

@Composable
fun AudioChannelButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) BloodCrimson else LiquidGlassBgHigh,
        border = androidx.compose.foundation.BorderStroke(1.dp, DragonSlayerIron)
    ) {
        Box(
            modifier = Modifier.padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = CrimsonTypography.labelSmall.copy(
                    color = if (isSelected) BoneWhite else AshGrey,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            )
        }
    }
}

@Composable
fun AudioEqualizerAnimation(frequency: Int, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "eq_anim")

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        for (i in 0 until 18) {
            val anim by infiniteTransition.animateFloat(
                initialValue = 0.15f,
                targetValue = 0.95f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 200 + (i * 45), easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "bar_$i"
            )
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .fillMaxHeight(anim)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(BloodCrimson, EclipseGold)
                        )
                    )
            )
        }
    }
}
