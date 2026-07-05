package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CampaignEntity
import com.example.data.CircleGenieEngine
import com.example.data.EventEntity
import com.example.data.PageEntity
import com.example.data.Profile
import com.example.ui.theme.*
import com.example.viewmodel.CircleUpViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ==========================================
// EXPLORE TAB MAIN
// ==========================================

@Composable
fun ExploreScreen(
    viewModel: CircleUpViewModel,
    onGenieClick: () -> Unit,
    onGuardClick: () -> Unit,
    onBazaarClick: () -> Unit,
    onScenesClick: () -> Unit,
    onProfileClick: (String) -> Unit,
    onTopicClick: (String) -> Unit
) {
    var searchQ by remember { mutableStateOf("") }
    val profilesList by viewModel.profiles.collectAsState()

    val filteredProfiles = if (searchQ.trim().isEmpty()) {
        profilesList
    } else {
        profilesList.filter {
            it.name.contains(searchQ, ignoreCase = true) || it.username.contains(searchQ, ignoreCase = true)
        }
    }

    val categories = listOf(
        Quadruple("Circle Genie", "Ask AI for local picks", Icons.Default.Star, BrandPrimary, onGenieClick),
        Quadruple("Circle Guard", "Stay safe, stay alert", Icons.Default.Security, SosDark, onGuardClick),
        Quadruple("Circle Bazaar", "Buy & sell with neighbours", Icons.Default.ShoppingBag, BrandAmber, onBazaarClick),
        Quadruple("Circle Scenes", "Local events near you", Icons.Default.CalendarToday, BrandDeep, onScenesClick)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(text = "Explore", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = BrandInk)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = searchQ,
                onValueChange = { searchQ = it },
                placeholder = { Text("Search people, posts, places...", fontSize = 14.sp) },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandPrimary,
                    unfocusedBorderColor = Color(0xFFE5E7EB),
                    focusedContainerColor = BrandCream,
                    unfocusedContainerColor = BrandCream
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            if (searchQ.trim().isEmpty()) {
                // Main Category Grid
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        val rows = categories.chunked(2)
                        rows.forEach { rowList ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowList.forEach { cat ->
                                    Card(
                                        onClick = cat.fourth,
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1.1f)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(12.dp),
                                            verticalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(cat.third.copy(alpha = 0.15f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(cat.second, contentDescription = null, tint = cat.third, modifier = Modifier.size(20.dp))
                                            }
                                            Column {
                                                Text(text = cat.first, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BrandInk)
                                                Text(text = cat.description, fontSize = 10.sp, color = Color.Gray, modifier = Modifier.padding(top = 2.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Society Connections
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "CIRCLE NEARBY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                items(profilesList) { profile ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = BrandCream),
                        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .clickable { onProfileClick(profile.name) }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(BrandGradientSoft),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = profile.name.take(1).uppercase(), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = BrandPrimary)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = profile.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BrandInk)
                                    if (profile.isVerified) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BrandPrimary, modifier = Modifier.size(12.dp))
                                    }
                                }
                                Text(text = "@${profile.username} • ${profile.tower}", fontSize = 11.sp, color = Color.Gray)
                                Text(text = profile.profession, fontSize = 11.sp, color = Color.DarkGray, modifier = Modifier.padding(top = 2.dp))
                            }
                            Button(
                                onClick = { onProfileClick(profile.name) }, // Correct profile reference click
                                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text(text = "+ Add", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                // Search Profiles Only
                items(filteredProfiles) { profile ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onProfileClick(profile.name) }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(BrandGradientSoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = profile.name.take(1).uppercase(), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandPrimary)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = profile.name, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(text = "@${profile.username} • ${profile.followers} in circle", fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

data class Quadruple<A, B, C, D, E>(
    val first: A,
    val description: B,
    val second: C,
    val third: D,
    val fourth: E
)

// ==========================================
// CIRCLE GENIE AI ASSISTANT (REST ENDPOINT)
// ==========================================

@Composable
fun GenieScreen(
    viewModel: CircleUpViewModel,
    onBack: () -> Unit
) {
    var queryText by remember { mutableStateOf("") }
    val responseText by remember { mutableStateOf("") } // Placeholder/Live response binding
    var loading by remember { mutableStateOf(false) }

    val chatBubbles = remember { mutableStateListOf<Pair<String, Boolean>>() } // Text, isMe

    val coroutineScope = rememberCoroutineScope()

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
            Spacer(modifier = Modifier.width(12.dp))
            Icon(Icons.Default.Star, contentDescription = null, tint = BrandPrimary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = "Circle Genie AI", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "Ask AI for local picks & answer", fontSize = 10.sp, color = Color.Gray)
            }
        }

        // Q&A List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (chatBubbles.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(BrandGradientSoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = BrandPrimary, modifier = Modifier.size(32.dp))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Apni circle se poocho", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = BrandInk)
                        Text(
                            text = "Real recommendations from real neighbours, powered by Gemini 3.5 Flash",
                            fontSize = 13.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 30.dp, end = 30.dp, top = 6.dp)
                        )
                    }
                }
            } else {
                items(chatBubbles) { bubble ->
                    val isError = !bubble.second && (
                        bubble.first.contains("Error") ||
                        bubble.first.contains("Issue") ||
                        bubble.first.contains("down") ||
                        bubble.first.startsWith("🔑") ||
                        bubble.first.startsWith("🌐") ||
                        bubble.first.startsWith("🤖") ||
                        bubble.first.startsWith("⚠️")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (bubble.second) Arrangement.End else Arrangement.Start
                    ) {
                        if (isError) {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFFEF2F2)
                                ),
                                modifier = Modifier
                                    .widthIn(max = 300.dp)
                                    .padding(vertical = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Warning,
                                            contentDescription = "Error",
                                            tint = Color(0xFFEF4444),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "System Report",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = Color(0xFF991B1B)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = bubble.first,
                                        color = Color(0xFF7F1D1D),
                                        fontSize = 12.5.sp,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        } else {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (bubble.second) BrandPrimary else BrandCream
                                ),
                                modifier = Modifier.widthIn(max = 280.dp)
                            ) {
                                Text(
                                    text = bubble.first,
                                    color = if (bubble.second) Color.White else BrandInk,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                }
            }

            if (loading) {
                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = BrandCream)
                        ) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = BrandPrimary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "Genie is thinking...", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Input
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = queryText,
                onValueChange = { queryText = it },
                placeholder = { Text("Ask your Circle Genie...", fontSize = 14.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandPrimary,
                    unfocusedBorderColor = Color(0xFFE5E7EB)
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (queryText.isNotEmpty()) {
                        val userQ = queryText
                        chatBubbles.add(Pair(userQ, true))
                        queryText = ""
                        loading = true

                        // Call CircleGenieEngine using coroutines
                        com.example.ui.launchGenieTask(coroutineScope, userQ, chatBubbles) {
                            loading = false
                        }
                    }
                },
                enabled = queryText.isNotEmpty()
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = BrandPrimary)
            }
        }
    }
}

