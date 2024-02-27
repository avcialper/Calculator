package com.avcialper.calculator

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.avcialper.calculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    // To show the formula to the user
    private var inputVal = ""
    // To calculate the formula
    private var calculateVal = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.TRANSPARENT
    }

    private fun calculateResult() {
        try {
            val expressionBuilder = ExpressionBuilder(calculateVal).build()
            val result = expressionBuilder.evaluate()

            val longResult = result.toLong()
            if (result == longResult.toDouble()) {
                binding.result.text = longResult.toString()
            } else {
                binding.result.text = result.toString()
            }
        } catch (e: Exception) {
            binding.result.text = ""
            Log.e("calculate_error", "Error")
        }
    }

    fun onNumberButtonClick(view: View) {
        val button = view as Button
        inputVal += button.text
        calculateVal += button.text
        binding.input.text = inputVal
        calculateResult()
    }

    fun onBackSpaceClick(view: View) {
        if (inputVal.isNotEmpty()) {
            if (inputVal.length == 1) {
                inputVal = ""
                calculateVal = ""
            } else if (inputVal.substring(inputVal.lastIndex - 1) == "√(") {
                inputVal = inputVal.substring(0, inputVal.lastIndex - 1)
                calculateVal = calculateVal.substring(0, calculateVal.lastIndex - 4)
            } else {
                inputVal = inputVal.substring(0, inputVal.lastIndex)
                calculateVal = calculateVal.substring(0, calculateVal.lastIndex)
            }
            binding.input.text = inputVal
        }
        if (inputVal.isNotEmpty())
            calculateResult()
        else
            binding.result.text = ""
    }

    fun clearAll(view: View) {
        if (inputVal != "") {
            inputVal = ""
            calculateVal = ""
            binding.apply {
                input.text = ""
                result.text = ""
            }
        }
    }

    fun onOperandClick(view: View) {
        val button = view as Button
        val operand = button.text
        if (inputVal.isEmpty() && operand == "√") {
            inputVal = "√("
            calculateVal = "sqrt("
        } else if (inputVal.isNotEmpty() && (inputVal[inputVal.lastIndex].isDigit() || inputVal[inputVal.lastIndex] == ')')) {
            when (operand) {
                "√" -> {
                    inputVal += "√("
                    calculateVal += "sqrt("
                }

                "x" -> {
                    inputVal += operand
                    calculateVal += "*"
                }

                else -> {
                    inputVal += operand
                    calculateVal += operand
                }
            }
        }
        binding.input.text = inputVal
        calculateResult()
    }

    fun onBracketClick(view: View) {
        val button = view as Button
        val operand = button.text
        if (operand == ")" && inputVal.isNotEmpty()) {
            if (inputVal[inputVal.lastIndex] == '(') {
                inputVal = inputVal.substring(0, inputVal.lastIndex)
                calculateVal = calculateVal.substring(0, calculateVal.lastIndex)
            } else {
                inputVal += operand
                calculateVal += operand
            }
        } else if (inputVal.isEmpty() && operand == ")") {
            inputVal = ""
            calculateVal = ""
        } else {
            inputVal += operand
            calculateVal += operand
        }
        binding.input.text = inputVal
        calculateResult()
    }

    fun onDotClick(view: View) {
        if (inputVal.isEmpty()) {
            inputVal = "0"
            calculateVal = "0"
        }
        if (inputVal[inputVal.lastIndex] != '.' && inputVal[inputVal.lastIndex].isDigit()) {
            inputVal += "."
            calculateVal += "."
            binding.input.text = inputVal
        }
    }

    fun onResult(view: View) {
        binding.apply {
            if (result.text.toString().isNotEmpty()) {
                inputVal = binding.result.text.toString()
                calculateVal = inputVal
                input.text = inputVal
                result.text = ""
            }
        }
    }

}