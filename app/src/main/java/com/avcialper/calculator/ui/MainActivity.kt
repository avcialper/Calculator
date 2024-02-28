package com.avcialper.calculator.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.avcialper.calculator.databinding.ActivityMainBinding
import com.avcialper.calculator.room.AppDatabase
import com.avcialper.calculator.room.History
import com.avcialper.calculator.room.HistoryDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var historyList: List<History> = emptyList()
    private var isHistoryOpen = false
    private lateinit var adapter: HistoryAdapter
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.TRANSPARENT
        initUI()

        viewModel.input.observeForever {
            binding.input.text = it
        }

        viewModel.result.observeForever {
            binding.result.text = it
        }

    }

    private fun initUI() {

        binding.apply {

            history.setOnClickListener {
                if (!isHistoryOpen) {
                    getHistory()
                    isHistoryOpen = true
                }
            }

            historyRV.setHasFixedSize(true)
            historyRV.layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = HistoryAdapter(historyList)
            historyRV.adapter = adapter
        }
    }

    private fun getHistory() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.getHistoryList()
            viewModel.history.observeForever {
                historyList = it
                updateAdapter()
            }
        }
    }

    private fun updateAdapter() {
        adapter = HistoryAdapter(historyList)
        binding.historyRV.adapter = adapter
    }

    fun onNumberButtonClick(view: View) {
        val button = view as Button
        viewModel.onNumberButtonClick(button.text.toString())
    }

    fun onBackSpaceClick(view: View) {
        viewModel.onBackSpaceClick()
    }

    fun clearAll(view: View) {
        viewModel.clearAll()
    }

    fun onOperandClick(view: View) {
        val button = view as Button
        viewModel.onOperandClick(button.text.toString())
    }

    fun onBracketClick(view: View) {
        val button = view as Button
        viewModel.onBracketClick(button.text.toString())
    }

    fun onDotClick(view: View) {
        viewModel.onDotClick()
    }

    fun onResult(view: View) {
        viewModel.onResult()
    }

}