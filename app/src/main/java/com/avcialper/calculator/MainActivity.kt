package com.avcialper.calculator

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.avcialper.calculator.databinding.ActivityMainBinding
import com.avcialper.calculator.room.AppDatabase
import com.avcialper.calculator.room.History
import com.avcialper.calculator.room.HistoryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var inputVal = ""
    private lateinit var db: AppDatabase
    private lateinit var dao: HistoryDao
    private var historyList: List<History> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.TRANSPARENT

        db = AppDatabase.databaseConnect(this)!!
        dao = db.getDao()
    }

    private fun calculateResult() {
        try {
            val calculateVal = inputVal.replace("√", "sqrt")
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

    fun getHistory() {
        lifecycleScope.launch(Dispatchers.IO) {
            dao.getAll().collect {
                historyList = it
            }
        }
    }

    fun onNumberButtonClick(view: View) {
        val button = view as Button
        inputVal += button.text
        binding.input.text = inputVal
        calculateResult()
    }

    fun onBackSpaceClick(view: View) {
        if (inputVal.isNotEmpty()) {
            if (inputVal.length == 1) {
                inputVal = ""
            } else if (inputVal.substring(inputVal.lastIndex - 1) == "√(") {
                inputVal = inputVal.substring(0, inputVal.lastIndex - 1)
            } else {
                inputVal = inputVal.substring(0, inputVal.lastIndex)
            }
            binding.input.text = inputVal
        }
        calculateResult()
    }

    fun clearAll(view: View) {
        if (inputVal.isNotEmpty()) {
            inputVal = ""
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
        } else if (inputVal.isNotEmpty() && (inputVal[inputVal.lastIndex].isDigit() || inputVal[inputVal.lastIndex] == ')')) {
            when (operand) {
                "√" -> {
                    inputVal += "√("
                }

                "x" -> {
                    inputVal += operand
                }

                else -> {
                    inputVal += operand
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
            } else {
                inputVal += operand
            }
        } else if (inputVal.isEmpty() && operand == ")") {
            inputVal = ""
        } else {
            inputVal += operand
        }
        binding.input.text = inputVal
        calculateResult()
    }

    fun onDotClick(view: View) {
        if (inputVal.isEmpty()) {
            inputVal = "0"
        }
        if (inputVal[inputVal.lastIndex] != '.' && inputVal[inputVal.lastIndex].isDigit()) {
            inputVal += "."
            binding.input.text = inputVal
        }
    }

    fun onResult(view: View) {
        binding.apply {
            if (result.text.toString().isNotEmpty()) {
                inputVal = binding.result.text.toString()
                input.text = inputVal
                result.text = ""
            }
        }
    }

}