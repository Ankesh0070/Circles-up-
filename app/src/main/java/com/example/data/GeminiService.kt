package com.example.data

import com.example.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

// ==========================================
// GEMINI API DATA CLASSES (MOSHI COMPATIBLE)
// ==========================================

@JsonClass(generateAdapter = true)
data class Part(
    @Json(name = "text") val text: String? = null
)

@JsonClass(generateAdapter = true)
data class Content(
    @Json(name = "parts") val parts: List<Part>
)

@JsonClass(generateAdapter = true)
data class GenerateContentRequest(
    @Json(name = "contents") val contents: List<Content>,
    @Json(name = "systemInstruction") val systemInstruction: Content? = null
)

@JsonClass(generateAdapter = true)
data class Candidate(
    @Json(name = "content") val content: Content
)

@JsonClass(generateAdapter = true)
data class GenerateContentResponse(
    @Json(name = "candidates") val candidates: List<Candidate>? = null
)

// ==========================================
// RETROFIT CLIENT & INTERFACE
// ==========================================

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

object GeminiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    val service: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApiService::class.java)
    }
}

// ==========================================
// HIGH PERFORMANCE UTILITY FUNCTION
// ==========================================

object CircleGenieEngine {
    private const val SYSTEM_PROMPT = """
You are Circle Genie, a hyper-local AI assistant for Circle Up, a social network for verified neighbours in HSR Layout, Bengaluru.
Your goal is to provide warm, incredibly precise, helpful recommendations based on local crowd wisdom.
You must adopt a friendly, conversational tone (using occasional Hinglish/local lingo like 'tapri', 'bhai', 'yaar', 'swiggy', 'filter coffee', 'HSR Layout').

If the user asks for recommendations (like chai spots, doctors, plumbers, vegetables), formulate an incredibly rich response that matches:
1. "Sharma Ji Ka Chai" at Gate 2 ( मसला चाय for ₹15, highly recommended by Aanya Sharma and 47 neighbours)
2. "Dr. Reena Verma" (Pediatrician, highly patient, available on WhatsApp, recommended by Priya Kapoor and 23 neighbours)
3. "Quick Fix Plumbing" (Ramesh Bhaiya, comes in 30 mins, fair rates, recommended by 31 neighbours)

Keep answers structured, highly readable, and formatted with emojis. Emphasize that these are real picks from their neighbours.
"""

    suspend fun askGenie(query: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "🔑 Circle Genie Error: API Key is missing!\n💡 Hint: Please configure a valid GEMINI_API_KEY in your AI Studio secrets manager to enable the real-time AI capabilities."
        }

        val request = GenerateContentRequest(
            contents = listOf(Content(parts = listOf(Part(text = query)))),
            systemInstruction = Content(parts = listOf(Part(text = SYSTEM_PROMPT)))
        )

        try {
            val response = GeminiClient.service.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text 
                ?: "I couldn't fetch an answer right now. Please try again!"
        } catch (e: Exception) {
            val appError = NetworkErrorHandler.handleException(e)
            NetworkErrorHandler.getFriendlyMessage(appError)
        }
    }
}