fun launchGenieTask(
    scope: kotlinx.coroutines.CoroutineScope,
    query: String,
    list: MutableList<Pair<String, Boolean>>,
    onComplete: () -> Unit
) {
    scope.launch {
        val answer = CircleGenieEngine.askGenie(query)
        list.add(Pair(answer, false))
        onComplete()
    }
}

// ==========================================
// LOCAL BAZAAR SCREEN
// ==========================================

@Composable
fun BazaarScreen(onBack: () -> Unit, onProfileClick: (String) -> Unit) {
    val listings = listOf(
        BazaarItem("IKEA Study Desk", "₹3,200", "Rohan Mehta", "Furniture", "Like new", "🪑"),
        BazaarItem("PS5 Slim + 2 controllers", "₹38,000", "Karan Singh", "Electronics", "Used 6 months", "🎮"),
        BazaarItem("Atomic Habits book", "FREE", "Sneha Iyer", "Books", "Giveaway", "📚")
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
            Column {
                Text(text = "Circle Bazaar", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = "Buy & sell with verified neighbours", fontSize = 11.sp, color = Color.Gray)
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listings) { item ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandCream),
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(BrandGradientSoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = item.emoji, fontSize = 32.sp)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = item.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandInk)
                            Text(
                                text = item.price,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = if (item.price == "FREE") BrandSage else BrandPrimary,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                            Text(
                                text = "${item.seller} • ${item.condition}",
                                fontSize = 11.sp,
                                color = Color.Gray,
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .clickable { onProfileClick(item.seller) }
                            )
                        }
                        Button(
                            onClick = { },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                        ) {
                            Text(text = "Chat", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

data class BazaarItem(
    val title: String,
    val price: String,
    val seller: String,
    val cat: String,
    val condition: String,
    val emoji: String
)

// ==========================================
// LOCAL EVENTS (SCENES) SCREEN
// ==========================================

@Composable
fun ScenesScreen(
    viewModel: CircleUpViewModel,
    onBack: () -> Unit,
    onCreateEvent: () -> Unit
) {
    val eventsList by viewModel.events.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = "Circle Scenes", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = "Meetups & activities near you", fontSize = 11.sp, color = Color.Gray)
                }
            }
            IconButton(onClick = onCreateEvent) {
                Icon(Icons.Default.Add, contentDescription = "Create Event")
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(eventsList) { event ->
                var goingStatus by remember { mutableStateOf(event.rsvpStatus) }
                var rsvpCount by remember { mutableStateOf(event.goingCount) }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandCream),
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        // Header graphic
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .background(BrandGradient),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = event.emoji, fontSize = 36.sp)
                        }

                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = event.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = BrandInk)
                            Text(text = "hosted by ${event.host}", fontSize = 11.sp, color = Color.Gray)

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = event.whenTime, fontSize = 11.sp, color = Color.DarkGray)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = event.location, fontSize = 11.sp, color = Color.DarkGray)
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        if (goingStatus == "going") {
                                            goingStatus = null
                                            rsvpCount -= 1
                                        } else {
                                            if (goingStatus == "maybe") rsvpCount += 1 // moving from maybe to going delta adjustment
                                            goingStatus = "going"
                                        }
                                        viewModel.rsvpToEvent(event.id, goingStatus, rsvpCount)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (goingStatus == "going") BrandSage else BrandPrimary
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = if (goingStatus == "going") "Going ✓" else "RSVP Going", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        goingStatus = if (goingStatus == "maybe") null else "maybe"
                                        viewModel.rsvpToEvent(event.id, goingStatus, rsvpCount)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (goingStatus == "maybe") BrandSage else Color.LightGray
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = if (goingStatus == "maybe") "Maybe ✓" else "Maybe", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// HOST SCENE CREATE FORM
// ==========================================

