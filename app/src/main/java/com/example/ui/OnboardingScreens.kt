package com.example.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material.icons.filled.Security
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandInk
import com.example.ui.theme.BrandCream
import com.example.ui.theme.BrandDeep
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandSage
import com.example.ui.theme.HeartRed
import com.example.viewmodel.CircleUpViewModel
import kotlinx.coroutines.delay
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

// ==========================================
// GRADIENTS & BUTTONS
// ==========================================

val BrandGradient = Brush.linearGradient(
    colors = listOf(BrandAmber, BrandPrimary, BrandDeep)
)

val BrandGradientSoft = Brush.linearGradient(
    colors = listOf(
        BrandAmber.copy(alpha = 0.12f),
        BrandPrimary.copy(alpha = 0.12f),
        BrandDeep.copy(alpha = 0.12f)
    )
)

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .border(
                width = if (enabled) 0.dp else 1.dp,
                color = if (enabled) Color.Transparent else Color.LightGray,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = if (enabled) BrandGradient else Brush.linearGradient(
                        colors = listOf(Color.LightGray, Color.LightGray)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.2).sp
            )
        }
    }
}

// ==========================================
// LOGO COMPONENT
// ==========================================

@Composable
fun CircleUpLogo(size: Int = 80, modifier: Modifier = Modifier) {
    val sizeDp = size.dp
    Box(
        modifier = modifier
            .size(sizeDp)
            .clip(RoundedCornerShape((size * 0.28f).dp))
            .background(BrandGradient),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.dp.toPx() / 2f
            val cy = size.dp.toPx() / 2f
            val r = size.dp.toPx() * 0.26f

            // Nodes
            val angles = listOf(-90f, -18f, 54f, 126f, 198f)
            val outerPoints = angles.map { angle ->
                val rad = Math.toRadians(angle.toDouble())
                Offset(
                    (cx + r * Math.cos(rad)).toFloat(),
                    (cy + r * Math.sin(rad)).toFloat()
                )
            }

            // Draw connecting lines
            outerPoints.forEach { pt ->
                drawLine(
                    color = Color.White,
                    start = Offset(cx, cy),
                    end = pt,
                    strokeWidth = 4f
                )
            }

            // Draw outer node circles
            outerPoints.forEach { pt ->
                drawCircle(color = Color.White, radius = 9f, center = pt)
            }

            // Draw center node
            drawCircle(color = Color.White, radius = 13f, center = Offset(cx, cy))
        }
    }
}

// ==========================================
// SPLASH SCREEN
// ==========================================

@Composable
fun SplashScreen(onComplete: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2200)
        onComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircleUpLogo(size = 110)
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Circle Up",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = BrandPrimary,
                letterSpacing = (-0.5).sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Aage badh, apni circle ke saath",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "from ", fontSize = 12.sp, color = Color.Gray)
                Text(
                    text = "India",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandPrimary
                )
            }
        }
    }
}

// ==========================================
// LOGIN SCREEN
// ==========================================

