package com.packages.speedmath

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


var popupType: Int? = null
var scoreLevel: Int? = null

var finishedLayout: RelativeLayout? = null
var timeUpLayout: RelativeLayout? = null

var currentTime: TextView? = null
var bestTime: TextView? = null

var scoreMinutes: String? = null
var scoreSeconds: String? = null
var scoreMilliSeconds: String? = null

var timeFormat : String? = null

class ScoreDisplayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_display)

        finishedLayout = findViewById(R.id.finished)
        timeUpLayout = findViewById(R.id.time_up)
        currentTime = findViewById(R.id.current_time)
        bestTime = findViewById(R.id.best_time)

        popupType = intent?.getIntExtra("popupType", 0)
        scoreLevel = intent?.getIntExtra("level", 0)

        when (popupType) {
            0 -> {
                finishedLayout?.visibility = View.GONE
                timeUpLayout?.visibility = View.VISIBLE
            }
            1 -> {
                finishedLayout?.visibility = View.VISIBLE
                timeUpLayout?.visibility = View.GONE
                setupScore()
            }
            2 -> {
                this.finish()
            }
        }

    }

    private fun setupScore() {

        val prefs = getSharedPreferences("Scores", Context.MODE_PRIVATE)

        when (scoreLevel) {
            1 -> {
                val bestScoreCount = prefs.getInt("beginner_best_score", 0)
                scoreMinutes =
                    String.format(
                        "%02d",
                        bestScoreCount.div(600)
                    )
                scoreSeconds =
                    String.format(
                        "%02d",
                        bestScoreCount.rem(600).div(10)
                    )
                scoreMilliSeconds =
                    String.format(
                        "%01d",
                        bestScoreCount.rem(10)
                    )
            }
            2 -> {
                val bestScoreCount = prefs.getInt("intermediate_best_score", 0)
                scoreMinutes =
                    String.format(
                        "%02d",
                        bestScoreCount.div(600)
                    )
                scoreSeconds =
                    String.format(
                        "%02d",
                        bestScoreCount.rem(600).div(10)
                    )
                scoreMilliSeconds =
                    String.format(
                        "%01d",
                        bestScoreCount.rem(10)
                    )
            }
            else -> {
                val bestScoreCount = prefs.getInt("expert_best_score", 0)
                scoreMinutes =
                    String.format(
                        "%02d",
                        bestScoreCount.div(600)
                    )
                scoreSeconds =
                    String.format(
                        "%02d",
                        bestScoreCount.rem(600).div(10)
                    )
                scoreMilliSeconds =
                    String.format(
                        "%01d",
                        bestScoreCount.rem(10)
                    )
            }
        }

        timeFormat = "$scoreMinutes:$scoreSeconds.$scoreMilliSeconds"
        bestTime?.text = timeFormat

        val currentScoreCount = prefs.getInt("current_score", 0)

        scoreMinutes = String.format("%02d", currentScoreCount.div(600))
        scoreSeconds = String.format("%02d", currentScoreCount.rem(600).div(10))
        scoreMilliSeconds = String.format("%01d", currentScoreCount.rem(10))

        timeFormat = "$scoreMinutes:$scoreSeconds.$scoreMilliSeconds"
        currentTime?.text = timeFormat
    }

}