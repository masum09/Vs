package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CrimsonDarkColorScheme = darkColorScheme(
    primary = BloodCrimson,
    onPrimary = BoneWhite,
    primaryContainer = DeepGoreRed,
    onPrimaryContainer = BoneWhite,
    secondary = EclipseGold,
    onSecondary = VoidBlack,
    secondaryContainer = DragonSlayerIron,
    onSecondaryContainer = EclipseGoldGlow,
    tertiary = BloodCrimsonGlow,
    onTertiary = VoidBlack,
    background = VoidBlack,
    onBackground = BoneWhite,
    surface = ObsidianSurface,
    onSurface = BoneWhite,
    surfaceVariant = ObsidianCard,
    onSurfaceVariant = AshGrey,
    outline = BloodCrimson.copy(alpha = 0.4f),
    outlineVariant = DragonSlayerIron
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CrimsonDarkColorScheme,
        typography = CrimsonTypography,
        content = content
    )
}
