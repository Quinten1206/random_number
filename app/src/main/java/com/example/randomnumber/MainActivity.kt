package com.example.randomnumber

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.graphics.Color
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var editProbability: EditText
    private lateinit var btnGenerate: Button
    private lateinit var textResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editProbability = findViewById(R.id.editProbability)
        btnGenerate = findViewById(R.id.btnGenerate)
        textResult = findViewById(R.id.textResult)

        btnGenerate.setOnClickListener {
            val input = editProbability.text.toString()
            val result = ProbabilityParser.parse(input)

            when (result) {
                is ParseResult.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
                is ParseResult.Success -> {
                    val r = Random.nextDouble() // [0, 1) 均匀分布
                    if (r < result.value) {
                        textResult.text = "1"
                        textResult.setTextColor(Color.rgb(76, 175, 80)) // Green 500
                    } else {
                        textResult.text = "0"
                        textResult.setTextColor(Color.rgb(244, 67, 54)) // Red 500
                    }
                }
            }
        }
    }
}
