package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CommentEntity
import com.example.data.PostEntity
import com.example.ui.theme.*
import com.example.viewmodel.CircleUpViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius

// ==========================================
// ANATOMICAL HUMAN HEART (CUSTOM CANVAS)
// ==========================================

@Composable
fun AnatomicalHeartIcon(
    size: Int = 26,
    filled: Boolean = false,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (filled) 1.25f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessMedium),
        label = "Pulse"
    )

    Canvas(
        modifier = modifier
            .size(size.dp)
            .scale(scale)
    ) {
        val w = size.dp.toPx()
        val h = size.dp.toPx()

        if (!filled) {
            // Outline state: clean standard wireframe shape
            drawCircle(
                color = Color.LightGray,
                radius = w / 2.2f,
                style = Stroke(width = 1.5.dp.toPx())
            )
            drawCircle(
                color = HeartRed,
                radius = w / 5f,
                style = Fill
            )
        } else {
            // Draw anatomical muscle and aorta details
            // Ventricle muscular body
            drawCircle(
                color = HeartRed,
                radius = w / 2.2f,
                style = Fill
            )

            // Aorta main arch (blue/red vascular tubes)
            drawRoundRect(
                color = Color(0xFF1565C0), // Blue Vena Cava
                topLeft = Offset(w * 0.35f, w * 0.1f),
                size = Size(w * 0.12f, h * 0.3f),
                cornerRadius = CornerRadius(4f, 4f)
            )
            drawRoundRect(
                color = HeartRed, // Red Aorta
                topLeft = Offset(w * 0.52f, w * 0.05f),
                size = Size(w * 0.14f, h * 0.35f),
                cornerRadius = CornerRadius(4f, 4f)
            )

            // Outer contour outline to emphasize detail
            drawCircle(
                color = BrandInk,
                radius = w / 2.2f,
                style = Stroke(width = 1.5.dp.toPx())
            )

            // Vascular veins (yellow/coronary lines)
            drawLine(
                color = Color(0xFFFF8A80),
                start = Offset(w * 0.5f, h * 0.5f),
                end = Offset(w * 0.7f, h * 0.8f),
                strokeWidth = 2.dp.toPx()
            )
            drawLine(
                color = Color(0xFFFF8A80),
                start = Offset(w * 0.5f, h * 0.5f),
                end = Offset(w * 0.3f, h * 0.75f),
                strokeWidth = 1.5.dp.toPx()
            )
        }
    }
}

// ==========================================
// TOP BAR
// ==========================================

@Composable
fun MainTopBar(
    onSosClick: () -> Unit,
    onNotifClick: () -> Unit,
    onNeighbourhoodClick: () -> Unit,
    onCreatePostClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable { onNeighbourhoodClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Circle Up",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = BrandPrimary,
                letterSpacing = (-0.5).sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "▼", fontSize = 10.sp, color = BrandInk)
        }

        IconButton(
            onClick = onCreatePostClick,
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(BrandPrimary)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Create", tint = Color.White, modifier = Modifier.size(18.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Red SOS Trigger
        Card(
            onClick = onSosClick,
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = SosDark),
            modifier = Modifier.height(28.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "SOS",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        IconButton(onClick = onNotifClick, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = BrandInk)
        }
    }
}

// ==========================================
// STORIES BAR
// ==========================================

@Composable
fun StoriesBar(
    onStoryClick: (Int) -> Unit
) {
    val sampleStories = listOf(
        Pair("Your Story", true),
        Pair("Aanya", false),
        Pair("Rohan", false),
        Pair("Priya", false),
        Pair("Vikram", false),
        Pair("Sneha", false)
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(sampleStories.size) { idx ->
            val story = sampleStories[idx]
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onStoryClick(idx) }
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(
                            brush = if (story.second) Brush.linearGradient(colors = listOf(Color.LightGray, Color.LightGray)) else BrandGradient
                        )
                        .padding(2.5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(BrandGradientSoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = story.first.take(1).uppercase(),
                                fontWeight = FontWeight.Bold,
                                color = BrandPrimary,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = story.first, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = BrandInk)
            }
        }
    }
}

