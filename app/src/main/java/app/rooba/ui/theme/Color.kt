package app.rooba.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val RoobaSeed = Color(0xFFF59E2B)

val md_theme_light_primary = Color(0xFF8A4B00)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFFFDCB8)
val md_theme_light_onPrimaryContainer = Color(0xFF2C1600)
val md_theme_light_inversePrimary = Color(0xFFFFB86B)

val md_theme_light_secondary = Color(0xFF5B5E66)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFE1E2EA)
val md_theme_light_onSecondaryContainer = Color(0xFF181B22)

val md_theme_light_tertiary = Color(0xFF2F6A5C)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFB3EFDD)
val md_theme_light_onTertiaryContainer = Color(0xFF00201A)

val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onErrorContainer = Color(0xFF410002)

val md_theme_light_background = Color(0xFFFBFAF7)
val md_theme_light_onBackground = Color(0xFF1B1B1D)
val md_theme_light_surface = Color(0xFFFBFAF7)
val md_theme_light_onSurface = Color(0xFF1B1B1D)
val md_theme_light_surfaceVariant = Color(0xFFE6E3DD)
val md_theme_light_onSurfaceVariant = Color(0xFF4A4740)
val md_theme_light_surfaceTint = md_theme_light_primary

val md_theme_light_surfaceContainerLowest = Color(0xFFFFFFFF)
val md_theme_light_surfaceContainerLow = Color(0xFFF5F3EF)
val md_theme_light_surfaceContainer = Color(0xFFEFEDE8)
val md_theme_light_surfaceContainerHigh = Color(0xFFE9E7E2)
val md_theme_light_surfaceContainerHighest = Color(0xFFE3E1DC)
val md_theme_light_surfaceBright = Color(0xFFFBFAF7)
val md_theme_light_surfaceDim = Color(0xFFDCDAD5)

val md_theme_light_outline = Color(0xFF7B776F)
val md_theme_light_outlineVariant = Color(0xFFCCC8C0)
val md_theme_light_inverseSurface = Color(0xFF303033)
val md_theme_light_inverseOnSurface = Color(0xFFF3F1EC)
val md_theme_light_scrim = Color(0xFF000000)

val md_theme_dark_primary = Color(0xFFFFB86B)
val md_theme_dark_onPrimary = Color(0xFF4A2700)
val md_theme_dark_primaryContainer = Color(0xFF6A3A00)
val md_theme_dark_onPrimaryContainer = Color(0xFFFFDCB8)
val md_theme_dark_inversePrimary = Color(0xFF8A4B00)

val md_theme_dark_secondary = Color(0xFFC4C6CF)
val md_theme_dark_onSecondary = Color(0xFF2D3038)
val md_theme_dark_secondaryContainer = Color(0xFF3A3D45)
val md_theme_dark_onSecondaryContainer = Color(0xFFE1E2EA)

val md_theme_dark_tertiary = Color(0xFF97D3C1)
val md_theme_dark_onTertiary = Color(0xFF00382E)
val md_theme_dark_tertiaryContainer = Color(0xFF135144)
val md_theme_dark_onTertiaryContainer = Color(0xFFB3EFDD)

val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)

val md_theme_dark_background = Color(0xFF111113)
val md_theme_dark_onBackground = Color(0xFFE6E4E0)
val md_theme_dark_surface = Color(0xFF111113)
val md_theme_dark_onSurface = Color(0xFFE6E4E0)
val md_theme_dark_surfaceVariant = Color(0xFF45444A)
val md_theme_dark_onSurfaceVariant = Color(0xFFC7C5C0)
val md_theme_dark_surfaceTint = md_theme_dark_primary

val md_theme_dark_surfaceContainerLowest = Color(0xFF0C0C0E)
val md_theme_dark_surfaceContainerLow = Color(0xFF19191C)
val md_theme_dark_surfaceContainer = Color(0xFF1D1D20)
val md_theme_dark_surfaceContainerHigh = Color(0xFF27272B)
val md_theme_dark_surfaceContainerHighest = Color(0xFF323236)
val md_theme_dark_surfaceBright = Color(0xFF37373B)
val md_theme_dark_surfaceDim = Color(0xFF111113)

val md_theme_dark_outline = Color(0xFF918F8A)
val md_theme_dark_outlineVariant = Color(0xFF45444A)
val md_theme_dark_inverseSurface = Color(0xFFE6E4E0)
val md_theme_dark_inverseOnSurface = Color(0xFF303033)
val md_theme_dark_scrim = Color(0xFF000000)

data class RoobaStatusColors(
    val connected: Color,
    val connecting: Color,
    val disconnected: Color,
)

val LightStatusColors = RoobaStatusColors(
    connected = Color(0xFF1B6E2F),
    connecting = Color(0xFF8A5A00),
    disconnected = md_theme_light_onSurfaceVariant,
)

val DarkStatusColors = RoobaStatusColors(
    connected = Color(0xFF7BDF92),
    connecting = Color(0xFFFFC46B),
    disconnected = md_theme_dark_onSurfaceVariant,
)

val LocalRoobaStatusColors = staticCompositionLocalOf { LightStatusColors }

val ConnectedGreen = LightStatusColors.connected
val ConnectingAmber = LightStatusColors.connecting