@Composable
fun PhoneScreen(
    onContinue: (String) -> Unit,
    onSignup: () -> Unit
) {
    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    val canContinue = identifier.trim().length >= 3 && password.length >= 6

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        CircleUpLogo(size = 68)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Circle Up",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = BrandPrimary,
            letterSpacing = (-0.5).sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        OutlinedTextField(
            value = identifier,
            onValueChange = { identifier = it },
            placeholder = { Text("Phone number, username or email", fontSize = 14.sp) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandPrimary,
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedContainerColor = BrandCream,
                unfocusedContainerColor = BrandCream
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Password", fontSize = 14.sp) },
            singleLine = true,
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandPrimary,
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedContainerColor = BrandCream,
                unfocusedContainerColor = BrandCream
            ),
            trailingIcon = {
                Text(
                    text = if (showPassword) "Hide" else "Show",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandPrimary,
                    modifier = Modifier
                        .clickable { showPassword = !showPassword }
                        .padding(end = 12.dp)
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        GradientButton(
            text = "Log in",
            onClick = { onContinue(identifier) },
            enabled = canContinue
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Forgot password?",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = BrandPrimary,
            modifier = Modifier.clickable { }
        )

        Spacer(modifier = Modifier.height(28.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Divider(modifier = Modifier.weight(1f), color = Color(0xFFE5E7EB))
            Text(
                text = "OR",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Divider(modifier = Modifier.weight(1f), color = Color(0xFFE5E7EB))
        }

        Spacer(modifier = Modifier.height(28.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onContinue("google_account@gmail.com") }
        ) {
            Box(modifier = Modifier.size(20.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(color = BrandPrimary, radius = size.minDimension / 2f)
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Continue with Google",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = BrandInk
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = onSignup,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.5.dp, BrandPrimary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Create new account",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = BrandPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "By continuing, you agree to Circle Up's Terms & Privacy",
            fontSize = 10.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

// ==========================================
// SIGNUP SELECTION SCREEN
// ==========================================

@Composable
fun SignupScreen(
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        IconButton(onClick = onBack, modifier = Modifier.size(36.dp)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }

        Spacer(modifier = Modifier.height(16.dp))
        CircleUpLogo(size = 52)
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Create your account",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = BrandInk,
            letterSpacing = (-0.5).sp
        )
        Text(
            text = "Choose how you want to sign up. You can always link other methods later.",
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 6.dp)
        )

        Spacer(modifier = Modifier.height(36.dp))

        // Option 1: Gmail
        Card(
            onClick = onContinue,
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(2.dp, BrandInk),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(BrandCream),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Google",
                        tint = BrandPrimary
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(text = "Continue with Gmail", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandInk)
                    Text(text = "Fastest — use your Google account", fontSize = 11.sp, color = Color.Gray)
                }
            }
        }

        // Option 2: Email
        Card(
            onClick = onContinue,
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BrandCream),
            border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Lock, contentDescription = "Email", tint = BrandInk)
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(text = "Sign up with Email", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandInk)
                    Text(text = "Use any email + a password", fontSize = 11.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Already on Circle Up? ", fontSize = 13.sp, color = Color.Gray)
            Text(
                text = "Log in",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = BrandPrimary,
                modifier = Modifier.clickable { onBack() }
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

// ==========================================
// OTP VERIFICATION SCREEN
// ==========================================

@Composable
fun OtpScreen(
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    var otpDigits = remember { mutableStateListOf("", "", "", "") }
    var verifying by remember { mutableStateOf(false) }

    LaunchedEffect(otpDigits.joinToString("")) {
        if (otpDigits.joinToString("").length == 4) {
            verifying = true
            delay(1500)
            verifying = false
            onContinue()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        IconButton(onClick = onBack, modifier = Modifier.size(36.dp)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Verify OTP",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = BrandInk,
            letterSpacing = (-0.5).sp
        )
        Text(
            text = "Code sent to +91 98765 43210",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            for (i in 0..3) {
                OutlinedTextField(
                    value = otpDigits.getOrElse(i) { "" },
                    onValueChange = { value ->
                        if (value.length <= 1 && value.all { it.isDigit() }) {
                            otpDigits[i] = value
                        }
                    },
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandPrimary,
                        unfocusedBorderColor = Color(0xFFE5E7EB),
                        focusedContainerColor = BrandCream,
                        unfocusedContainerColor = BrandCream
                    ),
                    modifier = Modifier
                        .size(64.dp)
                        .padding(horizontal = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Didn't get OTP? ", fontSize = 13.sp, color = Color.Gray)
            Text(text = "Resend in 0:24", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BrandPrimary)
        }

        if (verifying) {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = BrandPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Verifying...", fontSize = 13.sp, color = BrandSage, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ==========================================
// ADDRESS & GPS SELFIE VERIFICATION
// ==========================================

@Composable
fun AddressScreen(
    viewModel: CircleUpViewModel,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    var address by remember { mutableStateOf("") }
    var locationOn by remember { mutableStateOf(false) }
    var selfieBase64 by remember { mutableStateOf<String?>(null) }
    var aiStatus by remember { mutableStateOf("") } // 'none', 'verifying', 'verified'

    val doneAddress = address.isNotEmpty()
    val doneSelfie = aiStatus == "verified"
    val allDone = doneAddress && doneSelfie && locationOn

    val progressValue by animateFloatAsState(
        targetValue = (if (doneAddress) 0.5f else 0f) + (if (doneSelfie) 0.5f else 0f),
        label = "Progress"
    )

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
            Text(text = "Verification", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Card(
                onClick = { locationOn = !locationOn },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (locationOn) BrandSage.copy(alpha = 0.15f) else BrandCream
                ),
                border = BorderStroke(
                    1.dp,
                    if (locationOn) BrandSage.copy(alpha = 0.4f) else Color.LightGray
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = if (locationOn) BrandSage else Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (locationOn) "Location ON" else "Enable GPS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (locationOn) BrandSage else Color.Gray
                    )
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
                Text(
                    text = "Where's your circle?",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandInk,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "AI will auto-verify your identity 🤖",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )

                // Safety Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandCream),
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Row(modifier = Modifier.padding(14.dp)) {
                        Icon(
                            Icons.Default.Security,
                            contentDescription = "Shield",
                            tint = Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Safety first 🔒 — Only real verified neighbours can enter. Outsiders and fake profiles are strictly blocked.",
                            fontSize = 11.sp,
                            color = Color.DarkGray,
                            lineHeight = 16.sp
                        )
                    }
                }

                // Progress Indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Progress", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(10.dp))
                    LinearProgressIndicator(
                        progress = progressValue,
                        color = BrandPrimary,
                        trackColor = Color(0xFFF3F4F6),
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "${if (doneAddress) 1 else 0 + if (doneSelfie) 1 else 0}/2",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandPrimary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // STEP 1: Address Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (doneAddress) BrandSage.copy(alpha = 0.04f) else BrandCream
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (doneAddress) BrandSage.copy(alpha = 0.3f) else Color(0xFFE5E7EB)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(if (doneAddress) BrandSage else BrandPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                if (doneAddress) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = "Done",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                } else {
                                    Text(text = "1", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Black)
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = "Add your address", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandInk)
                                Text(text = "Exact name of your society or building", fontSize = 11.sp, color = Color.Gray)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            placeholder = { Text("Search building or society", fontSize = 13.sp) },
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Default.Map, contentDescription = "Map", tint = Color.Gray) },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandPrimary,
                                unfocusedBorderColor = Color(0xFFE5E7EB),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (address.isNotEmpty() && address.length < 15) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Card(
                                onClick = { address = "Brigade Meadows, HSR Layout, Bangalore" },
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.LocationOn, contentDescription = "Location", tint = Color.Gray, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(text = "Brigade Meadows", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                        Text(text = "HSR Layout, Bangalore", fontSize = 10.sp, color = Color.Gray)
                                    }
                                }
                            }
                        }
                    }
                }

                // STEP 2: Selfie Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (doneSelfie) BrandSage.copy(alpha = 0.04f) else BrandCream
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (doneSelfie) BrandSage.copy(alpha = 0.3f) else Color(0xFFE5E7EB)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(if (doneSelfie) BrandSage else BrandPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                if (doneSelfie) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = "Done",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                } else {
                                    Text(text = "2", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Black)
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = "Quick selfie verification", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandInk)
                                Text(text = "AI confirms your identity in 3 seconds", fontSize = 11.sp, color = Color.Gray)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (selfieBase64 != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                            ) {
                                // Render captured selfie
                                val decodedString = Base64.decode(selfieBase64, Base64.DEFAULT)
                                val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                                if (bitmap != null) {
                                    androidx.compose.foundation.Image(
                                        bitmap = bitmap.asImageBitmap(),
                                        contentDescription = "Selfie",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }

                                if (aiStatus == "verifying") {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Black.copy(alpha = 0.5f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(32.dp))
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(text = "AI verifying...", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                } else if (aiStatus == "verified") {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(10.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(BrandGradient)
                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(10.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(text = "AI Verified", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Black)
                                        }
                                    }
                                }
                            }

                            if (aiStatus == "verified") {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Match confidence: 98% — You're a verified neighbour! 🎉",
                                    fontSize = 11.sp,
                                    color = BrandSage,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            // Empty Trigger Box
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(BorderStroke(1.dp, Color.Gray), RoundedCornerShape(12.dp))
                                    .clickable {
                                        // Mock Take Selfie
                                        aiStatus = "verifying"
                                        selfieBase64 = "dummy_data" // dummy selfie representation
                                    }
                                    .padding(horizontal = 16.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CameraAlt, contentDescription = "Camera", tint = BrandPrimary)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(text = "Take a selfie", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BrandInk)
                                        Text(text = "Front camera + AI verification 🤳", fontSize = 10.sp, color = Color.Gray)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Mock verification timer
        LaunchedEffect(aiStatus) {
            if (aiStatus == "verifying") {
                delay(2000)
                aiStatus = "verified"
            }
        }

        // Bottom CTA
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Column {
                GradientButton(
                    text = if (allDone) "Continue — Welcome!" else "Complete all steps",
                    onClick = onContinue,
                    enabled = allDone
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Entry isn't granted until all steps are complete.", fontSize = 10.sp, color = Color.Gray)
                }
            }
        }
    }
}

// ==========================================
// PROFILE SETUP SCREEN
// ==========================================

@Composable
fun ProfileSetupScreen(
    viewModel: CircleUpViewModel,
    onComplete: () -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    val vibes = remember { mutableStateListOf<String>() }

    val minimumVibesSelected = vibes.size >= 3

    val sampleVibes = listOf(
        "Chai Lover", "Plant Parent", "Bookworm", "Gamer", "Fitness Freak",
        "Coffee Snob", "Late Night Owl", "Early Bird", "Home Chef", "Yogi"
    )

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
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Set up your profile", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            item {
                Text(
                    text = "Let your circle know who you are.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Dummy Avatar Selector
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(BrandGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (name.isNotEmpty()) name.take(1).uppercase() else "C",
                            color = Color.White,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Text(
                    text = "YOUR NAME",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    letterSpacing = 1.sp
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("e.g. Aanya Sharma", fontSize = 15.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandPrimary,
                        unfocusedBorderColor = Color(0xFFE5E7EB)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp, bottom = 20.dp)
                )

                Text(
                    text = "YOUR BIO",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    letterSpacing = 1.sp
                )
                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    placeholder = { Text("Tell your circle who you are", fontSize = 15.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandPrimary,
                        unfocusedBorderColor = Color(0xFFE5E7EB)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(top = 6.dp, bottom = 24.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PICK YOUR VIBES",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "${vibes.size} selected ${if (minimumVibesSelected) "✓" else "• Needs 3"}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (minimumVibesSelected) BrandSage else HeartRed
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Flow row vibes selector
                Column(modifier = Modifier.fillMaxWidth()) {
                    val rows = sampleVibes.chunked(3)
                    rows.forEach { rowList ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowList.forEach { vibe ->
                                val selected = vibes.contains(vibe)
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(if (selected) BrandPrimary else BrandCream)
                                        .border(
                                            1.dp,
                                            if (selected) Color.Transparent else Color.LightGray,
                                            RoundedCornerShape(20.dp)
                                        )
                                        .clickable {
                                            if (selected) vibes.remove(vibe) else vibes.add(vibe)
                                        }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = vibe,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selected) Color.White else BrandInk
                                    )
                                }
                            }
                            if (rowList.size < 3) {
                                Spacer(modifier = Modifier.weight((3 - rowList.size).toFloat()))
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
        }

        // Action CTA
        Box(modifier = Modifier.padding(24.dp)) {
            GradientButton(
                text = "Continue",
                onClick = {
                    viewModel.registerNewUserProfile(name, bio, vibes, null)
                    onComplete()
                },
                enabled = name.isNotEmpty() && minimumVibesSelected
            )
        }
    }
}
