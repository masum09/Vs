package com.example.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ui.theme.LiquidGlassBg
import com.example.ui.theme.LiquidGlassBgHigh

@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    borderColor: Color = BloodCrimson.copy(alpha = 0.35f),
    contentColor: Color = BoneWhite,
    shapeRadius: Dp = 20.dp,
    onClick: (() -> Unit)? = null,
    testTag: String = "liquid_glass_card",
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(shapeRadius)
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = modifier
            .testTag(testTag)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = ripple(color = BloodCrimsonGlow),
                        onClick = onClick
                    )
                } else Modifier
            ),
        shape = shape,
        color = LiquidGlassBg,
        border = BorderStroke(
            1.dp,
            Brush.linearGradient(
                colors = listOf(
                    borderColor,
                    Color(0x18FFFFFF),
                    borderColor.copy(alpha = 0.15f)
                )
            )
        ),
        tonalElevation = 6.dp,
        contentColor = contentColor
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x1AFFFFFF),
                            Color(0x05FF1A3C),
                            Color(0x00000000)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            content()
        }
    }
}

@Composable
fun BrandOfSacrificeCanvas(
    modifier: Modifier = Modifier,
    runeColor: Color = BloodCrimson,
    glowEffect: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "brand_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.65f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f

        // Outer eclipse aura
        if (glowEffect) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        runeColor.copy(alpha = 0.35f * pulseAlpha),
                        runeColor.copy(alpha = 0.08f * pulseAlpha),
                        Color.Transparent
                    ),
                    center = Offset(cx, cy),
                    radius = w * 0.48f
                )
            )
        }

        val stroke = Stroke(
            width = (w * 0.065f).coerceAtLeast(3f),
            cap = StrokeCap.Round
        )

        // Central vertical shaft (Dragonslayer spine)
        drawLine(
            color = runeColor.copy(alpha = if (glowEffect) pulseAlpha else 1f),
            start = Offset(cx, h * 0.12f),
            end = Offset(cx, h * 0.88f),
            strokeWidth = stroke.width,
            cap = StrokeCap.Round
        )

        // Left upper fang
        val leftPath = Path().apply {
            moveTo(cx, h * 0.28f)
            lineTo(w * 0.26f, h * 0.40f)
            lineTo(cx, h * 0.52f)
            lineTo(w * 0.24f, h * 0.68f)
        }
        drawPath(
            path = leftPath,
            color = runeColor.copy(alpha = if (glowEffect) pulseAlpha else 1f),
            style = stroke
        )

        // Right upper fang
        val rightPath = Path().apply {
            moveTo(cx, h * 0.28f)
            lineTo(w * 0.74f, h * 0.40f)
            lineTo(cx, h * 0.52f)
            lineTo(w * 0.76f, h * 0.68f)
        }
        drawPath(
            path = rightPath,
            color = runeColor.copy(alpha = if (glowEffect) pulseAlpha else 1f),
            style = stroke
        )

        // Central blood knot circle
        drawCircle(
            color = runeColor.copy(alpha = if (glowEffect) pulseAlpha else 1f),
            center = Offset(cx, cy),
            radius = w * 0.14f,
            style = stroke
        )
    }
}

