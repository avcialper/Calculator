package com.avcialper.calculator.data

import android.util.Log
import com.avcialper.calculator.room.AppDatabase
import com.avcialper.calculator.room.History
import com.avcialper.calculator.room.HistoryDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.objecthunter.exp4j.ExpressionBuilder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val dao: HistoryDao) {

    suspend fun getHistoryList(): List<History> {
        return dao.getAll()
    }

    suspend fun clearHistory() {
        dao.delete()
    }

    fun calculateResult(input: String): String? {
        try {
            var calculateVal = input.toString().replace("√", "sqrt")
            calculateVal = calculateVal.replace("x", "*")
            val expressionBuilder = ExpressionBuilder(calculateVal).build()
            val result = expressionBuilder.evaluate()
            val longResult = result.toLong()

            return if (result == longResult.toDouble()) {
                longResult.toString()
            } else {
                result.toString()
            }
        } catch (e: Exception) {
            Log.e("calculate_error", "Error")
            return null
        }
    }

    fun onNumberButtonClick(input: String, data: String): String {
        return input + data
    }

    fun onBackSpaceClick(input: String): String {
        var currentValue = input
        if (input.isNotEmpty()) {
            currentValue = if (input.length == 1) {
                ""
            } else if (input.substring(input.lastIndex - 1) == "√(") {
                input.substring(0, input.lastIndex - 1)
            } else {
                input.substring(0, input.lastIndex)
            }
        }
        return currentValue
    }

    fun onOperandClick(input: String, data: String): String {
        var currentData = input
        if (input.isEmpty() && data == "√") {
            currentData = "√("
        } else if (input.isNotEmpty() && (input[input.lastIndex].isDigit() || input[input.lastIndex] == ')')) {
            currentData += when (data) {
                "√" -> {
                    "√("
                }

                else -> {
                    data
                }
            }
        }
        return currentData
    }

    fun onBracketClick(input: String, data: String): String {
        var currentData = input
        if (data == ")" && input.isNotEmpty()) {
            if (input[input.lastIndex] == '(') {
                currentData = input.substring(0, input.lastIndex)
            } else {
                currentData += data
            }
        } else if (input.isEmpty() && data == ")") {
            currentData = ""
        } else {
            currentData += data
        }
        return currentData
    }

    fun onDotClick(input: String): String {
        var currentData = input
        if (input.isEmpty()) {
            currentData = "0"
        }
        if (input[input.lastIndex] != '.' && input[input.lastIndex].isDigit()) {
            currentData += "."
        }
        return currentData
    }

    fun onResult(input: String, result: String): Boolean {
        if (result.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                val newHistory = History(0, input, result)
                dao.addValue(newHistory)
            }
            return true
        }
        return false
    }

}