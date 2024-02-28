package com.avcialper.calculator.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.avcialper.calculator.data.Repository
import com.avcialper.calculator.room.History
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private var _input = MutableLiveData("")
    private var _result = MutableLiveData("")
    private var _history = MutableLiveData<List<History>>()

    val input: LiveData<String>
        get() = _input
    val result: LiveData<String>
        get() = _result
    val history: LiveData<List<History>>
        get() = _history

    suspend fun getHistoryList() {
        coroutineScope {
            _history.value = repository.getHistoryList()
        }
    }

    private fun calculateResult() {
        _result.value = repository.calculateResult(_input.value.toString())
    }

    fun onNumberButtonClick(data: String) {
        val response = repository.onNumberButtonClick(_input.value.toString(), data)
        _input.value = response
        calculateResult()
    }

    fun onBackSpaceClick() {
        val response = repository.onBackSpaceClick(_input.value.toString())
        _input.value = response
        calculateResult()
    }

    fun clearAll() {
        _input.value = ""
        _result.value = ""
    }

    fun onOperandClick(data: String) {
        val response = repository.onOperandClick(_input.value.toString(), data)
        _input.value = response
        calculateResult()
    }

    fun onBracketClick(data: String) {
        val response = repository.onBracketClick(_input.value.toString(), data)
        _input.value = response
        calculateResult()
    }

    fun onDotClick() {
        val response = repository.onDotClick(_input.value.toString())
        _input.value = response
    }

    fun onResult() {
        val response = repository.onResult(_input.value.toString(), _result.value.toString())
        _input.value = response
        _result.value = ""
    }

}