@Composable
fun CrimsonCircularGauge(
    percentage: Float,
    label: String,
    valueText: String,
    subText: String,
    modifier: Modifier = Modifier,
    primaryColor: Color = BloodCrimson,
    secondaryColor: Color = EclipseGold
) {
    val animatedPercent = remember { Animatable(0f) }

    LaunchedEffect(percentage) {
        animatedPercent.animateTo(
            targetValue = percentage.coerceIn(0f, 100f),
            animationSpec = tween(900, easing = FastOutSlowInEasing)
        )
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = size.width * 0.11f
            val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
            val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)

            // Background Track
            drawArc(
                color = DragonSlayerIron.copy(alpha = 0.5f),
                startAngle = 140f,
                sweepAngle = 260f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Foreground Glowing Arc
            val sweep = (animatedPercent.value / 100f) * 260f
            if (sweep > 0f) {
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            DeepGoreRed,
                            primaryColor,
                            secondaryColor
                        )
                    ),
                    startAngle = 140f,
                    sweepAngle = sweep,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label.uppercase(),
                style = CrimsonTypography.labelSmall.copy(
                    letterSpacing = 1.2.sp,
                    color = AshGrey
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = valueText,
                style = CrimsonTypography.titleLarge.copy(
                    fontFamily = JetBrainsMonoFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = BoneWhite
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subText,
                style = CrimsonTypography.labelMedium.copy(
                    color = primaryColor
                )
            )
        }
    }
}

@Composable
fun CrimsonProgressBar(
    percentage: Float,
    modifier: Modifier = Modifier,
    barHeight: Dp = 10.dp,
    barColor: Color = BloodCrimson
) {
    val animatedPercent = remember { Animatable(0f) }

    LaunchedEffect(percentage) {
        animatedPercent.animateTo(
            targetValue = percentage.coerceIn(0f, 100f),
            animationSpec = tween(700, easing = FastOutSlowInEasing)
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(barHeight)
            .clip(RoundedCornerShape(barHeight / 2f))
            .background(DragonSlayerIron.copy(alpha = 0.6f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = (animatedPercent.value / 100f).coerceIn(0.01f, 1f))
                .clip(RoundedCornerShape(barHeight / 2f))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            DeepGoreRed,
                            barColor,
                            EclipseGold
                        )
                    )
                )
        )
    }
}

@Composable
fun MetricRow(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    valueColor: Color = BoneWhite,
    tag: String? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f, fill = false)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = BloodCrimson,
                    modifier = Modifier
                        .size(18.dp)
                        .padding(end = 6.dp)
                )
            }
            Text(
                text = title,
                style = CrimsonTypography.bodyMedium.copy(
                    color = AshGrey
                )
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (tag != null) {
                Box(
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(DeepGoreRed.copy(alpha = 0.4f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = tag,
                        style = CrimsonTypography.labelSmall.copy(color = EclipseGold)
                    )
                }
            }
            Text(
                text = value,
                style = CrimsonTypography.bodyLarge.copy(
                    color = valueColor,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    badgeText: String? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(4.dp, 18.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(BloodCrimson)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title.uppercase(),
                    style = CrimsonTypography.titleMedium.copy(
                        fontFamily = CinzelFontFamily,
                        letterSpacing = 1.2.sp,
                        color = BoneWhite
                    )
                )
            }

            if (badgeText != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(BloodCrimson.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = badgeText,
                        style = CrimsonTypography.labelSmall.copy(
                            color = BloodCrimsonGlow,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        if (subtitle != null) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = CrimsonTypography.bodyMedium.copy(
                    color = DarkAsh
                ),
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}

@Composable
fun LiquidGlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    isPrimary: Boolean = true,
    testTag: String = "liquid_glass_button"
) {
    val bg = if (isPrimary) {
        Brush.horizontalGradient(
            colors = listOf(
                DeepGoreRed,
                BloodCrimson,
                BloodCrimsonGlow
            )
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(
                DragonSlayerIron,
                LiquidGlassBgHigh
            )
        )
    }

    val border = if (isPrimary) {
        BorderStroke(1.dp, BloodCrimsonGlow.copy(alpha = 0.6f))
    } else {
        BorderStroke(1.dp, BladeSteel.copy(alpha = 0.3f))
    }

    Surface(
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .testTag(testTag)
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        border = border,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(bg)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = BoneWhite,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text.uppercase(),
                    style = CrimsonTypography.labelLarge.copy(
                        fontFamily = CinzelFontFamily,
                        color = BoneWhite,
                        letterSpacing = 1.0.sp
                    )
                )
            }
        }
    }
}
