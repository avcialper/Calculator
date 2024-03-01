package com.avcialper.calculator.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.avcialper.calculator.R
import com.avcialper.calculator.databinding.ActivityMainBinding
import com.avcialper.calculator.room.History
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var historyList: List<History> = emptyList()
    private lateinit var adapter: HistoryAdapter
    private val viewModel: MainViewModel by viewModels()
    private var isHistoryOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.BLACK
        initUI()
    }

    private fun initUI() {
        binding.apply {
            historyRV.setHasFixedSize(true)
            historyRV.layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = HistoryAdapter(historyList, viewModel)
            historyRV.adapter = adapter

            viewModel.input.observeForever {
                input.text = it
            }

            viewModel.result.observeForever {
                result.text = it
            }

            buttonResult.setOnClickListener {
                viewModel.onResult()
            }

            viewModel.history.observeForever {
                historyList = it
                updateAdapter()
                isHistoryOpen = if (isHistoryOpen && historyList.isEmpty()) {
                    motionLayout.transitionToStart()
                    history.setImageResource(R.drawable.history)
                    Toast.makeText(this@MainActivity, "No history", Toast.LENGTH_LONG).show()
                    false
                } else {
                    motionLayout.transitionToEnd()
                    history.setImageResource(R.drawable.close)
                    true
                }
            }

            history.setOnClickListener {
                isHistoryOpen = if (!isHistoryOpen) {
                    getHistory()
                    motionLayout.transitionToEnd()
                    history.setImageResource(R.drawable.close)
                    true
                } else {
                    motionLayout.transitionToStart()
                    history.setImageResource(R.drawable.history)
                    false
                }
            }

            clear.setOnClickListener {
                if (isHistoryOpen) {
                    lifecycleScope.launch {
                        viewModel.clearHistory()
                    }
                    motionLayout.transitionToStart()
                    history.setImageResource(R.drawable.history)
                }
            }
        }
    }

    private fun getHistory() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.getHistoryList()
        }
    }

    private fun updateAdapter() {
        adapter = HistoryAdapter(historyList, viewModel)
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

}