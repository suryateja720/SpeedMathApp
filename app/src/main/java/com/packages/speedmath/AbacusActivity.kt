package com.packages.speedmath

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

var num1: Button? = null
var num2: Button? = null
var num3: Button? = null
var num4: Button? = null
var num5: Button? = null
var num6: Button? = null
var num7: Button? = null
var num8: Button? = null
var num9: Button? = null
var num0: Button? = null
var backspace: Button? = null
var all_clear: Button? = null
var question_counter_display: TextView? = null
var timer_display: TextView? = null
var level_display: TextView? = null
var answer_display: TextView? = null
var question_display: TextView? = null
var close_button: ImageView? = null
var abacus_layout: RelativeLayout? = null
var question_counter: Int = 0
var timer_counter: Int? = 0
var counter_limit: Int = 0
var timer_limit: Int = 0
var minutes: String? = null
var seconds: String? = null
var milliSeconds: String? = null
var level: Int? = null
var finished: Int? = null
var answer: Int? = null

@Suppress("DEPRECATION")
class AbacusActivity : AppCompatActivity() {
    override fun onDestroy() {
        super.onDestroy()
        timer_counter = timer_limit + 1
        finished = 2
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        level = intent.getIntExtra("level", 0)
        when (level) {
            1 -> {
                setTheme(R.style.BeginnerTheme)
            }
            2 -> {
                setTheme(R.style.IntermediateTheme)
            }
            3 -> {
                setTheme(R.style.ExpertTheme)
            }
        }
        setContentView(R.layout.activity_abacus)
//...........................Finding the Buttons & Widgets.........................................
        num1 = findViewById(R.id.n1)
        num2 = findViewById(R.id.n2)
        num3 = findViewById(R.id.n3)
        num4 = findViewById(R.id.n4)
        num5 = findViewById(R.id.n5)
        num6 = findViewById(R.id.n6)
        num7 = findViewById(R.id.n7)
        num8 = findViewById(R.id.n8)
        num9 = findViewById(R.id.n9)
        num0 = findViewById(R.id.n0)
        backspace = findViewById(R.id.backspace)
        all_clear = findViewById(R.id.all_clear)
        close_button = findViewById(R.id.close_button)
        abacus_layout = findViewById(R.id.abacus_relative_layout)
        question_display = findViewById(R.id.question_display)
        answer_display = findViewById(R.id.answer_display)
        question_counter_display = findViewById(R.id.counter)
        timer_display = findViewById(R.id.live_timer)
        level_display = findViewById(R.id.level)
//............................Setting up level requirements.........................................
        when (level) {
            1 -> {
                level_display?.text = "Beginner"
                abacus_layout?.setBackgroundColor(resources.getColor(R.color.colorBeginner))
                changeButtonColor(resources.getDrawable(R.drawable.rounded_button_beginner))
                counter_limit = 15
                timer_limit = 1200
            }
            2 -> {
                level_display?.text = "Intermediate"
                abacus_layout?.setBackgroundColor(resources.getColor(R.color.colorIntermediate))
                changeButtonColor(resources.getDrawable(R.drawable.rounded_button_intermediate))
                counter_limit = 20
                timer_limit = 2400
            }
            3 -> {
                level_display?.text = "Expert"
                abacus_layout?.setBackgroundColor(resources.getColor(R.color.colorExpert))
                changeButtonColor(resources.getDrawable(R.drawable.rounded_button_expert))
                counter_limit = 30
                timer_limit = 3000
            }
        }
//............................Setting up Button & Widget actions....................................
        num1?.setOnClickListener {
            answer_display?.text = answer_display?.text.toString().plus("1")
        }
        num2?.setOnClickListener {
            answer_display?.text = answer_display?.text.toString().plus("2")
        }
        num3?.setOnClickListener {
            answer_display?.text = answer_display?.text.toString().plus("3")
        }
        num4?.setOnClickListener {
            answer_display?.text = answer_display?.text.toString().plus("4")
        }
        num5?.setOnClickListener {
            answer_display?.text = answer_display?.text.toString().plus("5")
        }
        num6?.setOnClickListener {
            answer_display?.text = answer_display?.text.toString().plus("6")
        }
        num7?.setOnClickListener {
            answer_display?.text = answer_display?.text.toString().plus("7")
        }
        num8?.setOnClickListener {
            answer_display?.text = answer_display?.text.toString().plus("8")
        }
        num9?.setOnClickListener {
            answer_display?.text = answer_display?.text.toString().plus("9")
        }
        num0?.setOnClickListener {
            answer_display?.text = answer_display?.text.toString().plus("0")
        }
        backspace?.setOnClickListener {
            if (answer_display?.text != "") {
                answer_display?.text = answer_display?.text.toString()
                    .subSequence(0, answer_display?.text!!.length - 1)
            }
        }
        all_clear?.setOnClickListener {
            answer_display?.text = ""
        }
        close_button?.setOnClickListener {
            this.finish()
        }
//............................Initializing values & question........................................
        question_counter = 0
        timer_counter = 0
        newQuestion()
        timer()
//............................Setting up the text watcher...........................................
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (answer_display?.text != null
                    && answer_display?.text.toString().length == answer.toString().length
                ) {
                    if (answer_display?.text.toString() == answer?.toString()) {
                        Handler().postDelayed({
                            newQuestion()
                        }, 500)
                    } else {
                        Handler().postDelayed({
                            answer_display?.text = ""
                        }, 500)
                    }
                }
            }
        }
        answer_display?.addTextChangedListener(textWatcher)
    }

    //.....................................Functions....................................................
    private fun newQuestion() {
        question_counter++
        if (question_counter > counter_limit) {
            val pref = getSharedPreferences("Scores", Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putInt("current_score", timer_counter!!)
            when (level) {
                1 -> {
                    if (timer_counter!! < pref.getInt("beginner_best_score", 0)
                        || pref.getInt("beginner_best_score", 0) == 0
                    ) {
                        editor.putInt("beginner_best_score", timer_counter!!)
                    }
                }
                2 -> {
                    if (timer_counter!! < pref.getInt("intermediate_best_score", 0)
                        || pref.getInt("beginner_best_score", 0) == 0
                    ) {
                        editor.putInt("intermediate_best_score", timer_counter!!)
                    }
                }
                3 -> {
                    if (timer_counter!! < pref.getInt("expert_best_score", 0)
                        || pref.getInt("beginner_best_score", 0) == 0
                    ) {
                        editor.putInt("expert_best_score", timer_counter!!)
                    }
                }
            }
            editor.apply()
            finished = 1
            timer_counter = timer_limit + 1
        } else {
            finished = 0
            val questionCount = "$question_counter/$counter_limit"
            question_counter_display?.text = questionCount
            answer_display?.text = ""
            when (level) {
                1 -> {
                    beginnerQuestion()
                }
                2 -> {
                    intermediateQuestion()
                }
                3 -> {
                    expertQuestion()
                }
            }
        }
    }

    private fun beginnerQuestion() {
        val selector = Random.nextInt(0, 10)
        val operator =
            when {
                selector % 2 == 0 -> {
                    '+'
                }
                else -> {
                    '-'
                }
            }
        val operand1 = Random.nextInt(1, 50)
        val operand2 =
            when {
                selector % 2 == 0 -> {
                    Random.nextInt(1, 50)
                }
                else -> {
                    Random.nextInt(1, operand1)
                }
            }
        answer =
            when {
                selector % 2 == 0 -> {
                    operand1 + operand2
                }
                else -> {
                    operand1 - operand2
                }
            }
        val question = "$operand1  $operator  $operand2"
        question_display?.text = question
    }

    private fun intermediateQuestion() {
        val selector = Random.nextInt(0, 20)
        val operator =
            when {
                selector % 4 == 0 -> {
                    '+'
                }
                selector % 4 == 1 -> {
                    '-'
                }
                selector % 4 == 2 -> {
                    '*'
                }
                else -> {
                    '/'
                }
            }
        var operand1 = Random.nextInt(1, 50)
        val operand2: Int
        when {
            selector % 4 == 0 -> {
                operand2 = Random.nextInt(1, 50)
            }
            selector % 4 == 1 -> {
                operand2 = Random.nextInt(1, operand1)
            }
            selector % 4 == 2 -> {
                operand2 = Random.nextInt(1, 10)
            }
            else -> {
                operand2 = Random.nextInt(1, 10)
                operand1 -= operand1 % operand2
            }
        }
        answer =
            when {
                selector % 4 == 0 -> {
                    operand1 + operand2
                }
                selector % 4 == 1 -> {
                    operand1 - operand2
                }
                selector % 4 == 2 -> {
                    operand1 * operand2
                }
                else -> {
                    operand1 / operand2
                }
            }
        val question = "$operand1  $operator  $operand2"
        question_display?.text = question
    }

    private fun expertQuestion() {
        val selector1 = Random.nextInt(0, 30)
        val selector2 = Random.nextInt(0, 30)
        val operator1 =
            when {
                selector1 % 4 == 0 -> {
                    '+'
                }
                selector1 % 4 == 1 -> {
                    '-'
                }
                selector1 % 4 == 2 -> {
                    '*'
                }
                else -> {
                    '/'
                }
            }
        val operator2 =
            when {
                selector2 % 4 == 0 -> {
                    '+'
                }
                selector2 % 4 == 1 -> {
                    '-'
                }
                selector2 % 4 == 2 -> {
                    '*'
                }
                else -> {
                    '/'
                }
            }
        val pairOperator = Random.nextInt(1, 3)
        var operand1: Int
        var operand2: Int
        val operand3: Int
        when (selector1 % 4) {
            0 -> {
                when (selector2 % 4) {
                    0 -> {
                        when (pairOperator) {
                            1 -> {
                                operand1 = Random.nextInt(1, 50)
                                operand2 = Random.nextInt(1, 50)
                                operand3 = Random.nextInt(1, 50)
                            }
                            else -> {
                                operand1 = Random.nextInt(1, 50)
                                operand2 = Random.nextInt(1, 50)
                                operand3 = Random.nextInt(1, 50)
                            }
                        }

                    }
                    1 -> {
                        when (pairOperator) {
                            1 -> {
                                operand1 = Random.nextInt(1, 50)
                                operand2 = Random.nextInt(1, 50)
                                operand3 = Random.nextInt(1, operand1 + operand2)
                            }
                            else -> {
                                operand1 = Random.nextInt(1, 50)
                                operand2 = Random.nextInt(1, 50)
                                operand3 = Random.nextInt(1, operand2)
                            }
                        }
                    }
                    2 -> {
                        when (pairOperator) {
                            1 -> {
                                operand1 = Random.nextInt(1, 50)
                                operand2 = Random.nextInt(1, 50)
                                operand3 = Random.nextInt(1, 10)
                            }
                            else -> {
                                operand1 = Random.nextInt(1, 50)
                                operand2 = Random.nextInt(1, 50)
                                operand3 = Random.nextInt(1, 10)
                            }
                        }
                    }
                    else -> {
                        when (pairOperator) {
                            1 -> {
                                operand1 = Random.nextInt(1, 50)
                                operand2 = Random.nextInt(10, 50)
                                operand3 = Random.nextInt(1, 10)
                                operand2 -= (operand1 + operand2) % operand3
                            }
                            else -> {
                                operand1 = Random.nextInt(1, 50)
                                operand2 = Random.nextInt(1, 50)
                                operand3 = Random.nextInt(1, 10)
                                operand2 -= operand2 % operand3
                            }
                        }
                    }
                }
            }
            1 -> {
                when (selector2 % 4) {
                    0 -> {
                        when (pairOperator) {
                            1 -> {
                                operand1 = Random.nextInt(1, 50)
                                operand2 = Random.nextInt(1, operand1)
                                operand3 = Random.nextInt(1, 50)
                            }
                            else -> {
                                operand1 = Random.nextInt(10, 50)
                                operand2 = Random.nextInt(1, operand1)
                                operand3 = Random.nextInt(0, operand1 - operand2)
                            }
                        }
                    }
                    1 -> {
                        when (pairOperator) {
                            1 -> {
                                operand1 = Random.nextInt(10, 50)
                                operand2 = Random.nextInt(1, operand1)
                                operand3 = Random.nextInt(1, operand1 - operand2)
                            }
                            else -> {
                                operand1 = Random.nextInt(1, 50)
                                operand2 = Random.nextInt(1, operand1)
                                operand3 = Random.nextInt(1, operand1)
                            }
                        }
                    }
                    2 -> {
                        when (pairOperator) {
                            1 -> {
                                operand1 = Random.nextInt(1, 50)
                                operand2 = Random.nextInt(1, operand1)
                                operand3 = Random.nextInt(1, 10)
                            }
                            else -> {
                                operand3 = Random.nextInt(1, 9)
                                operand1 = Random.nextInt(10, 50)
                                operand2 = Random.nextInt(1, operand1 / operand3)
                            }
                        }
                    }
                    else -> {
                        when (pairOperator) {
                            1 -> {
                                operand1 = Random.nextInt(10, 50)
                                operand2 = Random.nextInt(1, operand1)
                                operand3 = Random.nextInt(1, 10)
                                operand1 -= (operand1 - operand2) % operand3
                            }
                            else -> {
                                operand1 = Random.nextInt(10, 50)
                                operand2 = Random.nextInt(1, operand1)
                                operand3 = Random.nextInt(1, 9)
                                operand2 -= operand2 % operand3
                            }
                        }
                    }
                }
            }
            2 -> {
                when (selector2 % 4) {
                    0 -> {
                        when (pairOperator) {
                            1 -> {
                                operand1 = Random.nextInt(1, 50)
                                operand2 = Random.nextInt(1, 10)
                                operand3 = Random.nextInt(1, 50)
                            }
                            else -> {
                                operand1 = Random.nextInt(1, 50)
                                operand2 = Random.nextInt(1, 10)
                                operand3 = Random.nextInt(1, 10 - operand2)
                            }
                        }
                    }
                    1 -> {
                        when (pairOperator) {
                            1 -> {
                                operand1 = Random.nextInt(1, 50)
                                operand2 = Random.nextInt(1, 10)
                                operand3 = Random.nextInt(1, operand1 * operand2)
                            }
                            else -> {
                                operand1 = Random.nextInt(1, 50)
                                operand2 = Random.nextInt(20, 50)
                                operand3 = Random.nextInt(operand2 - 10, operand2)
                            }
                        }
                    }
                    2 -> {
                        when (pairOperator) {
                            1 -> {
                                operand1 = Random.nextInt(1, 50)
                                operand2 = Random.nextInt(1, 10)
                                operand3 = Random.nextInt(1, 10)
                            }
                            else -> {
                                operand1 = Random.nextInt(1, 50)
                                operand2 = Random.nextInt(1, 4)
                                operand3 = Random.nextInt(1, 4)
                            }
                        }
                    }
                    else -> {
                        when (pairOperator) {
                            1 -> {
                                operand1 = Random.nextInt(1, 50)
                                operand2 = Random.nextInt(1, 10)
                                operand3 = Random.nextInt(1, 10)
                                operand1 -= operand1 % operand3
                            }
                            else -> {
                                operand1 = Random.nextInt(1, 50)
                                operand2 = Random.nextInt(1, 90)
                                operand3 = Random.nextInt(1, 10)
                                operand2 -= operand2 % operand3
                            }
                        }
                    }
                }
            }
            else -> {
                when (selector2 % 4) {
                    0 -> {
                        when (pairOperator) {
                            1 -> {
                                operand1 = Random.nextInt(1, 100)
                                operand2 = Random.nextInt(1, 10)
                                operand1 -= operand1 % operand2
                                operand3 = Random.nextInt(1, 50)
                            }
                            else -> {
                                operand1 = Random.nextInt(1, 100)
                                operand2 = Random.nextInt(1, 10)
                                operand3 = Random.nextInt(1, 11 - operand2)
                                operand1 -= operand1 % (operand3 + operand2)
                            }
                        }
                    }
                    1 -> {
                        when (pairOperator) {
                            1 -> {
                                operand1 = Random.nextInt(1, 50)
                                operand2 = Random.nextInt(1, 10)
                                operand1 -= operand1 % operand2
                                operand3 = Random.nextInt(1, operand1 / operand2)
                            }
                            else -> {
                                operand1 = Random.nextInt(1, 100)
                                operand2 = Random.nextInt(1, 50)
                                operand3 = Random.nextInt(1, operand2)
                                operand1 -= operand1 % (operand2 - operand3)
                            }
                        }
                    }
                    2 -> {
                        when (pairOperator) {
                            1 -> {
                                operand1 = Random.nextInt(1, 50)
                                operand2 = Random.nextInt(1, 9)
                                operand1 -= operand1 % operand2
                                operand3 = Random.nextInt(1, 9)
                            }
                            else -> {
                                operand1 = Random.nextInt(1, 100)
                                operand2 = Random.nextInt(1, 4)
                                operand3 = Random.nextInt(1, 4)
                                operand1 -= operand1 % (operand2 * operand3)
                            }
                        }
                    }
                    else -> {
                        when (pairOperator) {
                            1 -> {
                                operand1 = Random.nextInt(1, 100)
                                operand2 = Random.nextInt(1, 9)
                                operand3 = Random.nextInt(1, 9)
                                operand1 *= operand2 * operand3

                            }
                            else -> {
                                operand3 = Random.nextInt(1, 9)
                                operand1 = Random.nextInt(1, 100)
                                operand2 = Random.nextInt(1, 9)
                                operand2 *= operand2 * operand3
                                operand1 -= operand1 % (operand2 / operand3)
                            }
                        }
                    }
                }
            }
        }
        answer =
            when (selector1 % 4) {
                0 -> {
                    when (selector2 % 4) {
                        0 -> {
                            when (pairOperator) {
                                1 -> (operand1 + operand2) + operand3
                                else -> operand1 + (operand2 + operand3)
                            }
                        }
                        1 -> {
                            when (pairOperator) {
                                1 -> (operand1 + operand2) - operand3
                                else -> operand1 + (operand2 - operand3)
                            }
                        }
                        2 -> {
                            when (pairOperator) {
                                1 -> (operand1 + operand2) * operand3
                                else -> operand1 + (operand2 * operand3)
                            }
                        }
                        else -> {
                            when (pairOperator) {
                                1 -> (operand1 + operand2) / operand3
                                else -> operand1 + (operand2 / operand3)
                            }
                        }
                    }
                }
                1 -> {
                    when (selector2 % 4) {
                        0 -> {
                            when (pairOperator) {
                                1 -> (operand1 - operand2) + operand3
                                else -> operand1 - (operand2 + operand3)
                            }
                        }
                        1 -> {
                            when (pairOperator) {
                                1 -> (operand1 - operand2) - operand3
                                else -> operand1 - (operand2 - operand3)
                            }
                        }
                        2 -> {
                            when (pairOperator) {
                                1 -> (operand1 - operand2) * operand3
                                else -> operand1 - (operand2 * operand3)
                            }
                        }
                        else -> {
                            when (pairOperator) {
                                1 -> (operand1 - operand2) / operand3
                                else -> operand1 - (operand2 / operand3)
                            }
                        }
                    }
                }
                2 -> {
                    when (selector2 % 4) {
                        0 -> {
                            when (pairOperator) {
                                1 -> (operand1 * operand2) + operand3
                                else -> operand1 * (operand2 + operand3)
                            }
                        }
                        1 -> {
                            when (pairOperator) {
                                1 -> (operand1 * operand2) - operand3
                                else -> operand1 * (operand2 - operand3)
                            }
                        }
                        2 -> {
                            when (pairOperator) {
                                1 -> (operand1 * operand2) * operand3
                                else -> operand1 * (operand2 * operand3)
                            }
                        }
                        else -> {
                            when (pairOperator) {
                                1 -> (operand1 * operand2) / operand3
                                else -> operand1 * (operand2 / operand3)
                            }
                        }
                    }
                }
                else -> {
                    when (selector2 % 4) {
                        0 -> {
                            when (pairOperator) {
                                1 -> (operand1 / operand2) + operand3
                                else -> operand1 / (operand2 + operand3)
                            }
                        }
                        1 -> {
                            when (pairOperator) {
                                1 -> (operand1 / operand2) - operand3
                                else -> operand1 / (operand2 - operand3)
                            }
                        }
                        2 -> {
                            when (pairOperator) {
                                1 -> (operand1 / operand2) * operand3
                                else -> operand1 / (operand2 * operand3)
                            }
                        }
                        else -> {
                            when (pairOperator) {
                                1 -> (operand1 / operand2) / operand3
                                else -> operand1 / (operand2 / operand3)
                            }
                        }
                    }
                }
            }
        question_display?.text =
            when (pairOperator) {
                1 -> "( $operand1  $operator1  $operand2 ) $operator2 $operand3"
                else -> "$operand1  $operator1  ( $operand2 $operator2 $operand3 )"
            }
    }

    private fun changeButtonColor(buttonDrawable: Drawable?) {
        num0?.setBackgroundDrawable(buttonDrawable)
        num1?.setBackgroundDrawable(buttonDrawable)
        num2?.setBackgroundDrawable(buttonDrawable)
        num3?.setBackgroundDrawable(buttonDrawable)
        num4?.setBackgroundDrawable(buttonDrawable)
        num5?.setBackgroundDrawable(buttonDrawable)
        num6?.setBackgroundDrawable(buttonDrawable)
        num7?.setBackgroundDrawable(buttonDrawable)
        num8?.setBackgroundDrawable(buttonDrawable)
        num9?.setBackgroundDrawable(buttonDrawable)
        all_clear?.setBackgroundDrawable(buttonDrawable)
    }

    private fun timer() {
        if (timer_counter!! > timer_limit) {
            timer_counter = 0
            val i = Intent(this, ScoreDisplayActivity::class.java)
            i.putExtra("popupType", finished)
            i.putExtra("level", level)
            startActivity(i)
            this.finish()
        } else {
            Handler().postDelayed({
                timer_counter = timer_counter?.plus(1)
                minutes = String.format("%02d", timer_counter?.div(600))
                seconds = String.format("%02d", timer_counter?.rem(600)?.div(10))
                milliSeconds = String.format("%01d", timer_counter?.rem(10))
                val timerCount = "$minutes:$seconds.$milliSeconds"
                timer_display?.text = timerCount
                timer()
            }, 100)
        }
    }
}