package app.rooba.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.rooba.R
import app.rooba.RoobaApp
import app.rooba.data.model.ConnectionState
import app.rooba.ui.components.TELEGRAM_CHANNEL
import app.rooba.ui.components.flagEmoji
import app.rooba.ui.components.openTelegramChannel
import app.rooba.ui.components.pressScale
import app.rooba.ui.theme.LocalRoobaStatusColors
import app.rooba.ui.theme.ThemeController
import app.rooba.ui.theme.ThemeMode
import app.rooba.vpn.RoobaVpnService

private val TelegramBlue = Color(0xFF2AABEE)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    app: RoobaApp,
    themeController: ThemeController,
    onRequestConnect: () -> Unit,
    onDisconnect: () -> Unit,
    onOpenServers: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    val state by RoobaVpnService.state.collectAsState()
    val lastError by RoobaVpnService.lastError.collectAsState()
    val selectedProxy by app.proxyStateStore.selectedProxyFlow.collectAsState()
    val context = LocalContext.current
    val haptics = LocalHapticFeedback.current
    val systemInDarkTheme = isSystemInDarkTheme()
    val colors = MaterialTheme.colorScheme
    val statusColors = LocalRoobaStatusColors.current

    val accent by animateColorAsState(
        targetValue = when (state) {
            ConnectionState.CONNECTED -> statusColors.connected
            ConnectionState.CONNECTING -> colors.primary
            ConnectionState.DISCONNECTED -> colors.outline
        },
        animationSpec = tween(320),
        label = "accent",
    )
    val glowAlpha by animateFloatAsState(
        targetValue = if (state == ConnectionState.DISCONNECTED) 0.06f else 0.20f,
        animationSpec = tween(400),
        label = "glow",
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .drawBehind {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(accent.copy(alpha = glowAlpha), Color.Transparent),
                        center = Offset(size.width / 2f, size.height * 0.40f),
                        radius = size.width * 0.85f,
                    ),
                    radius = size.width * 0.85f,
                    center = Offset(size.width / 2f, size.height * 0.40f),
                )
            }
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp),
    ) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(R.drawable.rooba_mark),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
            )
            Spacer(Modifier.width(10.dp))
            Text("Rooba", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))

            val mode = themeController.mode
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .combinedClickable(
                        role = Role.Button,
                        onClick = { themeController.toggle(systemInDarkTheme) },
                        onLongClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            themeController.set(ThemeMode.SYSTEM)
                        },
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = when (mode) {
                        ThemeMode.SYSTEM -> Icons.Filled.BrightnessAuto
                        ThemeMode.LIGHT -> Icons.Filled.LightMode
                        ThemeMode.DARK -> Icons.Filled.DarkMode
                    },
                    contentDescription = "Theme",
                    tint = colors.onSurfaceVariant,
                )
            }
            IconButton(onClick = onOpenSettings) {
                Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = colors.onSurfaceVariant)
            }
        }

        Spacer(Modifier.weight(1f))

        // Connect orb
        ConnectOrb(
            state = state,
            accent = accent,
            onClick = {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                if (state == ConnectionState.DISCONNECTED) onRequestConnect() else onDisconnect()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Spacer(Modifier.height(28.dp))

        AnimatedContent(
            targetState = state,
            transitionSpec = { fadeIn(tween(180)) togetherWith fadeOut(tween(120)) },
            label = "status",
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) { s ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = when (s) {
                        ConnectionState.CONNECTED -> "Connected"
                        ConnectionState.CONNECTING -> "Connecting…"
                        ConnectionState.DISCONNECTED -> "Not connected"
                    },
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = when (s) {
                        ConnectionState.CONNECTED -> "Your traffic is protected"
                        ConnectionState.CONNECTING -> "Tap to cancel"
                        ConnectionState.DISCONNECTED -> "Tap the button to connect"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant,
                )
            }
        }

        val error = lastError
        if (error != null && state != ConnectionState.CONNECTING) {
            Spacer(Modifier.height(10.dp))
            Text(
                error,
                style = MaterialTheme.typography.bodySmall,
                color = colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(Modifier.weight(1f))

        // Location
        val location = selectedProxy
        HomeCard(
            onClick = onOpenServers,
            leading = {
                Box(
                    Modifier.size(44.dp).clip(CircleShape).background(colors.surfaceContainerHighest),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(flagEmoji(location?.countryCode ?: ""), fontSize = 22.sp)
                }
            },
            title = location?.let { it.countryName.ifBlank { it.countryCode } } ?: "Recommended",
            subtitle = "Location · tap to change",
        )

        Spacer(Modifier.height(10.dp))

        // Telegram channel
        HomeCard(
            onClick = { openTelegramChannel(context) },
            leading = {
                Box(
                    Modifier.size(44.dp).clip(CircleShape).background(TelegramBlue),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp).padding(start = 2.dp),
                    )
                }
            },
            title = "Join our Telegram",
            subtitle = "@$TELEGRAM_CHANNEL · news and updates",
        )

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun ConnectOrb(
    state: ConnectionState,
    accent: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    val interaction = remember { MutableInteractionSource() }
    val connected = state == ConnectionState.CONNECTED

    val innerColor by animateColorAsState(
        targetValue = if (connected) accent else colors.surfaceContainerHigh,
        animationSpec = tween(320),
        label = "inner",
    )
    val iconColor by animateColorAsState(
        targetValue = when (state) {
            ConnectionState.CONNECTED -> if (colors.background.luminanceIsDark()) Color(0xFF14200A) else Color.White
            ConnectionState.CONNECTING -> colors.primary
            ConnectionState.DISCONNECTED -> colors.onSurface
        },
        animationSpec = tween(320),
        label = "icon",
    )

    Box(
        modifier = modifier.size(236.dp),
        contentAlignment = Alignment.Center,
    ) {
        // Outer track and the spinning arc while connecting
        Canvas(Modifier.size(224.dp)) {
            drawCircle(color = accent.copy(alpha = 0.22f), style = Stroke(3.dp.toPx()))
        }
        if (state == ConnectionState.CONNECTING) SpinningArc(accent)

        Box(
            modifier = Modifier
                .size(176.dp)
                .pressScale(interaction)
                .clip(CircleShape)
                .background(accent.copy(alpha = if (connected) 0.18f else 0.08f))
                .border(1.dp, accent.copy(alpha = 0.30f), CircleShape)
                .clickable(
                    interactionSource = interaction,
                    indication = null,
                    role = Role.Button,
                    onClick = onClick,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(124.dp)
                    .then(if (connected) Modifier.breathing() else Modifier)
                    .clip(CircleShape)
                    .background(innerColor),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.PowerSettingsNew,
                    contentDescription = if (state == ConnectionState.DISCONNECTED) "Connect" else "Disconnect",
                    tint = iconColor,
                    modifier = Modifier.size(52.dp),
                )
            }
        }
    }
}

@Composable
private fun SpinningArc(color: Color) {
    val t = rememberInfiniteTransition(label = "spin")
    val angle by t.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1100, easing = LinearEasing), RepeatMode.Restart),
        label = "angle",
    )
    Canvas(Modifier.size(224.dp)) {
        drawArc(
            color = color,
            startAngle = angle,
            sweepAngle = 80f,
            useCenter = false,
            style = Stroke(3.dp.toPx(), cap = StrokeCap.Round),
        )
    }
}

@Composable
private fun Modifier.breathing(): Modifier {
    val t = rememberInfiniteTransition(label = "breath")
    val s by t.animateFloat(
        initialValue = 0.97f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "breathScale",
    )
    return this.graphicsLayer { scaleX = s; scaleY = s }
}

private fun Color.luminanceIsDark(): Boolean = (0.2126f * red + 0.7152f * green + 0.0722f * blue) < 0.5f

@Composable
private fun HomeCard(
    onClick: () -> Unit,
    leading: @Composable () -> Unit,
    title: String,
    subtitle: String,
) {
    val interaction = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .pressScale(interaction)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        leading()
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