@Composable
fun CreateEventScreen(
    viewModel: CircleUpViewModel,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var dateText by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var privacy by remember { mutableStateOf("neighbours") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Host a new event", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = BrandInk)
            IconButton(onClick = onBack) {
                Text(text = "✕", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BrandInk)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            placeholder = { Text("Event Name (e.g. Holi Pool Party)", fontSize = 14.sp) },
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = Color(0xFFE5E7EB)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            placeholder = { Text("Location (e.g. Clubhouse)", fontSize = 14.sp) },
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = Color(0xFFE5E7EB)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = dateText,
            onValueChange = { dateText = it },
            placeholder = { Text("Date & Time (e.g. Sunday, 12 PM)", fontSize = 14.sp) },
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = Color(0xFFE5E7EB)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = desc,
            onValueChange = { desc = it },
            placeholder = { Text("Description (optional)", fontSize = 14.sp) },
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = Color(0xFFE5E7EB)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        GradientButton(
            text = "Create Event",
            onClick = {
                if (title.isNotEmpty() && location.isNotEmpty() && dateText.isNotEmpty()) {
                    viewModel.createEvent(title, location, dateText, desc, privacy, "🎉", "")
                    onBack()
                }
            },
            enabled = title.isNotEmpty() && location.isNotEmpty() && dateText.isNotEmpty()
        )
    }
}

// ==========================================
// PAGES CREATOR & ADS MANAGER
// ==========================================

@Composable
fun CreatePageScreen(
    viewModel: CircleUpViewModel,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var handle by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Business") }
    var tagline by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    val pageTypes = listOf("Business", "Personal", "NGO")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Create a page", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = BrandInk)
            IconButton(onClick = onBack) {
                Text(text = "✕", fontSize = 18.sp, color = BrandInk)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "PAGE TYPE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            pageTypes.forEach { type ->
                val active = category == type
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (active) BrandPrimary else BrandCream)
                        .clickable { category = type }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = type,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (active) Color.White else Color.DarkGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text("Page name (e.g. Sharma Ji Ka Chai)", fontSize = 14.sp) },
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = Color(0xFFE5E7EB)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = handle,
            onValueChange = { handle = it },
            placeholder = { Text("Page handle (e.g. sharma_chai)", fontSize = 14.sp) },
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = Color(0xFFE5E7EB)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = tagline,
            onValueChange = { tagline = it },
            placeholder = { Text("Tagline (e.g. The best tapri tea)", fontSize = 14.sp) },
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = Color(0xFFE5E7EB)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = desc,
            onValueChange = { desc = it },
            placeholder = { Text("About description...", fontSize = 14.sp) },
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandPrimary, unfocusedBorderColor = Color(0xFFE5E7EB)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        GradientButton(
            text = "Create Page",
            onClick = {
                if (name.isNotEmpty() && handle.isNotEmpty()) {
                    viewModel.createPage(name, handle, category, tagline, desc, "", category == "NGO")
                    onBack()
                }
            },
            enabled = name.isNotEmpty() && handle.isNotEmpty()
        )
    }
}

