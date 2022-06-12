package com.example.servicestest

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

class MyIntentService2 : IntentService(NAME) {

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
        setIntentRedelivery(true)

    }

    override fun onHandleIntent(intent: Intent?) {
        log("onHandleIntent")
        val page = intent?.getIntExtra(PAGE, 0)?:0
        for (i in 0..10) {
            Thread.sleep(1000)
            log("Timer $i $page")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        log("onDestroy")
    }


    companion object {
        private const val NAME = "MyIntentService2"
        private const val PAGE = "page"
        fun newIntent(context: Context, page: Int): Intent {
            return Intent(context, MyIntentService2::class.java).apply {
                putExtra(PAGE, page)
            }

        }
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyIntentService2 $message")

    }

}