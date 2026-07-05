package com.example.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.CircleUpViewModel
import kotlinx.coroutines.delay

val RedGradient = Brush.linearGradient(
    colors = listOf(Color(0xFFFF4D4D), Color(0xFFFF1744), SosDark)
)

// ==========================================
// ACTIVE SOS OVERLAY
// ==========================================

@Composable
fun SOSActiveOverlay(
    onCancel: () -> Unit,
    onEnd: () -> Unit
) {
    var stage by remember { mutableStateOf("countdown") } // 'countdown', 'active'
    var countdown by remember { mutableStateOf(5) }
    var elapsedSeconds by remember { mutableStateOf(0) }
    var alertsSent by remember { mutableStateOf(0) }

    val context = LocalContext.current

    // Countdown Timer
    LaunchedEffect(countdown, stage) {
        if (stage == "countdown") {
            if (countdown > 0) {
                delay(1000)
                countdown -= 1
            } else {
                stage = "active"
            }
        }
    }

    // Elapsed Active Timer
    LaunchedEffect(stage) {
        if (stage == "active") {
            while (true) {
                delay(1000)
                elapsedSeconds += 1
            }
        }
    }

    // Alerts dispatch delay simulation
    LaunchedEffect(stage) {
        if (stage == "active") {
            for (i in 1..5) {
                delay(800)
                alertsSent = i
            }
        }
    }

    if (stage == "countdown") {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colors = listOf(Color(0xFF991B1B), Color(0xFFDC2626)))),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "CIRCLE UP GUARD", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(text = "ACTIVATING SOS", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                            .border(4.dp, Color.White.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = Color.White, modifier = Modifier.size(56.dp))
                    }
                    Spacer(modifier = Modifier.height(28.dp))
                    Text(text = "AUTO-DIAL 100 IN", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp)
                    Text(text = countdown.toString(), color = Color.White, fontSize = 110.sp, fontWeight = FontWeight.Black)
                    Text(
                        text = "Police, Emergency and 5 nearby neighbours will be alerted with your live GPS location.",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = onCancel,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                    ) {
                        Text(text = "Cancel — Accidental", color = Color(0xFF991B1B), fontSize = 16.sp, fontWeight = FontWeight.Black)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Cancel within ${countdown}s to prevent alerts dispatch.",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    } else {
        // Active SOS Dispatched State
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colors = listOf(Color(0xFFDC2626), Color(0xFF7F1D1D)))),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "SOS ACTIVE", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                    }
                    Text(
                        text = String.format("%02d:%02d", elapsedSeconds / 60, elapsedSeconds % 60),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                        .border(2.dp, Color.White.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Help is on the way", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black)
                Text(text = "Your circle is with you. Stay calm.", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)

                Spacer(modifier = Modifier.height(20.dp))

                // Location Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "YOUR LIVE LOCATION", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Text(text = "LIVE", color = Color.Green, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "12.9120° N, 77.6446° E", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text(text = "HSR Layout, Bangalore • ~12m accuracy", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Recipients timeline
                Text(
                    text = "ALERTS DISPATCHED",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(8.dp))

                val alerts = listOf(
                    "Karnataka State Police (100 dialed)",
                    "Unified Emergency (112 coordinates sent)",
                    "Women Helpline (1091 alerted)",
                    "3 Trusted Contacts pinged",
                    "5 Nearby Neighbours notified"
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    alerts.forEachIndexed { index, title ->
                        val sent = index < alertsSent
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = if (sent) 0.18f else 0.06f))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(if (sent) Color.Green else Color.LightGray.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (sent) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(12.dp))
                                } else {
                                    CircularProgressIndicator(modifier = Modifier.size(12.dp), strokeWidth = 1.5.dp, color = Color.White)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }

                // CTA
                Button(
                    onClick = {
                        // Mock Dial Police 100
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:100"))
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Icon(Icons.Default.Phone, contentDescription = null, tint = Color(0xFF991B1B))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "CALL 100 — POLICE", color = Color(0xFF991B1B), fontSize = 15.sp, fontWeight = FontWeight.Black)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "End SOS (only if you're safe)",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onEnd() }
                        .padding(bottom = 12.dp)
                )
            }
        }
    }
}

