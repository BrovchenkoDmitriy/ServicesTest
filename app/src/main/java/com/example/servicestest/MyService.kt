package com.example.servicestest

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

class MyService : Service() {

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        scope.launch {
            for (i in 0..10){
                delay(1000)
                log("Timer $i")
            }
        }
        return START_STICKY
        //super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        log("onDestroy")
    }




    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MyService::class.java)
        }
    }

    private fun log(message:String){
        Log.d("SERVICE_TAG", "MyService $message")

    }
}