package com.yessorae.presentation.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yessorae.common.Logger
import com.yessorae.domain.repository.TodoRepository
import com.yessorae.presentation.R
import com.yessorae.presentation.TodoNotificationManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.UUID

@HiltWorker
class TodoNotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val notificationManager: TodoNotificationManager,
    private val todoRepository: TodoRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val todoId = inputData.getInt(PARAM_TODO_ID, FAILURE_ID)
        Logger.debug("TodoNotificationWorker todoId $todoId")

        if (todoId == FAILURE_ID) {
            Logger.error("TodoNotificationWorker FAILURE_ID")
            return Result.failure()
        }

        Logger.debug("TodoNotificationWorker FAILURE_ID not")

        val todo = todoRepository.getTodo(todoId = todoId)
        Logger.debug("TodoNotificationWorker todo $todo")

        val title = applicationContext.getString(R.string.todo_noti_title)

        val body = todo.startTime?.let { startTime ->
            applicationContext.getString(R.string.todo_noti_body).format(
                startTime.hour,
                startTime.minute,
                todo.title
            )
        } ?: applicationContext.getString(R.string.todo_noti_body_without_start_time)
            .format(todo.title)

        notificationManager.apply {
            createNotificationChannel(context = applicationContext)
            showNotification(
                context = applicationContext,
                builder = createNotification(
                    context = applicationContext,
                    title = title,
                    body = body
                )
            )
            Logger.debug("showNotification $todoId")
        }

        return Result.success()
    }

    companion object {
        const val TAG = "TodoNotificationWorker"
        const val PARAM_TODO_ID = "todo_id"
        const val PARAM_NOTIFICATION_ID = "notification_id"
        const val FAILURE_ID = Int.MIN_VALUE
        fun createTag(todoId: Int, tagId: Int): String {
            return "$TAG - $todoId - $tagId"
        }
    }
}