package com.shubham.worldt2.views

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubham.worldt2.R
import com.shubham.worldt2.databinding.FragmentTeamListBinding
import com.shubham.worldt2.model.Team
import com.shubham.worldt2.viewModel.TeamListViewModel

class TeamListFragment : Fragment() {


    private val viewModel: TeamListViewModel by viewModels()
    private lateinit var binding: FragmentTeamListBinding
    private lateinit var adapter: TeamAdapter
    private lateinit var selectedTeams: List<Team>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeamListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initObservers()
        initListener()
    }

    private fun initListener()  {
        binding.btnStartGame.setOnClickListener {
            if (this::selectedTeams.isInitialized && selectedTeams.size == 2) {
                val bundle = Bundle().apply {
                    putString("TEAM1", selectedTeams[0].name)
                    putString("TEAM2", selectedTeams[1].name)
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, MatchScreenFragment::class.java, bundle)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    private fun initAdapter() {
        adapter = TeamAdapter{ selected ->
            selectedTeams = selected
            updateButtonState()
        }
        binding.rvTeams.adapter = adapter
    }

    private fun initObservers() {
        viewModel.teams.observe(viewLifecycleOwner) { teams ->
            Log.d("TeamListFragment", "Teams: $teams")
            adapter.submitList(teams)
        }
    }

    private fun updateButtonState() {
        if (selectedTeams.size == 2) {
            binding.btnStartGame.isEnabled = true
            binding.btnStartGame.setBackgroundColor(binding.root.context.getColor(R.color.green))
        } else {
            binding.btnStartGame.isEnabled = false
            binding.btnStartGame.setBackgroundColor(binding.root.context.getColor(R.color.white))
        }
    }

}