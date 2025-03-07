package com.example.learningapp

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

suspend fun fetchWordDetails(wordsList: List<String>): List<Pair<String, String?>> {
    val client = OkHttpClient()

    return withContext(Dispatchers.IO) {
        val wordDetailsList = mutableListOf<Pair<String, String?>>()

        for (word in wordsList) {
            try {
                // Datamuse API URL to fetch word details
                val url = "https://api.datamuse.com/words?sp=$word&max=1"

                val request = Request.Builder()
                    .url(url)
                    .addHeader("Accept", "application/json")
                    .build()

                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val jsonArray = JSONArray(responseBody)

                    Log.d("API_JSON_STRUCTURE", "Response for $word: $jsonArray")

                    if (jsonArray.length() > 0) {
                        // Fetch the word details
                        val wordData = jsonArray.getJSONObject(0)

                        // Get word properties (e.g., word and score)
                        val wordText = wordData.getString("word")
                        val image = fetchImage(word)  // Fetch image based on the word

                        // Add to the list
                        wordDetailsList.add(Pair(wordText, image))
                    }
                } else {
                    Log.e("API_ERROR", "Error fetching details for word: $word. Response: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error fetching details for word: $word", e)
            }
        }
        return@withContext wordDetailsList
    }
}

suspend fun fetchImage(query: String): String? {
    // Fetch image from Unsplash or Pexels (optional, if needed)
    val apiKey = Config.PEXELS_API_KEY
    return withContext(Dispatchers.IO) {
        try {
            val url = "https://api.pexels.com/v1/search?query=$query&per_page=1"
            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", apiKey)
                .build()

            val client = OkHttpClient()
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            val jsonObject = JSONObject(responseBody)
            val photosArray = jsonObject.getJSONArray("photos")

            if (photosArray.length() > 0) {
                // Return the image URL of the first photo
                val photoObject = photosArray.getJSONObject(0)
                photoObject.getJSONObject("src").getString("medium")
            } else null
        } catch (e: Exception) {
            null
        }
    }
}