// ==========================================
// REACTION TRAY
// ==========================================

val FingerReactions = listOf(
    Pair("👍", "Like"),
    Pair("👆", "Notice"),
    Pair("🖕", "Diss"),
    Pair("💍", "Engaged"),
    Pair("🤙", "I'm out")
)

@Composable
fun ReactionTray(
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = modifier
            .border(0.5.dp, Color.LightGray, RoundedCornerShape(24.dp))
            .wrapContentSize()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FingerReactions.forEach { item ->
                Text(
                    text = item.first,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .clickable { onSelect(item.first) }
                        .padding(4.dp)
                )
            }
        }
    }
}

// ==========================================
// POST CARD
// ==========================================

@Composable
fun PostCard(
    post: PostEntity,
    viewModel: CircleUpViewModel,
    onOpenPost: () -> Unit,
    onProfileClick: (String) -> Unit
) {
    var reactionState by remember { mutableStateOf(post.reaction) }
    var likesState by remember { mutableStateOf(post.likes) }
    var savedState by remember { mutableStateOf(false) }
    var reactionTrayOpen by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Card(
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(BrandGradientSoft)
                        .clickable { onProfileClick(post.name) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = post.name.take(1).uppercase(), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandPrimary)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onProfileClick(post.name) }
                ) {
                    Text(text = post.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BrandInk)
                    Text(text = "${post.time} • HSR Layout", fontSize = 11.sp, color = Color.Gray)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(BrandCream)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(text = post.category, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                }
            }

            // Image Placeholder (double-tap to like)
            if (post.imageEmoji != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(BrandGradientSoft)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onDoubleTap = {
                                    if (reactionState == null) {
                                        reactionState = "👍"
                                        likesState += 1
                                        viewModel.handlePostReaction(post.id, likesState, "👍")
                                    }
                                },
                                onTap = { onOpenPost() }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = post.imageEmoji, fontSize = 72.sp)
                }
            }

            // Actions row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Interactive Thumb Reaction (Dual Gestures)
                Box {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (reactionState != null) BrandPrimary.copy(alpha = 0.1f) else BrandCream)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = { reactionTrayOpen = true },
                                    onTap = {
                                        if (reactionState != null) {
                                            reactionState = null
                                            likesState -= 1
                                        } else {
                                            reactionState = "👍"
                                            likesState += 1
                                        }
                                        viewModel.handlePostReaction(post.id, likesState, reactionState)
                                    }
                                )
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = reactionState ?: "👍", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = likesState.toString(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (reactionState != null) BrandPrimary else BrandInk
                        )
                    }

                    if (reactionTrayOpen) {
                        ReactionTray(
                            onSelect = { emoji ->
                                if (reactionState == null) likesState += 1
                                reactionState = emoji
                                reactionTrayOpen = false
                                viewModel.handlePostReaction(post.id, likesState, emoji)
                            },
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .offset(y = (-48).dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Comments Action
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(BrandCream)
                        .clickable { onOpenPost() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Comment, contentDescription = "Comment", modifier = Modifier.size(15.dp), tint = BrandInk)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = post.commentsCount.toString(), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BrandInk)
                }

                Spacer(modifier = Modifier.weight(1f))

                // Saved Action
                IconButton(onClick = { savedState = !savedState }) {
                    Icon(
                        imageVector = if (savedState) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save",
                        tint = BrandPrimary
                    )
                }
            }

            // Caption
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
            ) {
                Text(
                    text = post.caption,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = BrandInk
                )
            }
        }
    }
}

// ==========================================
// FEED SCREEN TIMELINE
// ==========================================

@Composable
fun FeedTimeline(
    viewModel: CircleUpViewModel,
    onSosClick: () -> Unit,
    onNotifClick: () -> Unit,
    onNeighbourhoodClick: () -> Unit,
    onCreatePostClick: () -> Unit,
    onOpenPost: (PostEntity) -> Unit,
    onProfileClick: (String) -> Unit
) {
    val postsList by viewModel.posts.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandCream)
    ) {
        MainTopBar(
            onSosClick = onSosClick,
            onNotifClick = onNotifClick,
            onNeighbourhoodClick = onNeighbourhoodClick,
            onCreatePostClick = onCreatePostClick
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                StoriesBar(onStoryClick = {})
            }
            items(postsList) { post ->
                PostCard(
                    post = post,
                    viewModel = viewModel,
                    onOpenPost = { onOpenPost(post) },
                    onProfileClick = onProfileClick
                )
            }
        }
    }
}

