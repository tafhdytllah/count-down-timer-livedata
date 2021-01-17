package com.tafh.example_livedata

import android.os.CountDownTimer
import android.os.health.TimerStat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.log

enum class TimerState{
    isStopped, isPaused, isRunning
}

class MainViewModel : ViewModel() {

    private lateinit var timer: CountDownTimer

    var _timeState = MutableLiveData<TimerState>()

    var _timerValue = MutableLiveData<Long>()

    private val _seconds = MutableLiveData<Int>()
    val seconds: LiveData<Int>
        get() = _seconds

    var _finished = MutableLiveData<Boolean>()
    val finished: LiveData<Boolean>
        get() = _finished

    val countDownInterval = (1000).toLong()

    fun startTimer() {

        timer = object : CountDownTimer(_timerValue.value.toString().toLong(), countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                if (_timeState.value == TimerState.isRunning) {
                    val timerLeft = millisUntilFinished/1000
                    _seconds.value = timerLeft.toInt()
                } else {
                    _timerValue.value = millisUntilFinished
                    cancel()
                }
            }

            override fun onFinish() {
                _finished.value = true
            }
        }
        timer.start()

    }

    fun stopTimer() {
        timer.cancel()
    }

}
