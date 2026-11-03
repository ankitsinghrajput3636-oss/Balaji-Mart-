package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.config.StoreConfig
import com.example.ui.theme.StoreGreenDark
import com.example.ui.theme.StoreGreenPrimary
import com.example.ui.theme.StoreYellowAccent

enum class LogoSize {
    SMALL,   // For Top Bar / Cards
    MEDIUM,  // For Dialogs & Login
    LARGE    // For Splash & Hero Banners
}

/**
 * =========================================================================
 * BALAJI MART MODULAR STORE LOGO & BRANDING COMPONENT
 * =========================================================================
 * Provides clean, modern brand styling inspired by Blinkit.
 * When the store owner provides an official logo image file (e.g. store_logo.png),
 * it can be dropped into res/drawable and referenced here without changing app layouts.
 */
@Composable
fun StoreLogo(
    modifier: Modifier = Modifier,
    size: LogoSize = LogoSize.MEDIUM,
    showTagline: Boolean = true,
    isDarkTheme: Boolean = false
) {
    val iconSize: Dp = when (size) {
        LogoSize.SMALL -> 32.dp
        LogoSize.MEDIUM -> 52.dp
        LogoSize.LARGE -> 84.dp
    }

    val titleFontSize = when (size) {
        LogoSize.SMALL -> 15.sp
        LogoSize.MEDIUM -> 20.sp
        LogoSize.LARGE -> 30.sp
    }

    val taglineFontSize = when (size) {
        LogoSize.SMALL -> 9.sp
        LogoSize.MEDIUM -> 11.sp
        LogoSize.LARGE -> 13.sp
    }

    val primaryTextColor = if (isDarkTheme) Color.White else StoreGreenDark
    val secondaryTextColor = if (isDarkTheme) StoreYellowAccent else StoreGreenPrimary

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo Emblem Badge
        Box(
            modifier = Modifier
                .size(iconSize)
                .clip(RoundedCornerShape(iconSize / 4))
                .background(
                    Brush.linearGradient(
                        colors = listOf(StoreGreenPrimary, StoreGreenDark)
                    )
                )
                .border(2.dp, StoreYellowAccent, RoundedCornerShape(iconSize / 4)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = StoreConfig.Identity.STORE_NAME_EN,
                tint = StoreYellowAccent,
                modifier = Modifier.size(iconSize * 0.55f)
            )
        }

        Spacer(modifier = Modifier.width(iconSize / 4))

        // Brand Name & Tagline
        Column(verticalArrangement = Arrangement.Center) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = StoreConfig.Identity.STORE_NAME_EN,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = titleFontSize,
                    color = primaryTextColor,
                    letterSpacing = 0.5.sp
                )
            }

            if (showTagline) {
                Text(
                    text = StoreConfig.Identity.TAGLINE_EN,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = taglineFontSize,
                    color = secondaryTextColor
                )
            }
        }
    }
}
