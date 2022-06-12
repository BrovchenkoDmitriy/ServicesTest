package com.example.servicestest

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobWorkItem
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.example.servicestest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private var page = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.simpleService.setOnClickListener {
            startService(MyService.newIntent(this))
        }
        binding.foregroundService.setOnClickListener {
            ContextCompat.startForegroundService(
                this,
                MyForegroundService.newIntent(this)
            )
        }
        binding.intentService.setOnClickListener {
            ContextCompat.startForegroundService(
                this,
                MyIntentService.newIntent(this)
            )
        }
        binding.jobScheduler.setOnClickListener {
            val componentName = ComponentName(this, MyJobService::class.java)
            val jobInfo = JobInfo.Builder(MyJobService.JOB_ID, componentName)
                .setRequiresCharging(true) //ограничение устройство должно быть на зарядке
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY) //ограничение устройство должно быть в сети
                //.setPersisted(true) // сервис будет перезапущен даже при перезагрузке устройства  (не работает с очередью сервисов)
                .build()
            val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val intent = MyJobService.newIntent(page++)
                jobScheduler.enqueue(jobInfo, JobWorkItem(intent))
            } else { //если SDK меньше API 26, то используем всместо JobScheduler IntentService
                startService(MyIntentService2.newIntent(this, page++))
            }
        }
        binding.jobIntentService.setOnClickListener {
            MyJobIntentService.enqueue(this, page++)
        }
        binding.workManager.setOnClickListener {
            val workManager =
                WorkManager.getInstance(applicationContext)//чтоб не было утечек памяти
            workManager.enqueueUniqueWork( // если исп-ть enqueue то запустив 10 воркеров они все
                                          // будут выпол-ся. В нашем случае будет выппол-ся 1 воркер
                MyWorker.WORK_NAME,
                ExistingWorkPolicy.APPEND, //если воркер был запущет, то
                // APPEND новый воркер будет добавлен в очередь
                // KEEP новый воркер проигнорируется
                // REPLACE старый воркер замениться новым

                MyWorker.makeRequest(page++) // через OneTimeWorkRequest передаем page и ограничения
            )
        }
    }
}