package com.michasoft.thelasttime.reminder

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.michasoft.thelasttime.LastTimeApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReshowRemindersWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    @Inject
    lateinit var reshowRemindersUseCase: ReshowRemindersUseCase

    override suspend fun doWork(): Result {
        (applicationContext as LastTimeApplication).userSessionComponent?.inject(this)
        withContext(Dispatchers.IO) {
            reshowRemindersUseCase.invoke()
        }
        return Result.success()
    }

}