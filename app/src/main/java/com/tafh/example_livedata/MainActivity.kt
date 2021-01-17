package com.tafh.example_livedata

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tafh.example_livedata.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private var isPaused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.seconds.observe(this, Observer { seconds ->
            binding.tvNumber.text = "$seconds s"
        })

        timerIsStop()

        binding.btnStart.setOnClickListener {
            isPaused = true
            val detik = binding.etDetik.text


            if (detik.isEmpty() || detik.length < 0 ) {
                viewModel._timeState.value = TimerState.isStopped
                Toast.makeText(this, "Invalid number", Toast.LENGTH_SHORT).show()
            } else {
                timerIsRunning()
                viewModel._timeState.value = TimerState.isRunning
                viewModel._timerValue.value = detik.toString().toLong() * 1000 //second to millisecond
                binding.etDetik.text = null
                viewModel.startTimer()
            }

        }

        binding.btnStop.setOnClickListener {
            isPaused = true
            timerIsStop()

            viewModel._timeState.value = TimerState.isStopped

            binding.tvNumber.text = "0"
            viewModel.stopTimer()

            toast("Timer Stopped")
        }

        binding.btnPause.setOnClickListener {
            if (isPaused) {

                isPaused = false
                timerIsPaused()

                viewModel._timeState.value = TimerState.isPaused

                toast("Timer Paused")
            } else { //resumed

                isPaused = true
                timerIsResume()

                viewModel._timeState.value = TimerState.isRunning
                viewModel.startTimer()

                toast("Timer Resumed")
            }

        }


        viewModel.finished.observe(this, Observer { finished ->
            if (finished) {
                isPaused = true
                viewModel._timeState.value = TimerState.isStopped
                timerIsStop()
                toast("Timer Finished")
            }
        })

    }

    fun Context.toast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    fun timerIsRunning() {
        binding.apply {
            btnStart.isEnabled = false

            btnStop.isEnabled = true
            btnPause.isEnabled = true
        }
    }

    fun timerIsStop() {
        binding.apply {
            btnStop.isEnabled = false
            btnPause.isEnabled = false

            btnStart.isEnabled = true
        }
    }

    fun timerIsPaused() {
        binding.apply {
            btnPause.text = "Resume"
            btnStart.isEnabled = false

            btnStop.isEnabled = true
        }
    }

    fun timerIsResume() {
        binding.apply {

            btnPause.text = "Paused"
            btnStart.isEnabled = false

            btnStop.isEnabled = true
        }
    }
}