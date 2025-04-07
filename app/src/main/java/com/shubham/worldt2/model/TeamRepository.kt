package com.shubham.worldt2.model

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TeamRepository(private val context: Context) {

    fun getTeamsFromJson(): List<Team> {
        val inputStream = context.assets.open("teams.json")
        val json = inputStream.bufferedReader().use { it.readText() }
        return Gson().fromJson(json, object : TypeToken<List<Team>>() {}.type)
    }
}
