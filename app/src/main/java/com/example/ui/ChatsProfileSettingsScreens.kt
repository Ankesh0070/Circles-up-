package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChatEntity
import com.example.data.MessageEntity
import com.example.data.PostEntity
import com.example.data.Profile
import com.example.ui.theme.*
import com.example.viewmodel.CircleUpViewModel
import kotlinx.coroutines.delay

// ==========================================
// CHATS TIMELINE LIST
// ==========================================

@Composable
fun ChatsTimeline(
    viewModel: CircleUpViewModel,
    onChatClick: (ChatEntity) -> Unit,
    onNewChatClick: () -> Unit
) {
    var searchQ by remember { mutableStateOf("") }
    val chatsList by viewModel.chats.collectAsState()

    val filteredChats = if (searchQ.trim().isEmpty()) {
        chatsList
    } else {
        chatsList.filter { it.name.contains(searchQ, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Chats", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = BrandInk)
            IconButton(onClick = onNewChatClick) {
                Icon(Icons.Default.AddComment, contentDescription = "New chat", tint = BrandInk)
            }
        }

        OutlinedTextField(
            value = searchQ,
            onValueChange = { searchQ = it },
            placeholder = { Text("Search chats and groups", fontSize = 14.sp) },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = BrandInk, unfocusedTextColor = BrandInk,


                focusedBorderColor = BrandPrimary,
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedContainerColor = BrandCream,
                unfocusedContainerColor = BrandCream
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(filteredChats) { chat ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onChatClick(chat) }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Chat avatar / group emoji
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(BrandGradientSoft),
                        contentAlignment = Alignment.Center
                    ) {
                        if (chat.isGroup) {
                            Text(text = chat.emoji, fontSize = 24.sp)
                        } else {
                            Text(text = chat.name.take(1).uppercase(), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BrandPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = chat.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandInk)
                            Text(text = chat.time, fontSize = 11.sp, color = Color.Gray)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = chat.lastMessage, fontSize = 13.sp, color = Color.Gray, maxLines = 1)
                            if (chat.unread > 0) {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(BrandPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = chat.unread.toString(), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
                Divider(color = Color(0xFFF3F4F6), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

// ==========================================
// CHAT DETAILED SCREEN (MESSAGE FLOW)
// ==========================================

@Composable
fun ChatDetailScreen(
    chat: ChatEntity,
    viewModel: CircleUpViewModel,
    onBack: () -> Unit,
    onProfileClick: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    val messagesList: List<MessageEntity> by viewModel.getMessagesForChat(chat.id).collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(BrandGradientSoft)
                    .clickable { if (!chat.isGroup) onProfileClick(chat.name) },
                contentAlignment = Alignment.Center
            ) {
                if (chat.isGroup) {
                    Text(text = chat.emoji, fontSize = 18.sp)
                } else {
                    Text(text = chat.name.take(1).uppercase(), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandPrimary)
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = chat.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandInk)
                Text(
                    text = if (chat.isGroup) "24 members" else "Active now",
                    fontSize = 10.sp,
                    color = if (chat.isGroup) Color.Gray else BrandSage
                )
            }
        }

        // Messages timeline
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(BrandCream)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messagesList) { msg ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (msg.fromMe) Arrangement.End else Arrangement.Start
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (msg.fromMe) BrandPrimary else Color.White
                        ),
                        modifier = Modifier.widthIn(max = 260.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = msg.text,
                                color = if (msg.fromMe) Color.White else BrandInk,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }

        // Action Input Box
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.sendChatMessage(chat.id, "📸 Camera photograph captured") }) {
                Icon(Icons.Default.CameraAlt, contentDescription = "Camera", tint = BrandPrimary)
            }
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Message...", fontSize = 14.sp) },
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = BrandInk, unfocusedTextColor = BrandInk,


                    focusedBorderColor = BrandPrimary,
                    unfocusedBorderColor = Color(0xFFE5E7EB)
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (text.isNotEmpty()) {
                        viewModel.sendChatMessage(chat.id, text)
                        text = ""
                    }
                },
                enabled = text.isNotEmpty()
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = BrandPrimary)
            }
        }
    }
}

// ==========================================
// PROFILE VIEW
// ==========================================