// ==========================================
// GUARD SCREEN MAIN
// ==========================================

@Composable
fun GuardScreen(
    viewModel: CircleUpViewModel,
    onBack: () -> Unit,
    onFakeCallClick: () -> Unit,
    onSilentPhraseClick: () -> Unit,
    onShareLocationClick: () -> Unit,
    onTrustedContactsClick: () -> Unit
) {
    var sosActive by remember { mutableStateOf(false) }
    val alertsList by viewModel.alerts.collectAsState()

    if (sosActive) {
        SOSActiveOverlay(
            onCancel = { sosActive = false },
            onEnd = { sosActive = false }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Toolbar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(text = "Circle Guard", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Card(
                    onClick = { /* Call 100 directly */ },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE2E2))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = null, tint = SosDark, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "100", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SosDark)
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                item {
                    // Big SOS Trigger Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(RedGradient)
                            .clickable { sosActive = true }
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Security, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(text = "Tap for SOS", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "Alerts police (100) + trusted contacts + 5 nearest neighbours with live location",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Grid Options
                    Text(text = "SAFETY UTILITIES", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(10.dp))

                    val options = listOf(
                        Triple("Fake Check-in", "Send fake call/text to exit safely", onFakeCallClick),
                        Triple("Silent Phrase", "Say 'order kar do' to trigger SOS", onSilentPhraseClick),
                        Triple("Share Live Location", "Share live map coordinate for 30m", onShareLocationClick),
                        Triple("Trusted Contacts", "Configure 3 emergency contacts", onTrustedContactsClick)
                    )

                    // Simplified Rows for grid
                    val chunks = options.chunked(2)
                    chunks.forEach { rowList ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowList.forEach { option ->
                                Card(
                                    onClick = option.third,
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = BrandCream),
                                    border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(text = option.first, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BrandInk)
                                        Text(text = option.second, fontSize = 10.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(text = "RECENT ALERTS IN HSR LAYOUT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                }

                items(alertsList) { alert ->
                    val color = if (alert.severity == "high") SosDark else BrandPrimary
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = BrandCream),
                        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .border(
                                    BorderStroke(width = 3.dp, color = color),
                                    shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                                )
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Warning, contentDescription = null, tint = color, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = alert.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BrandInk)
                                }
                                Text(text = alert.time, fontSize = 11.sp, color = Color.Gray)
                            }
                            Text(text = alert.text, fontSize = 13.sp, color = BrandInk, modifier = Modifier.padding(top = 8.dp))
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// FAKE INCOMING CALL SIMULATION
// ==========================================

@Composable
fun FakeCallScreen(onBack: () -> Unit) {
    var secondsElapsed by remember { mutableStateOf(0) }
    var active by remember { mutableStateOf(false) }

    LaunchedEffect(active) {
        if (active) {
            while (true) {
                delay(1000)
                secondsElapsed += 1
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F1A)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 40.dp)
            ) {
                Text(text = "Incoming Call", color = Color.White.copy(alpha = 0.6f), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text(text = "Mom", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Light, modifier = Modifier.padding(top = 4.dp))
                if (active) {
                    Text(
                        text = String.format("%02d:%02d", secondsElapsed / 60, secondsElapsed % 60),
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.05f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "👩", fontSize = 80.sp)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color.Red)
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = "Decline", tint = Color.White)
                    }

                    if (!active) {
                        IconButton(
                            onClick = { active = true },
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color.Green)
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "Accept", tint = Color.White)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Fake check-in call simulation", color = Color.White.copy(alpha = 0.4f), fontSize = 10.sp)
            }
        }
    }
}

// ==========================================
// SILENT PHRASE SCREEN
// ==========================================

@Composable
fun SilentPhraseScreen(onBack: () -> Unit) {
    var phrase by remember { mutableStateOf("order kar do") }
    var active by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Silent Phrase", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BrandCream),
                border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Default.VolumeUp, contentDescription = null, tint = BrandPrimary)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "How does it work?", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BrandInk)
                        Text(
                            text = "Your phone listens in the background. Saying the phrase triggers SOS silently with no sound or notifications. Voice data is processed encrypted on-device. 🔒",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            lineHeight = 16.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BrandCream),
                border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Silent Phrase Trigger", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandInk)
                        Text(text = if (active) "Active in background" else "Disabled", fontSize = 11.sp, color = Color.Gray)
                    }
                    Switch(checked = active, onCheckedChange = { active = it }, colors = SwitchDefaults.colors(checkedThumbColor = BrandPrimary))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "YOUR SECRET PHRASE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray, letterSpacing = 1.sp)
            OutlinedTextField(
                value = phrase,
                onValueChange = { phrase = it },
                textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandPrimary,
                    unfocusedBorderColor = Color(0xFFE5E7EB)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            GradientButton(text = "Save Phrase", onClick = onBack)
        }
    }
}

