package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    lateinit var timerBinder : TimerService.TimerBinder
    var isConnected = false

    val timeHandler = Handler(Looper.getMainLooper()){
        true
    }

    val serviceConnection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            timerBinder = service as TimerService.TimerBinder
            timerBinder.setHandler(timeHandler)
            isConnected = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isConnected = false
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindService(
            Intent(this, TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE)
        findViewById<Button>(R.id.startButton).setOnClickListener {
            Toast.makeText(this, "USE MENU", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.pauseButton).setOnClickListener {
            Toast.makeText(this, "USE MENU", Toast.LENGTH_SHORT).show()
        }
        
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            Toast.makeText(this, "USE MENU", Toast.LENGTH_SHORT).show()
        }
    }

    fun start(){
        timerBinder.setTextView(findViewById(R.id.textView))
        if(isConnected){
            timerBinder.start(100)
        }
    }

    fun pause(){
        if(isConnected){
            timerBinder.pause()
        }
    }

    fun stop(){
        if(isConnected){
            timerBinder.stop()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.start -> start()
            R.id.pause -> pause()
            R.id.stop -> stop()
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onDestroy() {
        unbindService(serviceConnection)
        super.onDestroy()
    }
}