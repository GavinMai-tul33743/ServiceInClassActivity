package edu.temple.myapplication

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.NonDisposableHandle.parent

@Suppress("ControlFlowWithEmptyBody")
class TimerService : Service() {

    private var isRunning = false

    lateinit var t: TimerThread

    private var paused = false

    lateinit var timeHandler : Handler

    lateinit var textView : TextView

    inner class TimerBinder : Binder() {

        // Check if Timer is already running
        var isRunning: Boolean
            get() = this@TimerService.isRunning
            set(value) {this@TimerService.isRunning = value}

        // Start a new timer
        fun start(startValue: Int){

            if (!paused) {
                if (!isRunning) {
                    if (::t.isInitialized) t.interrupt()
                    this@TimerService.start(startValue)
                }
            } else {
                pause()
            }
        }

        // Stop a currently running timer
        fun stop() {
            if (::t.isInitialized || isRunning) {
                textView!!.text = null
                t.interrupt()
            }
        }

        // Pause a running timer
        fun pause() {
            this@TimerService.pause()
        }
        fun setHandler(_handler : Handler){
            timeHandler = _handler
        }

        fun setTextView(_view : TextView){
            textView = _view
        }

    }

    override fun onCreate() {
        super.onCreate()

        Log.d("TimerService status", "Created")
    }

    override fun onBind(intent: Intent): IBinder {
        return TimerBinder()
    }

    fun start(startValue: Int) {
        t = TimerThread(startValue)
        t.start()
    }

    fun pause () {
        if (::t.isInitialized) {
            paused = !paused
            isRunning = !paused
        }
    }



    inner class TimerThread(private val startValue: Int) : Thread() {

        override fun run() {
            isRunning = true
            try {
                for (i in startValue downTo 1)  {
                    Log.d("Countdown", i.toString())
                    textView!!.text = i.toString()
                        while (paused);
                    if(::timeHandler.isInitialized){
                        timeHandler.sendEmptyMessage(i)
                    }
                        sleep(1000)

                }
                isRunning = false
            } catch (e: InterruptedException) {
                Log.d("Timer interrupted", e.toString())
                isRunning = false
                paused = false
            }
        }

    }

    override fun onUnbind(intent: Intent?): Boolean {
        if (::t.isInitialized) {
            t.interrupt()
        }

        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("TimerService status", "Destroyed")
    }


}