// ==========================================
// SHARE LIVE LOCATION SCREEN
// ==========================================

@Composable
fun ShareLocationScreen(onBack: () -> Unit) {
    var active by remember { mutableStateOf(false) }
    var durationMinutes by remember { mutableStateOf(30f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Share Live Location", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Mock Location Circle Map
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(BrandGradientSoft),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(BrandPrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = BrandPrimary, modifier = Modifier.size(32.dp))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "HSR Layout, Tower B", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandInk)
                    Text(text = "12.9120° N, 77.6446° E", fontSize = 11.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (!active) {
                Text(text = "DURATION: ${durationMinutes.toInt()} MINUTES", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray, letterSpacing = 1.sp)
                Slider(
                    value = durationMinutes,
                    onValueChange = { durationMinutes = it },
                    valueRange = 15f..120f,
                    steps = 7,
                    colors = SliderDefaults.colors(thumbColor = BrandPrimary, activeTrackColor = BrandPrimary)
                )
            } else {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandSage.copy(alpha = 0.1f)),
                    border = BorderStroke(1.dp, BrandSage.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(BrandSage)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "Sharing coordinates dynamically with 3 trusted contacts.", fontSize = 12.sp, color = BrandSage, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            GradientButton(
                text = if (active) "Stop Sharing" else "Start Sharing Location",
                onClick = { active = !active }
            )
        }
    }
}

// ==========================================
// TRUSTED EMERGENCY CONTACTS SCREEN
// ==========================================

@Composable
fun TrustedContactsScreen(onBack: () -> Unit) {
    val contacts = listOf(
        Pair("Mom (Priya)", "+91 98765 43200"),
        Pair("Sneha Iyer", "+91 98765 43201"),
        Pair("Rohan Bhaiya", "+91 98765 43202")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Trusted Contacts", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = BorderStroke(1.dp, BrandPrimary.copy(alpha = 0.2f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(brush = BrandGradientSoft, shape = RoundedCornerShape(16.dp))
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = BrandPrimary)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "When SOS is active, contacts will instantly receive an automated SMS coordinate stamp. Maximum of 5 contacts allowed.",
                        fontSize = 11.sp,
                        color = Color.DarkGray,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "YOUR TRUSTED CONTACTS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(10.dp))

            contacts.forEach { contact ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandCream),
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = contact.first, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandInk)
                            Text(text = contact.second, fontSize = 11.sp, color = Color.Gray)
                        }
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Phone, contentDescription = "Call", tint = BrandPrimary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            OutlinedButton(
                onClick = {},
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, BrandPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = BrandPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Add Emergency Contact", color = BrandPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
