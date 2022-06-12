package com.example.servicestest

import android.content.Context
import android.util.Log
import androidx.work.*

class MyWorker(context: Context, private val workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        log("doWork")
        val page = workerParameters.inputData.getInt(PAGE, 0) //получаем page из workerParameters
        for (i in 0..5) {
            Thread.sleep(1000)
            log("Timer $i $page")
        }
        return Result.success()// Result.failure() и Result.retry() если метод завершился с ошибкой
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyWorker $message")

    }

    companion object {
        private const val PAGE = "page"
        const val WORK_NAME = "work name"

        fun makeRequest(page: Int): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<MyWorker>()
                .setInputData(workDataOf(PAGE to page)) //кладем page
                .setConstraints(makeConstraints()) //накладываем ограничения
            .build()
        }

        private fun makeConstraints(): Constraints {
            return Constraints.Builder()
                .setRequiresCharging(true) //ограничение - устройство должно быть на зарядке
                .build()
        }

    }
}