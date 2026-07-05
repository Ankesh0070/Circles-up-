package com.example.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow

// ==========================================
// TYPE CONVERTERS
// ==========================================
class Converters {
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val stringListType = Types.newParameterizedType(List::class.java, String::class.java)
    private val adapter = moshi.adapter<List<String>>(stringListType)

    @TypeConverter
    fun fromString(value: String?): List<String>? {
        if (value == null) return null
        return try {
            adapter.fromJson(value)
        } catch (e: Exception) {
            value.split(",")
        }
    }

    @TypeConverter
    fun fromList(list: List<String>?): String? {
        if (list == null) return null
        return try {
            adapter.toJson(list)
        } catch (e: Exception) {
            list.joinToString(",")
        }
    }
}

// ==========================================
// ROOM ENTITIES
// ==========================================

@Entity(tableName = "profiles")
data class Profile(
    @PrimaryKey val username: String,
    val name: String,
    val bio: String,
    val avatar: String, // Gradient color string or path
    val postsCount: Int,
    val followers: String,
    val following: Int,
    val isVerified: Boolean,
    val society: String,
    val tower: String,
    val flat: String,
    val profession: String,
    val distance: String,
    val mutuals: Int,
    @TypeConverters(Converters::class) val vibes: List<String>
)

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: String,
    val name: String,
    val handle: String,
    val time: String,
    val category: String,
    val categoryColor: String,
    val caption: String,
    val imageEmoji: String?,
    val imageGradient: String?,
    val likes: Int,
    val commentsCount: Int,
    val hasStory: Boolean = false,
    val isYou: Boolean = false,
    val reaction: String? = null // null, 'like', 'notice', 'diss', 'engaged', 'out'
)

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val postId: String,
    val name: String,
    val text: String,
    val time: String,
    val likes: Int,
    val liked: Boolean = false,
    val isOp: Boolean = false
)

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val lastMessage: String,
    val time: String,
    val unread: Int,
    val isGroup: Boolean,
    val emoji: String // Group icon emoji
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val chatId: Int,
    val fromMe: Boolean,
    val text: String,
    val time: String,
    val isMedia: Boolean = false,
    val mediaType: String? = null
)

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey val id: String,
    val title: String,
    val host: String,
    val whenTime: String,
    val location: String,
    val emoji: String,
    val gradient: String,
    val goingCount: Int,
    val weekOnly: Boolean,
    val privacy: String, // 'neighbours' | 'close-friends' | 'open'
    val description: String,
    val rsvpStatus: String? = null // null, 'going', 'maybe'
)

@Entity(tableName = "pages")
data class PageEntity(
    @PrimaryKey val id: String,
    val name: String,
    val handle: String,
    val category: String,
    val emoji: String,
    val followers: Int,
    val postsCount: Int,
    val verified: Boolean,
    val isNgo: Boolean,
    val description: String,
    val tagline: String,
    val website: String = "",
    val phone: String = "",
    val location: String = "",
    val extraValue: String = ""
)

@Entity(tableName = "campaigns")
data class CampaignEntity(
    @PrimaryKey val id: String,
    val name: String,
    val pageName: String,
    val objective: String,
    val status: String, // 'active', 'paused', 'ended'
    val budget: String,
    val spent: String,
    val reach: String,
    val clicks: String,
    val ctr: String,
    val endsIn: String,
    val emoji: String,
    val gradient: String
)

@Entity(tableName = "alerts")
data class AlertEntity(
    @PrimaryKey val id: String,
    val name: String,
    val time: String,
    val text: String,
    val severity: String // 'high', 'medium', 'low'
)

// ==========================================
// DATA ACCESS OBJECTS (DAOs)
// ==========================================

@Dao
interface CircleUpDao {
    // Profiles
    @Query("SELECT * FROM profiles")
    fun getAllProfiles(): Flow<List<Profile>>

    @Query("SELECT * FROM profiles WHERE username = :username LIMIT 1")
    suspend fun getProfileByUsername(username: String): Profile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: Profile)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfiles(profiles: List<Profile>)

    // Posts
    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    @Query("UPDATE posts SET likes = :likes, reaction = :reaction WHERE id = :postId")
    suspend fun updatePostReaction(postId: String, likes: Int, reaction: String?)

    // Comments
    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY id ASC")
    fun getCommentsForPost(postId: String): Flow<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComments(comments: List<CommentEntity>)

    @Query("UPDATE comments SET likes = :likes, liked = :liked WHERE id = :commentId")
    suspend fun updateCommentLike(commentId: Int, likes: Int, liked: Boolean)

    // Chats
    @Query("SELECT * FROM chats ORDER BY time DESC")
    fun getAllChats(): Flow<List<ChatEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: ChatEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChats(chats: List<ChatEntity>)

    @Query("UPDATE chats SET lastMessage = :lastMessage, time = :time, unread = :unread WHERE id = :chatId")
    suspend fun updateChatState(chatId: Int, lastMessage: String, time: String, unread: Int)

    // Messages
    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY id ASC")
    fun getMessagesForChat(chatId: Int): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    // Events
    @Query("SELECT * FROM events ORDER BY id DESC")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Query("UPDATE events SET rsvpStatus = :status, goingCount = :goingCount WHERE id = :eventId")
    suspend fun updateEventRsvp(eventId: String, status: String?, goingCount: Int)

    // Pages
    @Query("SELECT * FROM pages ORDER BY id DESC")
    fun getAllPages(): Flow<List<PageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPage(page: PageEntity)

    // Campaigns
    @Query("SELECT * FROM campaigns ORDER BY id DESC")
    fun getAllCampaigns(): Flow<List<CampaignEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampaign(campaign: CampaignEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampaigns(campaigns: List<CampaignEntity>)

    @Query("UPDATE campaigns SET status = :status WHERE id = :campaignId")
    suspend fun updateCampaignStatus(campaignId: String, status: String)

    // Alerts
    @Query("SELECT * FROM alerts ORDER BY time DESC")
    fun getAllAlerts(): Flow<List<AlertEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: AlertEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlerts(alerts: List<AlertEntity>)
}

// ==========================================
// ROOM DATABASE
// ==========================================

@Database(
    entities = [
        Profile::class,
        PostEntity::class,
        CommentEntity::class,
        ChatEntity::class,
        MessageEntity::class,
        EventEntity::class,
        PageEntity::class,
        CampaignEntity::class,
        AlertEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CircleUpDatabase : RoomDatabase() {
    abstract fun dao(): CircleUpDao

    companion object {
        @Volatile
        private var INSTANCE: CircleUpDatabase? = null

        fun getDatabase(context: Context): CircleUpDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CircleUpDatabase::class.java,
                    "circleup_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
