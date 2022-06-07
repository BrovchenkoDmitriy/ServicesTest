package com.example.servicestest

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import kotlinx.coroutines.*

class MyJobService : JobService() {

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        log("onStartJob")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            scope.launch {
                //получаем из params объект workItem, т.о. мы из очереди получим первый сервис
                var workItem = params?.dequeueWork()
                //проверка очереди, если в ней есть сервис, то он выполняется
                while (workItem != null) {
                    val page = workItem.intent.getIntExtra(PAGE, 0)
                    for (i in 0..5) {
                        delay(1000)
                        log("Timer $i $page ")
                    }
                    // заканчиваем полученный  из очереди сервис и достаем следующий сервис из очереди
                    params?.completeWork(workItem)
                    workItem = params?.dequeueWork()
                }
                // когда все сервисы из очереди выполнены "сворачиваем лавочку"
                jobFinished(params, true)
            }
        }
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        log("onStopJob")
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        log("onDestroy")
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyJobService $message")

    }

    companion object {
        const val JOB_ID = 666
        const val PAGE = "page"

        fun newIntent(page: Int): Intent {
            return Intent().apply {
                putExtra(PAGE, page)
            }
        }
    }


}