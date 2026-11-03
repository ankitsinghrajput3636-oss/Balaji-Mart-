package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = StoreGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = StoreGreenContainer,
    onPrimaryContainer = StoreGreenOnContainer,
    secondary = StoreYellowAccent,
    onSecondary = Color(0xFF332000),
    secondaryContainer = StoreYellowContainer,
    onSecondaryContainer = Color(0xFF4A3200),
    tertiary = StoreYellowDark,
    background = StoreBackground,
    onBackground = TextPrimary,
    surface = StoreSurface,
    onSurface = TextPrimary,
    surfaceVariant = StoreSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = StoreOutline
)

private val DarkColorScheme = darkColorScheme(
    primary = StoreGreenLight,
    onPrimary = Color.Black,
    primaryContainer = StoreGreenDark,
    onPrimaryContainer = StoreGreenContainer,
    secondary = StoreYellowAccent,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF4A3800),
    background = DarkBackground,
    onBackground = Color(0xFFE2EBE5),
    surface = DarkSurface,
    onSurface = Color(0xFFE2EBE5),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFB8C5BC),
    outline = Color(0xFF445449)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep distinct brand colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
