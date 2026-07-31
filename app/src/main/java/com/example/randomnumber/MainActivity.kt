package com.example.randomnumber

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
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

        // 常用概率 Chip：点击直接填入输入框
        val grid = findViewById<GridLayout>(R.id.gridCommonProbability)
        for (i in 0 until grid.childCount) {
            val chip = grid.getChildAt(i) as? Chip ?: continue
            chip.setOnClickListener { editProbability.setText(chip.text) }
        }

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
                        textResult.setTextColor(ContextCompat.getColor(this, R.color.result_success))
                    } else {
                        textResult.text = "0"
                        textResult.setTextColor(ContextCompat.getColor(this, R.color.result_failure))
                    }
                }
            }
        }
    }
}
