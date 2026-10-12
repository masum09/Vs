package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AshGrey
import com.example.ui.theme.BloodCrimson
import com.example.ui.theme.BloodCrimsonGlow
import com.example.ui.theme.BoneWhite
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.CrimsonTypography
import com.example.ui.theme.DarkAsh
import com.example.ui.theme.EclipseGold
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.VoidBlack
import kotlin.math.hypot

data class ScreenColorTestItem(
    val name: String,
    val color: Color,
    val description: String
)

@Composable
fun DeadPixelTestScreen(
    onExit: () -> Unit
) {
    BackHandler(onBack = onExit)

    val colorList = remember {
        listOf(
            ScreenColorTestItem("PITCH OBSIDIAN (BLACK)", Color(0xFF000000), "Tests OLED true blacks & backlight light bleed"),
            ScreenColorTestItem("PURE WHITE", Color(0xFFFFFFFF), "Tests screen uniformity, clouding & stuck dark pixels"),
            ScreenColorTestItem("BLOOD CRIMSON (RED)", Color(0xFFFF0000), "Tests red subpixel integrity"),
            ScreenColorTestItem("EMERALD GREEN", Color(0xFF00FF00), "Tests green subpixel integrity"),
            ScreenColorTestItem("ROYAL BLUE", Color(0xFF0000FF), "Tests blue subpixel integrity"),
            ScreenColorTestItem("CYBER YELLOW", Color(0xFFFFFF00), "Tests red + green subpixel combination"),
            ScreenColorTestItem("GLOW CYAN", Color(0xFF00FFFF), "Tests green + blue subpixel combination"),
            ScreenColorTestItem("NEON MAGENTA", Color(0xFFFF00FF), "Tests red + blue subpixel combination"),
            ScreenColorTestItem("50% NEUTRAL GREY", Color(0xFF808080), "Tests panel gamma & uniformity banding")
        )
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var showHud by remember { mutableStateOf(true) }

    val currentSwatch = colorList[currentIndex]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("dead_pixel_canvas")
            .background(currentSwatch.color)
            .clickable {
                // If HUD is shown, tap advances and toggles HUD; tap again hides
                currentIndex = (currentIndex + 1) % colorList.size
            }
    ) {
        // Floating Semi-transparent Control HUD
        AnimatedVisibility(
            visible = showHud,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xCC0E070C),
                border = androidx.compose.foundation.BorderStroke(1.dp, BloodCrimson.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentSwatch.name,
                            style = CrimsonTypography.titleMedium.copy(
                                color = BoneWhite,
                                letterSpacing = 1.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${currentSwatch.description} • Tap anywhere to cycle (${currentIndex + 1}/${colorList.size})",
                            style = CrimsonTypography.labelSmall.copy(color = AshGrey)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { showHud = !showHud },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0x33FFFFFF))
                        ) {
                            Text(
                                text = "HUD",
                                style = CrimsonTypography.labelSmall.copy(color = EclipseGold, fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = onExit,
                            modifier = Modifier
                                .testTag("exit_dead_pixel_button")
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(BloodCrimson)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Exit Dead Pixel Test",
                                tint = BoneWhite
                            )
                        }
                    }
                }
            }
        }

        // Bottom subtle hint if HUD is hidden
        if (!showHud) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
                    .clickable { showHud = true },
                shape = RoundedCornerShape(20.dp),
                color = Color(0x80000000)
            ) {
                Text(
                    text = "Tap for HUD • Tap anywhere to cycle",
                    style = CrimsonTypography.labelSmall.copy(color = BoneWhite),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun MultitouchTestScreen(
    onExit: () -> Unit
) {
    BackHandler(onBack = onExit)

    val touches = remember { mutableStateMapOf<PointerId, Offset>() }
    var peakTouches by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("multitouch_arena")
            .background(VoidBlack)
            .pointerInput(Unit) {
                awaitEachGesture {
                    val firstDown = awaitFirstDown(requireUnconsumed = false)
                    touches[firstDown.id] = firstDown.position
                    if (touches.size > peakTouches) {
                        peakTouches = touches.size
                    }

                    do {
                        val event = awaitPointerEvent()
                        val currentPointers = event.changes
                        for (change in currentPointers) {
                            if (change.pressed) {
                                touches[change.id] = change.position
                            } else {
                                touches.remove(change.id)
                            }
                        }
                        if (touches.size > peakTouches) {
                            peakTouches = touches.size
                        }
                    } while (event.changes.any { it.pressed })

                    touches.clear()
                }
            }
    ) {
        // Touch Render Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val touchList = touches.values.toList()

            // Draw connection lines between pointers if multiple
            if (touchList.size >= 2) {
                for (i in touchList.indices) {
                    for (j in i + 1 until touchList.size) {
                        val p1 = touchList[i]
                        val p2 = touchList[j]
                        drawLine(
                            color = BloodCrimson.copy(alpha = 0.45f),
                            start = p1,
                            end = p2,
                            strokeWidth = 3f,
                            pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                        )
                    }
                }
            }

            // Draw glowing circles and runes around each touch
            touches.entries.forEachIndexed { index, entry ->
                val pos = entry.value
                val color = when (index % 4) {
                    0 -> BloodCrimson
                    1 -> EclipseGold
                    2 -> BloodCrimsonGlow
                    else -> Color(0xFFFF5252)
                }

                // Outer pulsing ring
                drawCircle(
                    color = color.copy(alpha = 0.25f),
                    center = pos,
                    radius = 90f
                )
                // Mid ring
                drawCircle(
                    color = color.copy(alpha = 0.6f),
                    center = pos,
                    radius = 60f,
                    style = Stroke(width = 4f)
                )
                // Inner center point
                drawCircle(
                    color = BoneWhite,
                    center = pos,
                    radius = 14f
                )
                drawCircle(
                    color = color,
                    center = pos,
                    radius = 8f
                )
            }
        }

        // Top HUD Overlay
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xCC140A12),
            border = androidx.compose.foundation.BorderStroke(1.dp, BloodCrimson.copy(alpha = 0.4f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BrandOfSacrificeCanvas(modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "MULTITOUCH ARENA",
                            style = CrimsonTypography.titleMedium.copy(
                                color = BoneWhite,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Text(
                            text = "ACTIVE: ${touches.size} FINGERS",
                            style = CrimsonTypography.labelMedium.copy(
                                color = BloodCrimsonGlow,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "PEAK: $peakTouches",
                            style = CrimsonTypography.labelMedium.copy(
                                color = EclipseGold,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { peakTouches = 0 },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0x26FFFFFF))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset Peak",
                            tint = BoneWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = onExit,
                        modifier = Modifier
                            .testTag("exit_multitouch_button")
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(BloodCrimson)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Exit Multitouch",
                            tint = BoneWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Real-time touch coordinates list at the bottom
        if (touches.isNotEmpty()) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp, start = 16.dp, end = 16.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xB3140A12)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    touches.entries.take(4).forEachIndexed { i, entry ->
                        Text(
                            text = "#$i: (${entry.value.x.toInt()}, ${entry.value.y.toInt()})",
                            style = CrimsonTypography.bodyMedium.copy(
                                fontFamily = JetBrainsMonoFontFamily,
                                color = AshGrey
                            )
                        )
                    }
                }
            }
        } else {
            // Instructions when no touch
            Surface(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(32.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color(0x66180D15)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BrandOfSacrificeCanvas(modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "TOUCH THE SCREEN WITH MULTIPLE FINGERS",
                        style = CrimsonTypography.labelMedium.copy(
                            color = BoneWhite,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tests digitizer polling rate, multi-point tracking & ghost touches",
                        style = CrimsonTypography.labelSmall.copy(color = AshGrey)
                    )
                }
            }
        }
    }
}
