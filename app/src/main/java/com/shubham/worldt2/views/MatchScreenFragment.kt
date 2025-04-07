package com.shubham.worldt2.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shubham.worldt2.databinding.FragmentMatchScreenBinding
import kotlin.random.Random

class MatchScreenFragment : Fragment() {

    private var _binding: FragmentMatchScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var team1: String
    private lateinit var team2: String


    private var currentInnings = 1
    private var team1Score = 0
    private var team1Wickets = 0
    private var team1Overs = 0.0f
    private var team2Score = 0
    private var team2Wickets = 0
    private var team2Overs = 0.0f
    private var isMatchOver = false


    private val outcomes = mapOf(
        "0" to 30,      // Dot ball (no run)
        "1" to 25,      // One run
        "2" to 15,      // Two runs
        "3" to 10,      // Three runs
        "4" to 10,      // Four runs
        "6" to 5,       // Six runs
        "Out" to 5      // Wicket
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            team1 = it.getString("TEAM1", "Team 1")
            team2 = it.getString("TEAM2", "Team 2")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchScreenBinding.inflate(inflater, container, false)

        updateMatchDisplay()


        binding.nextBallBtn.setOnClickListener {
            if (!isMatchOver) {
                playNextBall()
            } else {

                parentFragmentManager.popBackStack()
            }
        }

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

    private fun playNextBall() {
        val outcome = getRandomOutcome()
        binding.outText.text = outcome


        when {
            outcome == "Out" -> {
                if (currentInnings == 1) {
                    team1Wickets++
                } else {
                    team2Wickets++
                }
            }

            outcome == "Wide" -> {
                if (currentInnings == 1) {
                    team1Score++
                } else {
                    team2Score++
                }
                updateMatchDisplay()
                return
            }

            outcome == "No ball" -> {
                if (currentInnings == 1) {
                    team1Score++
                } else {
                    team2Score++
                }
                updateMatchDisplay()
                return
            }

            else -> {
                val runs = outcome.toInt()
                if (currentInnings == 1) {
                    team1Score += runs
                } else {
                    team2Score += runs
                }
            }
        }


        if (currentInnings == 1) {

            val currentBall = ((team1Overs * 10) % 10).toInt() + 1
            if (currentBall >= 6) {
                team1Overs = (team1Overs.toInt() + 1).toFloat()
            } else {
                team1Overs = team1Overs.toInt() + currentBall / 10.0f
            }
        } else {
            val currentBall = ((team2Overs * 10) % 10).toInt() + 1
            if (currentBall >= 6) {
                team2Overs = (team2Overs.toInt() + 1).toFloat()
            } else {
                team2Overs = team2Overs.toInt() + currentBall / 10.0f
            }
        }


        checkInningsStatus()


        updateMatchDisplay()
    }

    private fun getRandomOutcome(): String {

        val totalProbability = outcomes.values.sum()


        val random = Random.nextInt(totalProbability)


        var cumulativeProbability = 0
        for ((outcome, probability) in outcomes) {
            cumulativeProbability += probability
            if (random < cumulativeProbability) {
                val extraCheck = Random.nextInt(100)
                if (extraCheck < 10 && outcome != "Out") {
                    return "Wide"
                } else if (extraCheck < 20 && outcome != "Out") {
                    return "No ball"
                }
                return outcome
            }
        }

        return "0"
    }

    private fun checkInningsStatus() {
        if (currentInnings == 1 && (team1Wickets >= 3 || team1Overs >= 2.0f)) {
            currentInnings = 2
            return
        }


        if (currentInnings == 2) {

            if (team2Wickets >= 3) {
                declareWinner()
                return
            }


            if (team2Overs >= 2.0f) {
                declareWinner()
                return
            }

            if (team2Score > team1Score) {
                declareWinner()
                return
            }
        }
    }

    private fun declareWinner() {
        isMatchOver = true

        if (team2Score > team1Score) {
            binding.outText.text = "$team2 Wins"
        } else if (team1Score > team2Score) {
            binding.outText.text = "$team1 Wins"
        } else {
            binding.outText.text = "Match Tied"
        }

        binding.nextBallBtn.text = "Match Over"
    }

    private fun updateMatchDisplay() {
        if (currentInnings == 1) {
            binding.battingTeam.text = "$team1 (Batting)"
            binding.bowlingTeam.text = "$team2 (Bowling)"
            binding.scoreText.text = "Score: $team1Score/$team1Wickets"
            binding.oversText.text = "Overs: ${formatOvers(team1Overs)}"
            binding.yetToBatLeft.text = "yet to bat"
            binding.yetToBatRight.text = "yet to bat"
        } else {
            binding.battingTeam.text = "$team2 (Batting)"
            binding.bowlingTeam.text = "$team1 (Bowling)"
            binding.scoreText.text = "Score: $team2Score/$team2Wickets"
            binding.oversText.text = "Overs: ${formatOvers(team2Overs)}"
            binding.yetToBatLeft.text = " Score: $team1Score/$team1Wickets"
            binding.yetToBatRight.text = "Overs: ${formatOvers(team1Overs)}"
        }
    }

    private fun formatOvers(overs: Float): String {
        val fullOvers = overs.toInt()
        val balls = ((overs - fullOvers) * 10).toInt()
        return "$fullOvers.$balls"
    }

    companion object {
        @JvmStatic
        fun newInstance(team1: String, team2: String) = MatchScreenFragment().apply {
            arguments = Bundle().apply {
                putString("TEAM1", team1)
                putString("TEAM2", team2)
            }
        }
    }
}