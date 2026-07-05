package com.example.data

import android.util.Log
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class AppError {
    data class NetworkError(val message: String, val suggestion: String) : AppError()
    data class ApiError(val code: Int, val message: String, val suggestion: String) : AppError()
    data class KeyValidationError(val message: String, val suggestion: String) : AppError()
    data class UnknownError(val message: String) : AppError()
}

object NetworkErrorHandler {
    private const val TAG = "NetworkErrorHandler"

    fun handleException(throwable: Throwable): AppError {
        Log.e(TAG, "Error caught in centralized utility", throwable)
        return when (throwable) {
            is UnknownHostException -> AppError.NetworkError(
                message = "Unable to connect to the server.",
                suggestion = "Please check your network settings. Ensure your cellular data or Wi-Fi is active and try again."
            )
            is SocketTimeoutException -> AppError.NetworkError(
                message = "The connection timed out.",
                suggestion = "The server is taking too long to respond. Please check your connection speed or try again in a few seconds."
            )
            is IOException -> AppError.NetworkError(
                message = "A network error occurred.",
                suggestion = "Please verify your internet connection and try again."
            )
            is HttpException -> {
                val code = throwable.code()
                val message = throwable.message()
                val suggestion = when (code) {
                    400 -> "Bad request. Please check if the query content is supported."
                    401 -> "Invalid credentials. Please verify your Gemini API Key in the AI Studio Secrets panel."
                    403 -> "Access forbidden. Your API key might be blocked, rate-limited, or unauthorized for this model."
                    429 -> "Rate limit exceeded. Too many requests have been made in a short period. Please try again after a few minutes."
                    in 500..599 -> "Gemini API is temporarily down. Google servers are experiencing issues. Please try again shortly."
                    else -> "An unexpected API error occurred (HTTP $code). Please try again."
                }
                AppError.ApiError(code, message, suggestion)
            }
            else -> AppError.UnknownError(throwable.localizedMessage ?: "An unexpected error occurred.")
        }
    }

    fun getFriendlyMessage(error: AppError): String {
        return when (error) {
            is AppError.NetworkError -> "🌐 Connection Issue: ${error.message}\n💡 Hint: ${error.suggestion}"
            is AppError.ApiError -> "🤖 Gemini Service Error (HTTP ${error.code}): ${error.message}\n💡 Hint: ${error.suggestion}"
            is AppError.KeyValidationError -> "🔑 Key Error: ${error.message}\n💡 Hint: ${error.suggestion}"
            is AppError.UnknownError -> "⚠️ Unexpected Error: ${error.message}\n💡 Tip: Please restart the app or try again."
        }
    }
}
