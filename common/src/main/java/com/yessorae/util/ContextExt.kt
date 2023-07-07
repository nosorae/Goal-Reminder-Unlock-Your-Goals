package com.yessorae.util

import android.content.Context
import android.content.Intent
import android.widget.Toast

fun Context.showToast(stringModel: StringModel) {
    Toast.makeText(this, stringModel.get(this), Toast.LENGTH_LONG).show()
}

fun Context.startActivityByNewTask(activity: Class<*>) {
    Intent(this, activity).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }.also {
        startActivity(it)
    }
}