package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AlertEntity
import com.example.data.CampaignEntity
import com.example.data.ChatEntity
import com.example.data.CircleGenieEngine
import com.example.data.CircleUpDatabase
import com.example.data.CommentEntity
import com.example.data.EventEntity
import com.example.data.MessageEntity
import com.example.data.PageEntity
import com.example.data.PostEntity
import com.example.data.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CircleUpViewModel(application: Application) : AndroidViewModel(application) {

    private val db = CircleUpDatabase.getDatabase(application)
    private val dao = db.dao()

    // Expose Room queries as highly reactive StateFlows
    val profiles: StateFlow<List<Profile>> = dao.getAllProfiles().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val posts: StateFlow<List<PostEntity>> = dao.getAllPosts().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val chats: StateFlow<List<ChatEntity>> = dao.getAllChats().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val events: StateFlow<List<EventEntity>> = dao.getAllEvents().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val pages: StateFlow<List<PageEntity>> = dao.getAllPages().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val campaigns: StateFlow<List<CampaignEntity>> = dao.getAllCampaigns().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val alerts: StateFlow<List<AlertEntity>> = dao.getAllAlerts().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Current Logged-in User Profile State (Stored in Flow / Database)
    private val _currentUser = MutableStateFlow<Profile?>(null)
    val currentUser = _currentUser.asStateFlow()

    // Circle Genie (AI Search) States
    private val _genieAnswer = MutableStateFlow<String?>(null)
    val genieAnswer = _genieAnswer.asStateFlow()

    private val _genieLoading = MutableStateFlow(false)
    val genieLoading = _genieLoading.asStateFlow()

    init {
        viewModelScope.launch {
            // Check if profiles are empty; if so, pre-populate the Room database with standard mock data
            prepopulateDatabaseIfEmpty()
        }
    }

    private suspend fun prepopulateDatabaseIfEmpty() {
        val count = dao.getProfileByUsername("aanya_sharma")
        if (count == null) {
            // 1. Initialise Profiles
            val initialProfiles = listOf(
                Profile(
                    username = "aanya_sharma",
                    name = "Aanya Sharma",
                    bio = "chai pe charcha enthusiast ☕ • plant parent • books > netflix",
                    avatar = "linear-gradient(135deg, #FCA5A5, #F9A8D4)",
                    postsCount = 47,
                    followers = "1.2k",
                    following = 234,
                    isVerified = true,
                    society = "Brigade Meadows",
                    tower = "Tower B",
                    flat = "B-1204",
                    profession = "Marketing Manager @ Razorpay",
                    distance = "0.2 km",
                    mutuals = 4,
                    vibes = listOf("Chai Lover", "Plant Parent", "Bookworm")
                ),
                Profile(
                    username = "rohan.gg",
                    name = "Rohan Mehta",
                    bio = "gamer 🎮 • PS5 collector • Bengaluru → Pune soon",
                    avatar = "linear-gradient(135deg, #93C5FD, #6366F1)",
                    postsCount = 28,
                    followers = "456",
                    following = 189,
                    isVerified = false,
                    society = "Brigade Meadows",
                    tower = "Tower A",
                    flat = "A-805",
                    profession = "Software Dev @ Swiggy",
                    distance = "0.4 km",
                    mutuals = 6,
                    vibes = listOf("Gamer", "Late Night Owl", "Cinephile")
                ),
                Profile(
                    username = "priyak",
                    name = "Priya Kapoor",
                    bio = "mom of 2 🤱 • pediatric nurse • sleep deprived",
                    avatar = "linear-gradient(135deg, #C4B5FD, #8B5CF6)",
                    postsCount = 89,
                    followers = "892",
                    following = 412,
                    isVerified = true,
                    society = "Brigade Meadows",
                    tower = "Tower C",
                    flat = "C-301",
                    profession = "Pediatric Nurse",
                    distance = "0.3 km",
                    mutuals = 3,
                    vibes = listOf("Pet Parent", "Early Bird", "Helper")
                ),
                Profile(
                    username = "vikram_s",
                    name = "Vikram Singh",
                    bio = "fitness coach 💪 • day 47 streak • protein > everything",
                    avatar = "linear-gradient(135deg, #86EFAC, #10B981)",
                    postsCount = 156,
                    followers = "3.4k",
                    following = 89,
                    isVerified = true,
                    society = "Brigade Meadows",
                    tower = "Tower B",
                    flat = "B-602",
                    profession = "Fitness Coach @ Cult.fit",
                    distance = "0.2 km",
                    mutuals = 8,
                    vibes = listOf("Fitness Freak", "Gym Rat", "Runner")
                ),
                Profile(
                    username = "snehaa",
                    name = "Sneha Iyer",
                    bio = "plant mama 🌱 • monstera mafia • weekend brunches",
                    avatar = "linear-gradient(135deg, #FDBA74, #F59E0B)",
                    postsCount = 67,
                    followers = "789",
                    following = 234,
                    isVerified = false,
                    society = "Sobha Aspire",
                    tower = "Tower A",
                    flat = "A-1102",
                    profession = "UX Designer @ Flipkart",
                    distance = "0.7 km",
                    mutuals = 5,
                    vibes = listOf("Plant Parent", "Foodie", "Astrology Believer")
                ),
                Profile(
                    username = "karan.s",
                    name = "Karan Singh",
                    bio = "foodie 🍛 • biryani hunter • home chef wannabe",
                    avatar = "linear-gradient(135deg, #FDE68A, #F59E0B)",
                    postsCount = 34,
                    followers = "234",
                    following = 167,
                    isVerified = false,
                    society = "Brigade Meadows",
                    tower = "Tower D",
                    flat = "D-407",
                    profession = "Chef @ Toit",
                    distance = "0.5 km",
                    mutuals = 2,
                    vibes = listOf("Foodie", "Home Chef", "Biryani Hunter")
                )
            )
            dao.insertProfiles(initialProfiles)
            _currentUser.value = initialProfiles.first()

            // 2. Initialise Posts
            val initialPosts = listOf(
                PostEntity(
                    id = "p1",
                    name = "Aanya Sharma",
                    handle = "@aanya_s",
                    time = "12m",
                    category = "Recommend",
                    categoryColor = "#10B981",
                    caption = "Guys yeh wala chai stall at Gate 2 is *chef's kiss* 🤌 Sharma ji ka tapri — ₹15 only and best masala chai in HSR. Highly recommend before your 9am standup 🍵",
                    imageEmoji = "☕",
                    imageGradient = "linear-gradient(135deg, #92400E, #FCD34D)",
                    likes = 47,
                    commentsCount = 12,
                    hasStory = true
                ),
                PostEntity(
                    id = "p2",
                    name = "Rohan Mehta",
                    handle = "@rohan.gg",
                    time = "1h",
                    category = "Buy/Sell",
                    categoryColor = "#F59E0B",
                    caption = "Selling my PS5 (disc edition) + 2 controllers + Spider-Man 2, GoW Ragnarok, FIFA 24, Demon's Souls. Moving to Pune next week. Genuine sale only ₹42k negotiable. DM if interested 🎮",
                    imageEmoji = "🎮",
                    imageGradient = "linear-gradient(135deg, #1E293B, #94A3B8)",
                    likes = 89,
                    commentsCount = 34,
                    hasStory = true
                ),
                PostEntity(
                    id = "p3",
                    name = "Priya Kapoor",
                    handle = "@priyak",
                    time = "2h",
                    category = "Alert",
                    categoryColor = "#EF4444",
                    caption = "Loud music coming from B-wing 12th floor after 11:30 PM again 😤 Building society pe complaint kar diya. If anyone else getting disturbed pls also raise it. Sleep zaroori hai yaar 🙏",
                    imageEmoji = null,
                    imageGradient = null,
                    likes = 156,
                    commentsCount = 48,
                    hasStory = true
                ),
                PostEntity(
                    id = "p4",
                    name = "Sneha Iyer",
                    handle = "@snehaa",
                    time = "3h",
                    category = "Event",
                    categoryColor = "#A855F7",
                    caption = "HOLI POOL PARTY at clubhouse this Sunday 12 PM onwards 🎨💦 DJ Aman is coming, organic colors only, bhaang lassi for the 21+ crowd, kids zone separate. Entry ₹299 per person, food included. Comment to RSVP! 🌈",
                    imageEmoji = "🎨",
                    imageGradient = "linear-gradient(135deg, #EC4899, #F59E0B)",
                    likes = 234,
                    commentsCount = 67,
                    hasStory = true
                )
            )
            dao.insertPosts(initialPosts)

            // 3. Initialise Comments
            val initialComments = listOf(
                CommentEntity(postId = "p1", name = "Rohan Mehta", text = "Bhai daily jaata hu! 🔥", time = "8m", likes = 12),
                CommentEntity(postId = "p1", name = "Sneha Iyer", text = "Yes!! ₹15 me itni achi chai 🙏", time = "15m", likes = 8),
                CommentEntity(postId = "p2", name = "Karan Singh", text = "Bhai interested, DM ki", time = "30m", likes = 4),
                CommentEntity(postId = "p3", name = "Vikram Singh", text = "Same yaar, kal exam tha mera", time = "1h", likes = 19)
            )
            dao.insertComments(initialComments)

            // 4. Initialise Chats & Messages
            val initialChats = listOf(
                ChatEntity(id = 1, name = "Tower B Residents", lastMessage = "Aanya: kal water cut hai 9-12", time = "2m", unread = 5, isGroup = true, emoji = "🏢"),
                ChatEntity(id = 2, name = "Gamers Circle", lastMessage = "Karan: warzone tonight 10pm?", time = "15m", unread = 2, isGroup = true, emoji = "🎮"),
                ChatEntity(id = 3, name = "Pet Parents HSR", lastMessage = "Priya: Rocky ka grooming ho gaya 🐶", time = "1h", unread = 0, isGroup = true, emoji = "🐶"),
                ChatEntity(id = 4, name = "Aanya Sharma", lastMessage = "haan bhej de chai ki photo", time = "2h", unread = 0, isGroup = false, emoji = ""),
                ChatEntity(id = 5, name = "Plant Parents 🌱", lastMessage = "Sneha: monstera leaf yellow ho rahi", time = "4h", unread = 1, isGroup = true, emoji = "🌱")
            )
            dao.insertChats(initialChats)

            val initialMessages = listOf(
                MessageEntity(chatId = 4, fromMe = false, text = "Hey, are you going to the clubhouse Holi party?", time = "10:30 AM"),
                MessageEntity(chatId = 4, fromMe = true, text = "Yes! Standard masala chai pick before standup as well", time = "10:32 AM"),
                MessageEntity(chatId = 4, fromMe = false, text = "Awesome, see you there!", time = "10:33 AM")
              )
            initialMessages.forEach { dao.insertMessage(it) }

            // 5. Initialise Events
            val initialEvents = listOf(
                EventEntity(
                    id = "e1",
                    title = "Holi Celebrations 2026",
                    host = "Building Society",
                    whenTime = "Sun, Mar 22 • 11:00 AM",
                    location = "Clubhouse lawn",
                    emoji = "🎨",
                    gradient = "linear-gradient(135deg, #EC4899, #F59E0B)",
                    goingCount = 47,
                    weekOnly = true,
                    privacy = "neighbours",
                    description = "Holi organic colors only, bhaang lassi available for 21+"
                ),
                EventEntity(
                    id = "e2",
                    title = "Sunday Morning Yoga",
                    host = "Sneha Iyer",
                    whenTime = "Sun, Mar 22 • 6:30 AM",
                    location = "Park near Tower C",
                    emoji = "🧘",
                    gradient = "linear-gradient(135deg, #10B981, #34D399)",
                    goingCount = 18,
                    weekOnly = true,
                    privacy = "neighbours",
                    description = "Bring your own mat. Light herbal infusion will be served."
                ),
                EventEntity(
                    id = "e3",
                    title = "Garage Sale",
                    host = "Tower B Residents",
                    whenTime = "Sat, Mar 28 • 10:00 AM",
                    location = "Tower B parking",
                    emoji = "🛍️",
                    gradient = "linear-gradient(135deg, #F59E0B, #FCD34D)",
                    goingCount = 23,
                    weekOnly = false,
                    privacy = "neighbours",
                    description = "Pre-loved books, toys, kitchen appliances, and electronics."
                )
            )
            dao.insertEvents(initialEvents)

            // 6. Initialise Campaigns
            val initialCampaigns = listOf(
                CampaignEntity(
                    id = "c1",
                    name = "Diwali special — 20% off chai",
                    pageName = "Sharma Ji Ka Chai",
                    objective = "Reach",
                    status = "active",
                    budget = "₹150/day",
                    spent = "₹1,420",
                    reach = "12.3K",
                    clicks = "894",
                    ctr = "7.3%",
                    endsIn = "4 days left",
                    emoji = "🪔",
                    gradient = "linear-gradient(135deg, #F59E0B, #DC2626)"
                ),
                CampaignEntity(
                    id = "c2",
                    name = "New cardamom blend launch",
                    pageName = "Sharma Ji Ka Chai",
                    objective = "Engagement",
                    status = "active",
                    budget = "₹80/day",
                    spent = "₹640",
                    reach = "4.8K",
                    clicks = "287",
                    ctr = "5.9%",
                    endsIn = "8 days left",
                    emoji = "☕",
                    gradient = "linear-gradient(135deg, #10B981, #059669)"
                )
            )
            dao.insertCampaigns(initialCampaigns)

            // 7. Initialise Alerts
            val initialAlerts = listOf(
                AlertEntity(
                    id = "a1",
                    name = "Brigade Society",
                    time = "15m",
                    text = "🚨 Suspicious person spotted near gate 3. Security informed. Sab alert raho.",
                    severity = "high"
                ),
                AlertEntity(
                    id = "a2",
                    name = "HSR Police",
                    time = "2h",
                    text = "⚠️ Chain snatching reported in 5th Sector. Avoid wearing valuables in late evening.",
                    severity = "medium"
                )
            )
            dao.insertAlerts(initialAlerts)

            // 8. Prepopulate Default Pages
            val initialPages = listOf(
                PageEntity(
                    id = "page1",
                    name = "Sharma Ji Ka Chai",
                    handle = "sharma_ji_chai",
                    category = "Food & Drink",
                    emoji = "🍵",
                    followers = 234,
                    postsCount = 18,
                    verified = true,
                    isNgo = false,
                    tagline = "The absolute best tapri tea in HSR Layout",
                    description = "Freshly brewed standard masala tea, cardamom special blend, ginger shots, and piping hot snacks since 2018."
                )
            )
            initialPages.forEach { dao.insertPage(it) }
        }
    }

    // ==========================================
    // BUSINESS LOGIC OPERATIONS
    // ==========================================

    fun updateProfile(name: String, bio: String, vibes: List<String>) {
        viewModelScope.launch {
            val user = _currentUser.value ?: return@launch
            val updated = user.copy(name = name, bio = bio, vibes = vibes)
            dao.insertProfile(updated)
            _currentUser.value = updated
        }
    }

    fun registerNewUserProfile(name: String, bio: String, vibes: List<String>, imageUri: String?) {
        viewModelScope.launch {
            val newUser = Profile(
                username = "aanya_sharma", // keep it aligned with original name state
                name = name,
                bio = bio,
                avatar = imageUri ?: "linear-gradient(135deg, #FCA5A5, #F9A8D4)",
                postsCount = 0,
                followers = "1.0k",
                following = 120,
                isVerified = true,
                society = "Brigade Meadows",
                tower = "Tower B",
                flat = "B-1204",
                profession = "HSR Local Creator",
                distance = "0.0 km",
                mutuals = 0,
                vibes = vibes
            )
            dao.insertProfile(newUser)
            _currentUser.value = newUser
        }
    }

    fun submitPost(category: String, categoryColor: String, caption: String, emoji: String?) {
        viewModelScope.launch {
            val user = _currentUser.value ?: return@launch
            val newPost = PostEntity(
                id = "p_" + System.currentTimeMillis(),
                name = user.name,
                handle = "@" + user.username,
                time = "Just now",
                category = category,
                categoryColor = categoryColor,
                caption = caption,
                imageEmoji = emoji,
                imageGradient = "linear-gradient(135deg, #2196D6, #1976C2)",
                likes = 0,
                commentsCount = 0,
                hasStory = false,
                isYou = true
            )
            dao.insertPost(newPost)
        }
    }

    fun handlePostReaction(postId: String, likes: Int, reaction: String?) {
        viewModelScope.launch {
            dao.updatePostReaction(postId, likes, reaction)
        }
    }

    fun getCommentsForPost(postId: String): Flow<List<CommentEntity>> {
        return dao.getCommentsForPost(postId)
    }

    fun postComment(postId: String, text: String) {
        viewModelScope.launch {
            val user = _currentUser.value ?: return@launch
            val newComment = CommentEntity(
                postId = postId,
                name = user.name,
                text = text,
                time = "Just now",
                likes = 0,
                liked = false,
                isOp = true
            )
            dao.insertComment(newComment)
        }
    }

    fun toggleCommentLike(commentId: Int, likes: Int, liked: Boolean) {
        viewModelScope.launch {
            dao.updateCommentLike(commentId, likes, liked)
        }
    }

    fun createChat(chat: ChatEntity) {
        viewModelScope.launch {
            dao.insertChat(chat)
        }
    }

    fun getMessagesForChat(chatId: Int): Flow<List<MessageEntity>> {
        return dao.getMessagesForChat(chatId)
    }

    fun sendChatMessage(chatId: Int, text: String, isMedia: Boolean = false, mediaType: String? = null) {
        viewModelScope.launch {
            val msg = MessageEntity(
                chatId = chatId,
                fromMe = true,
                text = text,
                time = "now",
                isMedia = isMedia,
                mediaType = mediaType
            )
            dao.insertMessage(msg)
            dao.updateChatState(chatId, "me: $text", "now", 0)

            // Auto reply for prototype completeness
            launch {
                kotlinx.coroutines.delay(1200)
                val reply = MessageEntity(
                    chatId = chatId,
                    fromMe = false,
                    text = "Haan haan samjha, kal milte hain! 😄",
                    time = "now"
                )
                dao.insertMessage(reply)
                dao.updateChatState(chatId, "Them: haan haan samjha...", "now", 0)
            }
        }
    }

    fun rsvpToEvent(eventId: String, status: String?, goingDelta: Int) {
        viewModelScope.launch {
            dao.updateEventRsvp(eventId, status, goingDelta)
        }
    }

    fun createEvent(title: String, location: String, whenTime: String, description: String, privacy: String, emoji: String, rsvpLimit: String) {
        viewModelScope.launch {
            val user = _currentUser.value ?: return@launch
            val newEvent = EventEntity(
                id = "e_" + System.currentTimeMillis(),
                title = title,
                host = user.name,
                whenTime = whenTime,
                location = location,
                emoji = emoji,
                gradient = "linear-gradient(135deg, #4FB5E8, #1976C2)",
                goingCount = 1,
                weekOnly = true,
                privacy = privacy,
                description = description + if (rsvpLimit.isNotEmpty()) " (RSVP limit: $rsvpLimit)" else "",
                rsvpStatus = "going"
            )
            dao.insertEvent(newEvent)
        }
    }

    fun createPage(name: String, handle: String, category: String, tagline: String, description: String, extraValue: String, isNgo: Boolean) {
        viewModelScope.launch {
            val newPage = PageEntity(
                id = "page_" + System.currentTimeMillis(),
                name = name,
                handle = handle,
                category = category,
                emoji = if (isNgo) "💚" else "🏢",
                followers = 0,
                postsCount = 0,
                verified = false,
                isNgo = isNgo,
                tagline = tagline,
                description = description,
                extraValue = extraValue
            )
            dao.insertPage(newPage)
        }
    }

    fun createAdCampaign(name: String, pageName: String, objective: String, budget: String, emoji: String, gradient: String, endsIn: String) {
        viewModelScope.launch {
            val campaign = CampaignEntity(
                id = "c_" + System.currentTimeMillis(),
                name = name,
                pageName = pageName,
                objective = objective,
                status = "active",
                budget = budget,
                spent = "₹0",
                reach = "0",
                clicks = "0",
                ctr = "0.0%",
                endsIn = endsIn,
                emoji = emoji,
                gradient = gradient
            )
            dao.insertCampaign(campaign)
        }
    }

    fun toggleCampaignStatus(campaignId: String, currentStatus: String) {
        viewModelScope.launch {
            val nextStatus = if (currentStatus == "active") "paused" else "active"
            dao.updateCampaignStatus(campaignId, nextStatus)
        }
    }

    fun raiseSafetyAlert(text: String, severity: String) {
        viewModelScope.launch {
            val user = _currentUser.value ?: return@launch
            val alert = AlertEntity(
                id = "alert_" + System.currentTimeMillis(),
                name = user.name,
                time = "Just now",
                text = text,
                severity = severity
            )
            dao.insertAlert(alert)
        }
    }

    // ==========================================
    // GEMINI REAL-TIME SERVICE
    // ==========================================

    fun queryCircleGenie(query: String) {
        if (query.trim().isEmpty()) return
        _genieLoading.value = true
        _genieAnswer.value = null

        viewModelScope.launch {
            val answer = CircleGenieEngine.askGenie(query)
            _genieAnswer.value = answer
            _genieLoading.value = false
        }
    }

    fun clearGenieState() {
        _genieAnswer.value = null
        _genieLoading.value = false
    }
}