// ==========================================
// POST DETAIL SCREEN
// ==========================================

@Composable
fun PostDetailScreen(
    post: PostEntity,
    viewModel: CircleUpViewModel,
    onBack: () -> Unit,
    onProfileClick: (String) -> Unit
) {
    var commentText by remember { mutableStateOf("") }
    val commentsList: List<CommentEntity> by viewModel.getCommentsForPost(post.id).collectAsState(initial = emptyList())

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
            Text(text = "Comments", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                PostCard(
                    post = post,
                    viewModel = viewModel,
                    onOpenPost = {},
                    onProfileClick = onProfileClick
                )
                Divider(color = Color(0xFFF3F4F6), thickness = 8.dp)
            }

            items(commentsList) { comment ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(BrandGradientSoft),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = comment.name.take(1).uppercase(), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BrandPrimary)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = comment.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BrandInk)
                        Text(text = comment.text, fontSize = 13.sp, color = BrandInk, modifier = Modifier.padding(top = 2.dp))
                        Text(text = comment.time, fontSize = 10.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
                    }
                    
                    // Anatomical Heart Comment Reaction
                    IconButton(
                        onClick = {
                            viewModel.toggleCommentLike(
                                comment.id,
                                if (comment.liked) comment.likes - 1 else comment.likes + 1,
                                !comment.liked
                            )
                        }
                    ) {
                        AnatomicalHeartIcon(size = 14, filled = comment.liked)
                    }
                }
            }
        }

        // Add Comment Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = commentText,
                onValueChange = { commentText = it },
                placeholder = { Text("Add a comment...", fontSize = 13.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandPrimary,
                    unfocusedBorderColor = Color(0xFFE5E7EB)
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (commentText.isNotEmpty()) {
                        viewModel.postComment(post.id, commentText)
                        commentText = ""
                    }
                },
                enabled = commentText.isNotEmpty()
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = BrandPrimary)
            }
        }
    }
}

// ==========================================
// CREATE POST BOTTOM SHEET / DIALOG
// ==========================================

@Composable
fun CreatePostScreen(
    viewModel: CircleUpViewModel,
    onBack: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("General") }
    var caption by remember { mutableStateOf("") }
    var imageEmoji by remember { mutableStateOf("☕") }

    val categoriesList = listOf("Alert", "Buy/Sell", "Recommend", "Event", "General")
    val emojiOptions = listOf("☕", "🎮", "🥗", "🌱", "🧘", "🛍️", "🎨", "🏏", "🥞", "🏠")

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
            Text(text = "Create post", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = BrandInk)
            IconButton(onClick = onBack) {
                Text(text = "✕", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BrandInk)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "CHOOSE CATEGORY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            categoriesList.forEach { cat ->
                val active = selectedCategory == cat
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (active) BrandPrimary else BrandCream)
                        .clickable { selectedCategory = cat }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = cat,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (active) Color.White else Color.DarkGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "SELECT AN EMOJI GRAPHIC", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            emojiOptions.take(5).forEach { em ->
                val active = imageEmoji == em
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (active) BrandPrimary.copy(alpha = 0.15f) else BrandCream)
                        .border(1.dp, if (active) BrandPrimary else Color.Transparent, RoundedCornerShape(12.dp))
                        .clickable { imageEmoji = em },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = em, fontSize = 20.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = caption,
            onValueChange = { caption = it },
            placeholder = { Text("What do you want to share with your circle?", fontSize = 14.sp) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandPrimary,
                unfocusedBorderColor = Color(0xFFE5E7EB)
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        GradientButton(
            text = "Post to Circle",
            onClick = {
                if (caption.isNotEmpty()) {
                    viewModel.submitPost(selectedCategory, "#10B981", caption, imageEmoji)
                    onBack()
                }
            },
            enabled = caption.isNotEmpty()
        )
    }
}
