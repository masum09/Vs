package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Berserk Eclipse Dark Palette
val VoidBlack = Color(0xFF090507)
val ObsidianSurface = Color(0xFF13090F)
val ObsidianCard = Color(0xFF1A0D15)
val LiquidGlassBg = Color(0x8A1F0D18)
val LiquidGlassBgHigh = Color(0xA6260F1E)

val BloodCrimson = Color(0xFFFF1A3C)
val BloodCrimsonGlow = Color(0xFFFF4D6A)
val DeepGoreRed = Color(0xFF8A0314)
val DragonSlayerIron = Color(0xFF2C2428)
val BladeSteel = Color(0xFF8A8287)
val BoneWhite = Color(0xFFF7EFF2)
val AshGrey = Color(0xFFB5A4AB)
val DarkAsh = Color(0xFF6E5E65)

val EclipseGold = Color(0xFFE5A93C)
val EclipseGoldGlow = Color(0xFFFFC966)
val SoulCyan = Color(0xFF38EF7D)

val BerserkBorderGlow = Brush.linearGradient(
    colors = listOf(
        Color(0x80FF1A3C),
        Color(0x20FF1A3C),
        Color(0x40E5A93C),
        Color(0x20FF1A3C)
    )
)

val LiquidGlassRim = Brush.verticalGradient(
    colors = listOf(
        Color(0x40FFFFFF),
        Color(0x10FF1A3C),
        Color(0x05000000)
    )
)

val CrimsonGradient = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFF8A0314),
        Color(0xFFFF1A3C),
        Color(0xFFFF5270)
    )
)
