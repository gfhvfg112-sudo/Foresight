package com.foresight.app.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.foresight.app.data.local.entity.Alert
import com.foresight.app.repository.ItemRepository
import com.foresight.app.util.DateUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class ExpiryCheckWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val itemRepository: ItemRepository,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            checkExpiringItems()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private suspend fun checkExpiringItems() {
        // Check items expiring in 1, 3, 7, 14, and 30 days
        val checkDays = listOf(0, 1, 3, 7, 14, 30)

        for (days in checkDays) {
            val threshold = DateUtils.daysFromNow(days)
            val items = itemRepository.getItemsExpiringBefore(threshold)

            for (item in items) {
                val daysUntil = DateUtils.daysUntil(item.expiryDate)

                // Only notify for items that haven't expired yet or just expired today
                if (daysUntil in -1..days) {
                    val pendingAlerts = itemRepository.getPendingAlerts()
                    val alreadyAlerted = pendingAlerts.any {
                        it.itemId == item.id && it.alertDays <= days && it.isSent
                    }

                    if (!alreadyAlerted) {
                        notificationHelper.showExpiryNotification(
                            itemId = item.id,
                            itemName = item.name,
                            daysLeft = daysUntil.toInt()
                        )

                        // Record the alert as sent
                        itemRepository.insertAlert(
                            Alert(
                                itemId = item.id,
                                alertDays = days,
                                isSent = true,
                                sentAt = System.currentTimeMillis()
                            )
                        )
                    }
                }
            }
        }
    }

    companion object {
        private const val WORK_NAME = "expiry_check"

        fun schedule(context: Context) {
            val request = PeriodicWorkRequestBuilder<ExpiryCheckWorker>(
                8, TimeUnit.HOURS  // Run every 8 hours
            )
                .setConstraints(
                    Constraints.Builder()
                        .setRequiresBatteryNotLow(true)
                        .build()
                )
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }
}
