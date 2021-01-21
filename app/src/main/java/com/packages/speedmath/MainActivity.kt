package com.packages.speedmath

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

var beginnerTextView:TextView?= null
var intermediateTextView:TextView?= null
var expertTextView:TextView?= null
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        beginnerTextView = findViewById(R.id.main_beginner)
        intermediateTextView = findViewById(R.id.main_intermediate)
        expertTextView = findViewById(R.id.main_expert)

        beginnerTextView?.setOnClickListener {
            val i = Intent(this,AbacusActivity::class.java)
            i.putExtra("level",1)
            startActivity(i)
        }
        intermediateTextView?.setOnClickListener {
            val i = Intent(this,AbacusActivity::class.java)
            i.putExtra("level",2)
            startActivity(i)
        }
        expertTextView?.setOnClickListener {
            val i = Intent(this,AbacusActivity::class.java)
            i.putExtra("level",3)
            startActivity(i)
        }

    }
}