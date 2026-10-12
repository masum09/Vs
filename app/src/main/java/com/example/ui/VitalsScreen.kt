package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
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
import com.example.ui.theme.VoidBlack
import java.util.Locale

@Composable
fun VitalsScreen(
    vitals: DeviceVitalsState,
    onNavigateToDiagnostics: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("ALL") }

    val categories = listOf(
        "ALL" to "All Vitals",
        "POWER" to "Power & Cell",
        "MEMORY" to "Memory & Disk",
        "SILICON" to "Compute & Panel",
        "SENSORS" to "Sensors Live",
        "SYSTEM" to "OS & Kernel"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("vitals_dashboard_scroll"),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 100.dp)
    ) {
        // --- HERO HEADER WITH BERSERK ECLIPSE ART ---
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                // Hero Background Image
                Image(
                    painter = painterResource(id = R.drawable.berserk_eclipse_hero_1791798305427),
                    contentDescription = "Berserk Eclipse",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Atmospheric Gradient Overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    VoidBlack.copy(alpha = 0.5f),
                                    VoidBlack
                                )
                            )
                        )
                )

                // Title and Brand Badge
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                BrandOfSacrificeCanvas(
                                    modifier = Modifier.size(24.dp),
                                    runeColor = BloodCrimson
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "ECLIPSE TELEMETRY",
                                    style = CrimsonTypography.labelMedium.copy(
                                        color = EclipseGold,
                                        letterSpacing = 2.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "CRIMSON VITALS",
                                style = CrimsonTypography.displayLarge.copy(
                                    fontFamily = CinzelFontFamily,
                                    letterSpacing = 2.sp,
                                    fontSize = 28.sp
                                )
                            )
                        }

                        // Offline Shield Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x991A0D15))
                                .border(1.dp, BloodCrimson.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(SoulCyan)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "OFFLINE",
                                    style = CrimsonTypography.labelSmall.copy(
                                        color = BoneWhite,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Hardware diagnostics & device vitals forged in obsidian glass.",
                        style = CrimsonTypography.bodyMedium.copy(color = AshGrey)
                    )
                }
            }
        }

        // --- QUICK STATS TICKER STRIP ---
        item {
            QuickStatsStrip(vitals = vitals)
        }

        // --- CATEGORY FILTER TABS ---
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { (key, label) ->
                    val isSelected = selectedCategory == key
                    Surface(
                        modifier = Modifier
                            .testTag("filter_chip_$key")
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { selectedCategory = key },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) BloodCrimson else LiquidGlassBgHigh,
                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(
                            1.dp,
                            DragonSlayerIron
                        )
                    ) {
                        Text(
                            text = label,
                            style = CrimsonTypography.labelMedium.copy(
                                color = if (isSelected) BoneWhite else AshGrey,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            ),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // --- POWER & BATTERY CARD ---
        if (selectedCategory == "ALL" || selectedCategory == "POWER") {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    SectionHeader(title = "Power Cell & Thermals", badgeText = "${vitals.battery.level}%")
                    LiquidGlassCard(testTag = "battery_card") {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CrimsonCircularGauge(
                                percentage = vitals.battery.level.toFloat(),
                                label = "Battery",
                                valueText = "${vitals.battery.level}%",
                                subText = vitals.battery.status,
                                modifier = Modifier.size(116.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                MetricRow(
                                    title = "Status",
                                    value = vitals.battery.status,
                                    icon = Icons.Default.BatteryChargingFull,
                                    valueColor = if (vitals.battery.isCharging) SoulCyan else BoneWhite
                                )
                                MetricRow(
                                    title = "Power Source",
                                    value = vitals.battery.plugType,
                                    icon = Icons.Default.Bolt
                                )
                                MetricRow(
                                    title = "Temperature",
                                    value = String.format(Locale.US, "%.1f°C", vitals.battery.temperatureC),
                                    valueColor = if (vitals.battery.temperatureC > 40f) BloodCrimson else EclipseGold
                                )
                                MetricRow(
                                    title = "Voltage",
                                    value = "${vitals.battery.voltageMv} mV"
                                )
                                MetricRow(
                                    title = "Cell Health",
                                    value = vitals.battery.health,
                                    tag = vitals.battery.technology
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- MEMORY & STORAGE CARDS ---
        if (selectedCategory == "ALL" || selectedCategory == "MEMORY") {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    SectionHeader(title = "Volatile & Persistent Storage")
                    LiquidGlassCard(testTag = "memory_card") {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Memory,
                                        contentDescription = null,
                                        tint = BloodCrimson
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "RAM (PHYSICAL MEMORY)",
                                        style = CrimsonTypography.titleMedium.copy(fontSize = 15.sp)
                                    )
                                }
                                Text(
                                    text = "${vitals.memory.usedPercent.toInt()}% IN USE",
                                    style = CrimsonTypography.labelMedium.copy(color = EclipseGold)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            CrimsonProgressBar(
                                percentage = vitals.memory.usedPercent,
                                barColor = BloodCrimson
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Used: ${vitals.memory.usedFormatted}",
                                    style = CrimsonTypography.bodyMedium.copy(color = AshGrey)
                                )
                                Text(
                                    text = "Available: ${vitals.memory.freeFormatted}",
                                    style = CrimsonTypography.bodyMedium.copy(color = SoulCyan)
                                )
                                Text(
                                    text = "Total: ${vitals.memory.totalFormatted}",
                                    style = CrimsonTypography.bodyMedium.copy(color = BoneWhite)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LiquidGlassCard(testTag = "storage_card") {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Storage,
                                        contentDescription = null,
                                        tint = EclipseGold
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "INTERNAL STORAGE (NAND)",
                                        style = CrimsonTypography.titleMedium.copy(fontSize = 15.sp)
                                    )
                                }
                                Text(
                                    text = "${vitals.storage.usedPercent.toInt()}% USED",
                                    style = CrimsonTypography.labelMedium.copy(color = BloodCrimsonGlow)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            CrimsonProgressBar(
                                percentage = vitals.storage.usedPercent,
                                barColor = EclipseGold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Used: ${vitals.storage.usedFormatted}",
                                    style = CrimsonTypography.bodyMedium.copy(color = AshGrey)
                                )
                                Text(
                                    text = "Free: ${vitals.storage.freeFormatted}",
                                    style = CrimsonTypography.bodyMedium.copy(color = SoulCyan)
                                )
                                Text(
                                    text = "Capacity: ${vitals.storage.totalFormatted}",
                                    style = CrimsonTypography.bodyMedium.copy(color = BoneWhite)
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- SILICON CPU & DISPLAY ---
        if (selectedCategory == "ALL" || selectedCategory == "SILICON") {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    SectionHeader(title = "Silicon Compute & Visual Panel")
                    LiquidGlassCard(testTag = "cpu_card") {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.DeveloperBoard,
                                        contentDescription = null,
                                        tint = BloodCrimson
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "PROCESSOR ARCHITECTURE",
                                        style = CrimsonTypography.titleMedium.copy(fontSize = 15.sp)
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(DeepGoreRed)
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "${vitals.cpu.coreCount} CORES",
                                        style = CrimsonTypography.labelSmall.copy(color = BoneWhite)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            MetricRow(title = "Architecture", value = vitals.cpu.architecture)
                            MetricRow(title = "Hardware SoC", value = vitals.cpu.hardware)
                            MetricRow(title = "Motherboard", value = vitals.cpu.board)
                            MetricRow(
                                title = "Estimated Load",
                                value = "${vitals.cpu.estimatedUsagePercent}%",
                                valueColor = EclipseGold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LiquidGlassCard(testTag = "display_card") {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Tv,
                                    contentDescription = null,
                                    tint = EclipseGold
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "DISPLAY & REFRESH VELOCITY",
                                    style = CrimsonTypography.titleMedium.copy(fontSize = 15.sp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            MetricRow(title = "Resolution", value = vitals.display.resolution)
                            MetricRow(
                                title = "Refresh Rate",
                                value = String.format(Locale.US, "%.1f Hz", vitals.display.refreshRateHz),
                                valueColor = SoulCyan
                            )
                            MetricRow(
                                title = "Screen Density",
                                value = "${vitals.display.densityDpi} DPI (${vitals.display.densityCategory})"
                            )
                            MetricRow(
                                title = "Screen Diagonal",
                                value = String.format(Locale.US, "~%.1f inches", vitals.display.diagonalInches)
                            )
                            MetricRow(
                                title = "HDR Supported",
                                value = if (vitals.display.isHdrSupported) "Yes (Active)" else "No",
                                valueColor = if (vitals.display.isHdrSupported) SoulCyan else DarkAsh
                            )
                        }
                    }
                }
            }
        }

        // --- SENSORS & NETWORK ---
        if (selectedCategory == "ALL" || selectedCategory == "SENSORS") {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    SectionHeader(
                        title = "Live Sensor Telemetry",
                        badgeText = "${vitals.sensors.totalSensorsCount} SENSORS"
                    )
                    LiquidGlassCard(testTag = "sensors_card") {
                        Column {
                            // Interactive G-Force / Tilt Crosshair
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "3D INERTIAL TELEMETRY",
                                        style = CrimsonTypography.labelMedium.copy(color = BloodCrimsonGlow)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "X: ${String.format(Locale.US, "%.2f", vitals.sensors.accelX)} m/s²",
                                        style = CrimsonTypography.bodyMedium
                                    )
                                    Text(
                                        text = "Y: ${String.format(Locale.US, "%.2f", vitals.sensors.accelY)} m/s²",
                                        style = CrimsonTypography.bodyMedium
                                    )
                                    Text(
                                        text = "Z: ${String.format(Locale.US, "%.2f", vitals.sensors.accelZ)} m/s²",
                                        style = CrimsonTypography.bodyMedium
                                    )
                                    Text(
                                        text = "G-Force: ${String.format(Locale.US, "%.2f", vitals.sensors.gForce)} G",
                                        style = CrimsonTypography.bodyMedium.copy(color = EclipseGold)
                                    )
                                }

                                // Interactive Tilt Orb
                                Box(
                                    modifier = Modifier
                                        .size(96.dp)
                                        .clip(CircleShape)
                                        .background(DragonSlayerIron.copy(alpha = 0.5f))
                                        .border(1.dp, BloodCrimson.copy(alpha = 0.4f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        val cx = size.width / 2f
                                        val cy = size.height / 2f
                                        val r = size.width / 2f

                                        // Crosshairs
                                        drawLine(
                                            color = BladeSteel.copy(alpha = 0.3f),
                                            start = Offset(cx, 0f),
                                            end = Offset(cx, size.height),
                                            strokeWidth = 1f
                                        )
                                        drawLine(
                                            color = BladeSteel.copy(alpha = 0.3f),
                                            start = Offset(0f, cy),
                                            end = Offset(size.width, cy),
                                            strokeWidth = 1f
                                        )

                                        // Tilt position (scaled from accelX/accelY)
                                        val offsetX = (-vitals.sensors.accelX / 9.8f) * (r * 0.7f)
                                        val offsetY = (vitals.sensors.accelY / 9.8f) * (r * 0.7f)

                                        val orbX = (cx + offsetX).coerceIn(8f, size.width - 8f)
                                        val orbY = (cy + offsetY).coerceIn(8f, size.height - 8f)

                                        // Orb glow
                                        drawCircle(
                                            brush = Brush.radialGradient(
                                                colors = listOf(BloodCrimson, BloodCrimson.copy(alpha = 0.2f), Color.Transparent),
                                                center = Offset(orbX, orbY),
                                                radius = 16f
                                            )
                                        )
                                        drawCircle(
                                            color = EclipseGold,
                                            center = Offset(orbX, orbY),
                                            radius = 5f
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            MetricRow(
                                title = "Ambient Light",
                                value = "${vitals.sensors.lightLux.toInt()} Lux",
                                icon = Icons.Default.Sensors
                            )
                            MetricRow(
                                title = "Proximity Distance",
                                value = "${vitals.sensors.proximityCm} cm"
                            )
                            MetricRow(
                                title = "Magnetic Field",
                                value = "${vitals.sensors.magneticFieldUt.toInt()} µT"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LiquidGlassCard(testTag = "network_card") {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Wifi,
                                    contentDescription = null,
                                    tint = if (vitals.network.isConnected) SoulCyan else BloodCrimson
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "NETWORK & AIRGAP AUDIT",
                                    style = CrimsonTypography.titleMedium.copy(fontSize = 15.sp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            MetricRow(
                                title = "Connection Type",
                                value = vitals.network.typeName,
                                valueColor = if (vitals.network.isConnected) BoneWhite else BloodCrimson
                            )
                            MetricRow(
                                title = "Local IP Address",
                                value = vitals.network.ipAddress
                            )
                            MetricRow(
                                title = "Link Speed",
                                value = vitals.network.linkSpeed
                            )
                            MetricRow(
                                title = "VPN Active",
                                value = if (vitals.network.isVpn) "Yes" else "No",
                                valueColor = if (vitals.network.isVpn) SoulCyan else AshGrey
                            )
                        }
                    }
                }
            }
        }

        // --- SYSTEM OS & CHASSIS ---
        if (selectedCategory == "ALL" || selectedCategory == "SYSTEM") {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    SectionHeader(title = "Operating System & Chassis")
                    LiquidGlassCard(testTag = "os_card") {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhoneAndroid,
                                    contentDescription = null,
                                    tint = BloodCrimson
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "DEVICE IDENTIFICATION",
                                    style = CrimsonTypography.titleMedium.copy(fontSize = 15.sp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            MetricRow(title = "Device Model", value = vitals.os.deviceModel)
                            MetricRow(title = "Manufacturer", value = vitals.os.manufacturer)
                            MetricRow(title = "Brand", value = vitals.os.brand)
                            MetricRow(
                                title = "Android Release",
                                value = "Android ${vitals.os.androidVersion} (API ${vitals.os.apiLevel})",
                                valueColor = SoulCyan
                            )
                            MetricRow(title = "Security Patch", value = vitals.os.securityPatch)
                            MetricRow(title = "Kernel", value = vitals.os.kernelVersion.take(24))
                            MetricRow(title = "Bootloader", value = vitals.os.bootloader)
                            MetricRow(title = "System Uptime", value = vitals.os.uptimeFormatted, valueColor = EclipseGold)
                            MetricRow(title = "Build ID", value = vitals.os.buildId.take(20))
                        }
                    }
                }
            }
        }

        // --- HARDWARE DIAGNOSTICS QUICK CTA ---
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                LiquidGlassButton(
                    text = "Launch Hardware Diagnostics Arena",
                    onClick = onNavigateToDiagnostics,
                    icon = Icons.Default.Speed,
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "launch_diagnostics_button"
                )
            }
        }
    }
}

@Composable
fun QuickStatsStrip(vitals: DeviceVitalsState) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color(0x66180B14),
        border = androidx.compose.foundation.BorderStroke(1.dp, BloodCrimson.copy(alpha = 0.25f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            QuickStatItem(
                label = "BATTERY",
                value = "${vitals.battery.level}%",
                color = if (vitals.battery.isCharging) SoulCyan else BloodCrimson
            )
            Box(modifier = Modifier.size(1.dp, 24.dp).background(DragonSlayerIron))
            QuickStatItem(
                label = "RAM LOAD",
                value = "${vitals.memory.usedPercent.toInt()}%",
                color = EclipseGold
            )
            Box(modifier = Modifier.size(1.dp, 24.dp).background(DragonSlayerIron))
            QuickStatItem(
                label = "CPU CORES",
                value = "${vitals.cpu.coreCount}",
                color = BoneWhite
            )
            Box(modifier = Modifier.size(1.dp, 24.dp).background(DragonSlayerIron))
            QuickStatItem(
                label = "UPTIME",
                value = vitals.os.uptimeFormatted,
                color = AshGrey
            )
        }
    }
}

@Composable
fun QuickStatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = CrimsonTypography.labelSmall.copy(
                fontSize = 9.sp,
                color = DarkAsh,
                letterSpacing = 1.sp
            )
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = CrimsonTypography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                color = color
            )
        )
    }
}
