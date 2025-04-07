package com.shubham.worldt2.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shubham.worldt2.model.Team
import com.shubham.worldt2.model.TeamRepository

class TeamListViewModel(application: Application) : AndroidViewModel(application) {
        private val repository = TeamRepository(application)

        private val _teams = MutableLiveData<List<Team>>()
        val teams: LiveData<List<Team>> get() = _teams

        init {
            loadTeams()
        }

        private fun loadTeams() {
            _teams.value = repository.getTeamsFromJson()
        }
    }