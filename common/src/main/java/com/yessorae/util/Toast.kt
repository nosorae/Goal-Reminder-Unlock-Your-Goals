package com.yessorae.util

import android.content.Context
import android.widget.Toast

fun Context.showToast(stringModel: StringModel) {
    Toast.makeText(this, stringModel.get(this), Toast.LENGTH_LONG).show()
}