@Composable
fun ProfileScreen(
    viewModel: CircleUpViewModel,
    onEditProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAchievementsClick: () -> Unit,
    onOpenPost: (PostEntity) -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val postsList by viewModel.posts.collectAsState()

    val userPosts = postsList.filter { it.isYou }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header Toolbar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = currentUser?.username ?: "aanya_sharma",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = BrandInk
                )
                IconButton(onClick = onSettingsClick) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = BrandInk)
                }
            }
        }

        // Profile Details Card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(BrandGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentUser?.name?.take(1)?.uppercase() ?: "C",
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(24.dp))

                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = userPosts.size.toString(), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Posts", fontSize = 11.sp, color = Color.Gray)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = currentUser?.followers ?: "1.2k", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text(text = "In Circle", fontSize = 11.sp, color = Color.Gray)
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { onAchievementsClick() }
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "30", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BrandPrimary)
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(text = "🔥", fontSize = 14.sp)
                            }
                            Text(text = "Streak", fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = currentUser?.name ?: "Aanya Sharma", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandInk)
                Text(text = "📍 HSR Layout • Tower B", fontSize = 11.sp, color = BrandPrimary, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 2.dp))
                Text(
                    text = currentUser?.bio ?: "chai pe charcha enthusiast ☕ • building society gossip queen",
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = BrandInk,
                    modifier = Modifier.padding(top = 8.dp)
                )

                // Vibes List
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    currentUser?.vibes?.take(3)?.forEach { vibe ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(BrandPrimary)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(text = vibe, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // CTA row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onEditProfileClick,
                        colors = ButtonDefaults.buttonColors(containerColor = BrandCream),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                    ) {
                        Text(text = "Edit Profile", color = BrandInk, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandCream),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                    ) {
                        Text(text = "Share Profile", color = BrandInk, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Achievements Header Card
        item {
            Card(
                onClick = onAchievementsClick,
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BrandCream),
                border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🏆", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "Achievements Badges", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BrandInk)
                            Text(text = "Rank #23 in HSR Layout", fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                }
            }
        }

        // Section post grid divider
        item {
            Divider(color = Color(0xFFF3F4F6), thickness = 8.dp, modifier = Modifier.padding(top = 16.dp))
        }

        // Posts list
        if (userPosts.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.GridOn, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(44.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "No posts yet", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                }
            }
        } else {
            // Simplified lists rendering of items
            items(userPosts) { post ->
                Card(
                    onClick = { onOpenPost(post) },
                    shape = RoundedCornerShape(0.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(BrandGradientSoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = post.imageEmoji ?: "📝", fontSize = 24.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = post.caption, fontSize = 13.sp, maxLines = 1, fontWeight = FontWeight.Bold)
                            Text(text = "${post.likes} likes • ${post.commentsCount} comments", fontSize = 11.sp, color = Color.Gray)
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                    }
                }
            }
        }
    }
}

// ==========================================
// EDIT PROFILE SCREEN FORM
// ==========================================

@Composable
fun EditProfileScreen(
    viewModel: CircleUpViewModel,
    onBack: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()

    var name by remember { mutableStateOf(currentUser?.name ?: "") }
    var bio by remember { mutableStateOf(currentUser?.bio ?: "") }

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
            Text(text = "Edit Profile", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = BrandInk)
            IconButton(onClick = onBack) {
                Text(text = "✕", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BrandInk)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "FULL NAME", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray, letterSpacing = 1.sp)
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = BrandInk, unfocusedTextColor = BrandInk, focusedBorderColor = BrandPrimary, unfocusedBorderColor = Color(0xFFE5E7EB)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp, bottom = 20.dp)
        )

        Text(text = "BIO DESCRIPTION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray, letterSpacing = 1.sp)
        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = BrandInk, unfocusedTextColor = BrandInk, focusedBorderColor = BrandPrimary, unfocusedBorderColor = Color(0xFFE5E7EB)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(top = 6.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        GradientButton(
            text = "Save changes",
            onClick = {
                viewModel.updateProfile(name, bio, currentUser?.vibes ?: emptyList())
                onBack()
            },
            enabled = name.isNotEmpty()
        )
    }
}

// ==========================================
// SETTINGS PREFERENCES SCREEN
// ==========================================

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onPageCreatorClick: () -> Unit,
    onAdsManagerClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var darkMode by remember { mutableStateOf(false) }
    var privateAccount by remember { mutableStateOf(false) }

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
            Text(text = "Settings", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            // General
            item {
                Text(text = "COMMUNITY TOOLS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray, letterSpacing = 1.sp, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
                
                Card(
                    onClick = onPageCreatorClick,
                    colors = CardDefaults.cardColors(containerColor = BrandCream),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Storefront, contentDescription = null, tint = BrandPrimary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "Create page", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Personal, Business or NGO Page", fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                }

                Card(
                    onClick = onAdsManagerClick,
                    colors = CardDefaults.cardColors(containerColor = BrandCream),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.TrendingUp, contentDescription = null, tint = BrandPrimary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "Ads Manager", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Manage reach & run ads", fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                }

                Text(text = "PRIVACY & PREFERENCES", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray, letterSpacing = 1.sp, modifier = Modifier.padding(bottom = 8.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = BrandCream),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = "Private account", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Switch(checked = privateAccount, onCheckedChange = { privateAccount = it })
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = BrandCream),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Brightness4, contentDescription = null, tint = Color.Gray)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = "Dark mode (coming soon)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Switch(checked = darkMode, onCheckedChange = { darkMode = it }, enabled = false)
                    }
                }

                Button(
                    onClick = onLogoutClick,
                    colors = ButtonDefaults.buttonColors(containerColor = HeartRed),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Log out account", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

// ==========================================
// ACHIEVEMENTS OVERVIEW
// ==========================================

@Composable
fun AchievementsScreen(onBack: () -> Unit) {
    val badges = listOf(
        Triple("Early Bird", "🌅", "Joined circle early"),
        Triple("Helpful", "🤝", "50+ helpful reactions"),
        Triple("Streak 30", "🔥", "30-day streak count"),
        Triple("Local Guide", "🗺️", "20+ local picks")
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
            Text(text = "Achievements", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .background(BrandGradient, RoundedCornerShape(20.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Total points", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                            Text(text = "2,847 pts", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Rank in HSR Layout", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                            Text(text = "#23", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }

                Text(text = "EARNED BADGES", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray, letterSpacing = 1.sp, modifier = Modifier.padding(bottom = 12.dp))
            }

            items(badges) { badge ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandCream),
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(BrandPrimary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = badge.second, fontSize = 24.sp)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = badge.first, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandInk)
                            Text(text = badge.third, fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}
