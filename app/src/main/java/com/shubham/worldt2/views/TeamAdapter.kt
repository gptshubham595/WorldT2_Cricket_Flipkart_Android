package com.shubham.worldt2.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shubham.worldt2.databinding.ItemTeamBinding
import com.shubham.worldt2.model.Team

class TeamAdapter(
    private val onSelectionChanged: (List<Team>) -> Unit
) : RecyclerView.Adapter<TeamAdapter.TeamViewHolder>() {

    private val selectedTeams = mutableListOf<Team>()
    private var teams = mutableListOf<Team>()

    fun submitList(teams: List<Team>) {
        this.teams = teams.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val binding = ItemTeamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeamViewHolder(binding)
    }

    override fun getItemCount(): Int = teams.size

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.bind(teams[position])
    }

    inner class TeamViewHolder(private val binding: ItemTeamBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(team: Team) {
            binding.tvTeamName.text = team.name
            // create uri from the URL string
            Glide.with(binding.root.context).load(team.flag.toUri())
                .into(binding.imgFlag)
            if (selectedTeams.contains(team)) {
                binding.itemContainer.setBackgroundColor(binding.root.context.getColor(com.shubham.worldt2.R.color.green))
            } else {
                binding.itemContainer.setBackgroundColor(binding.root.context.getColor(com.shubham.worldt2.R.color.white))
            }

            binding.root.setOnClickListener {
                if (selectedTeams.contains(team)) {
                    selectedTeams.remove(team)
                } else {
                    if (selectedTeams.size < 2) {
                        selectedTeams.add(team)
                    }
                }
                notifyDataSetChanged()
                onSelectionChanged(selectedTeams)
            }
        }
    }
}