// ==========================================
// ADS MANAGER CAMPAIGN LIST
// ==========================================

@Composable
fun AdsManagerScreen(
    viewModel: CircleUpViewModel,
    onBack: () -> Unit,
    onCreateAd: () -> Unit
) {
    val campaignsList by viewModel.campaigns.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = "Ads Manager", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = "Promote your pages and boost reach", fontSize = 11.sp, color = Color.Gray)
                }
            }
            IconButton(onClick = onCreateAd) {
                Icon(Icons.Default.Add, contentDescription = "Create Ad")
            }
        }

        // Summary Hero Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(BrandGradient, RoundedCornerShape(20.dp))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "TOTAL SPENT (LAST 30 DAYS)", color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Text(text = "₹2,340", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(top = 4.dp))
                Text(text = "reaching ~18.4K local neighbours", color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp)
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(campaignsList) { campaign ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandCream),
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = campaign.emoji, fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(text = campaign.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BrandInk)
                                    Text(text = "${campaign.pageName} • ${campaign.objective}", fontSize = 11.sp, color = Color.Gray)
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (campaign.status == "active") Color(0xFFDCFCE7) else Color.LightGray.copy(alpha = 0.3f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = campaign.status.uppercase(),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (campaign.status == "active") Color(0xFF15803D) else Color.DarkGray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text(text = "Spent", fontSize = 10.sp, color = Color.Gray)
                                Text(text = campaign.spent, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text(text = "Reach", fontSize = 10.sp, color = Color.Gray)
                                Text(text = campaign.reach, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text(text = "Clicks", fontSize = 10.sp, color = Color.Gray)
                                Text(text = campaign.clicks, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text(text = "CTR", fontSize = 10.sp, color = Color.Gray)
                                Text(text = campaign.ctr, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BrandPrimary)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { viewModel.toggleCampaignStatus(campaign.id, campaign.status) },
                                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = if (campaign.status == "active") "Pause" else "Resume", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// AD CREATIVE COMPILER
// ==========================================

@Composable
fun CreateAdScreen(
    viewModel: CircleUpViewModel,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var pageName by remember { mutableStateOf("Sharma Ji Ka Chai") }
    var objective by remember { mutableStateOf("Reach") }
    var budgetAmount by remember { mutableStateOf("100") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Create campaign", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = BrandInk)
            IconButton(onClick = onBack) {
                Text(text = "✕", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BrandInk)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "CAMPAIGN NAME", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text("e.g. Cardi launch promotion", fontSize = 14.sp) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandPrimary,
                unfocusedBorderColor = Color(0xFFE5E7EB)
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp, bottom = 16.dp)
        )

        Text(text = "CHOOSE OBJECTIVE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        val objectives = listOf("Reach", "Engagement", "Traffic")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            objectives.forEach { obj ->
                val active = objective == obj
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (active) BrandPrimary else BrandCream)
                        .clickable { objective = obj }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = obj,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (active) Color.White else Color.DarkGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "DAILY BUDGET (₹)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        OutlinedTextField(
            value = budgetAmount,
            onValueChange = { budgetAmount = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

        GradientButton(
            text = "Publish Ad",
            onClick = {
                if (name.isNotEmpty() && budgetAmount.isNotEmpty()) {
                    viewModel.createAdCampaign(
                        name = name,
                        pageName = pageName,
                        objective = objective,
                        budget = "₹$budgetAmount/day",
                        emoji = "📈",
                        gradient = "linear-gradient(135deg, #10B981, #059669)",
                        endsIn = "7 days left"
                    )
                    onBack()
                }
            },
            enabled = name.isNotEmpty() && budgetAmount.isNotEmpty()
        )
    }
}
