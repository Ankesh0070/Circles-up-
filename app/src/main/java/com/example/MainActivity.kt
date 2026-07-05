package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.data.*
import com.example.ui.*
import com.example.ui.theme.*
import com.example.viewmodel.CircleUpViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppNavigation()
            }
        }
    }
}

@Composable
fun MainAppNavigation() {
    val navController = rememberNavController()
    val viewModel: CircleUpViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        // ==========================================
        // ONBOARDING
        // ==========================================
        composable("splash") {
            SplashScreen {
                navController.navigate("phone") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
        composable("phone") {
            PhoneScreen(
                onContinue = { emailOrPhone ->
                    navController.navigate("main") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onSignup = { navController.navigate("signup") }
            )
        }
        composable("signup") {
            SignupScreen(
                onContinue = { navController.navigate("otp") },
                onBack = { navController.popBackStack() }
            )
        }
        composable("otp") {
            OtpScreen(
                onContinue = { navController.navigate("address_setup") },
                onBack = { navController.popBackStack() }
            )
        }
        composable("address_setup") {
            AddressScreen(
                viewModel = viewModel,
                onContinue = { navController.navigate("profile_setup") },
                onBack = { navController.popBackStack() }
            )
        }
        composable("profile_setup") {
            ProfileSetupScreen(
                viewModel = viewModel,
                onComplete = {
                    navController.navigate("main") {
                        popUpTo("phone") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ==========================================
        // MAIN APPLICATION TABS CONTAINER
        // ==========================================
        composable("main") {
            MainTabsContainer(
                viewModel = viewModel,
                onNavigateToPostDetail = { post ->
                    navController.navigate("post_detail/${post.id}")
                },
                onNavigateToProfileDetail = { profileName ->
                    // Profiles click matches showing profile tab or similar
                    navController.navigate("main?tab=profile")
                },
                onNavigateToChatDetail = { chat ->
                    navController.navigate("chat_detail/${chat.id}")
                },
                onSosClick = {
                    navController.navigate("guard")
                },
                onNotifClick = {
                    navController.navigate("achievements")
                },
                onCreatePostClick = {
                    navController.navigate("create_post")
                },
                onGenieClick = {
                    navController.navigate("genie")
                },
                onGuardClick = {
                    navController.navigate("guard")
                },
                onBazaarClick = {
                    navController.navigate("bazaar")
                },
                onScenesClick = {
                    navController.navigate("scenes")
                },
                onAchievementsClick = {
                    navController.navigate("achievements")
                },
                onSettingsClick = {
                    navController.navigate("settings")
                }
            )
        }

        // ==========================================
        // POST DETAILS
        // ==========================================
        composable("create_post") {
            CreatePostScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "post_detail/{postId}",
            arguments = listOf(navArgument("postId") { type = NavType.StringType })
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: ""
            val postsList by viewModel.posts.collectAsState()
            val post = postsList.find { it.id == postId }
            if (post != null) {
                PostDetailScreen(
                    post = post,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onProfileClick = { name ->
                        navController.navigate("main")
                    }
                )
            }
        }

        // ==========================================
        // CHAT DETAILS
        // ==========================================
        composable(
            route = "chat_detail/{chatId}",
            arguments = listOf(navArgument("chatId") { type = NavType.IntType })
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getInt("chatId") ?: 0
            val chatsList by viewModel.chats.collectAsState()
            val chat = chatsList.find { it.id == chatId }
            if (chat != null) {
                ChatDetailScreen(
                    chat = chat,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onProfileClick = { name ->
                        navController.navigate("main")
                    }
                )
            }
        }

        // ==========================================
        // CIRCLE GUARD UTILITIES
        // ==========================================
        composable("guard") {
            GuardScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onFakeCallClick = { navController.navigate("fake_call") },
                onSilentPhraseClick = { navController.navigate("silent_phrase") },
                onShareLocationClick = { navController.navigate("share_live_location") },
                onTrustedContactsClick = { navController.navigate("trusted_contacts") }
            )
        }
        composable("fake_call") {
            FakeCallScreen(onBack = { navController.popBackStack() })
        }
        composable("silent_phrase") {
            SilentPhraseScreen(onBack = { navController.popBackStack() })
        }
        composable("share_live_location") {
            ShareLocationScreen(onBack = { navController.popBackStack() })
        }
        composable("trusted_contacts") {
            TrustedContactsScreen(onBack = { navController.popBackStack() })
        }

        // ==========================================
        // CIRCLE GENIE & EXPLORE
        // ==========================================
        composable("genie") {
            GenieScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable("bazaar") {
            BazaarScreen(
                onBack = { navController.popBackStack() },
                onProfileClick = { name -> navController.navigate("main") }
            )
        }
        composable("scenes") {
            ScenesScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onCreateEvent = { navController.navigate("create_event") }
            )
        }
        composable("create_event") {
            CreateEventScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // ==========================================
        // SETTINGS & PAGES
        // ==========================================
        composable("settings") {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onPageCreatorClick = { navController.navigate("create_page") },
                onAdsManagerClick = { navController.navigate("ads_manager") },
                onLogoutClick = {
                    navController.navigate("phone") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable("create_page") {
            CreatePageScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable("ads_manager") {
            AdsManagerScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onCreateAd = { navController.navigate("create_ad") }
            )
        }
        composable("create_ad") {
            CreateAdScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable("edit_profile") {
            EditProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable("achievements") {
            AchievementsScreen(onBack = { navController.popBackStack() })
        }
    }
}

// ==========================================
// TABS CONTAINER COMPOSE
// ==========================================

@Composable
fun MainTabsContainer(
    viewModel: CircleUpViewModel,
    onNavigateToPostDetail: (PostEntity) -> Unit,
    onNavigateToProfileDetail: (String) -> Unit,
    onNavigateToChatDetail: (ChatEntity) -> Unit,
    onSosClick: () -> Unit,
    onNotifClick: () -> Unit,
    onCreatePostClick: () -> Unit,
    onGenieClick: () -> Unit,
    onGuardClick: () -> Unit,
    onBazaarClick: () -> Unit,
    onScenesClick: () -> Unit,
    onAchievementsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("home") }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                val items = listOf(
                    Triple("home", "Home", Icons.Default.Home),
                    Triple("explore", "Explore", Icons.Default.Search),
                    Triple("guard", "Guard", Icons.Default.Security),
                    Triple("chats", "Chats", Icons.Default.Chat),
                    Triple("profile", "Profile", Icons.Default.Person)
                )

                for (item in items) {
                    val active = selectedTab == item.first
                    NavigationBarItem(
                        selected = active,
                        onClick = { selectedTab = item.first },
                        icon = {
                            Icon(
                                imageVector = item.third,
                                contentDescription = item.second,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = { Text(text = item.second, fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BrandPrimary,
                            selectedTextColor = BrandPrimary,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (selectedTab) {
                "home" -> FeedTimeline(
                    viewModel = viewModel,
                    onSosClick = onSosClick,
                    onNotifClick = onNotifClick,
                    onNeighbourhoodClick = {},
                    onCreatePostClick = onCreatePostClick,
                    onOpenPost = onNavigateToPostDetail,
                    onProfileClick = { selectedTab = "profile" }
                )
                "explore" -> ExploreScreen(
                    viewModel = viewModel,
                    onGenieClick = onGenieClick,
                    onGuardClick = onGuardClick,
                    onBazaarClick = onBazaarClick,
                    onScenesClick = onScenesClick,
                    onProfileClick = { selectedTab = "profile" },
                    onTopicClick = {}
                )
                "guard" -> GuardScreen(
                    viewModel = viewModel,
                    onBack = { selectedTab = "home" },
                    onFakeCallClick = onGuardClick,
                    onSilentPhraseClick = {},
                    onShareLocationClick = {},
                    onTrustedContactsClick = {}
                )
                "chats" -> ChatsTimeline(
                    viewModel = viewModel,
                    onChatClick = onNavigateToChatDetail,
                    onNewChatClick = {}
                )
                "profile" -> ProfileScreen(
                    viewModel = viewModel,
                    onEditProfileClick = {},
                    onSettingsClick = onSettingsClick,
                    onAchievementsClick = onAchievementsClick,
                    onOpenPost = onNavigateToPostDetail
                )
            }
        }
    }
}
