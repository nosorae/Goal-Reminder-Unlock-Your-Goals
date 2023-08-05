package com.yessorae.presentation.model.enums

import com.yessorae.presentation.R
import com.yessorae.util.ResString
import com.yessorae.util.StringModel
import kotlinx.datetime.DateTimeUnit

enum class AlarmType(val number: Int, val unit: DateTimeUnit, val display: StringModel) {
    NOW(
        number = 0,
        unit = DateTimeUnit.MILLISECOND,
        display = ResString(R.string.common_now)
    ),
    ONE_MINUTE(
        number = 1,
        unit = DateTimeUnit.MINUTE,
        display = ResString(R.string.common_n_minute_ago, 1)
    ),
    TEN_MINUTE(
        number = 10,
        unit = DateTimeUnit.MINUTE,
        display = ResString(R.string.common_n_minute_ago, 10)
    ),
    FIFTEEN_MINUTE(
        number = 15,
        unit = DateTimeUnit.MINUTE,
        display = ResString(R.string.common_n_minute_ago, 15)
    ),
    THIRTY_MINUTE(
        number = 30,
        unit = DateTimeUnit.MINUTE,
        display = ResString(R.string.common_n_minute_ago, 30)
    ),
    ONE_HOUR(
        number = 1,
        unit = DateTimeUnit.HOUR,
        display = ResString(R.string.common_n_hour_ago, 1)
    ),
    ONE_DAY(
        number = 1,
        unit = DateTimeUnit.DAY,
        display = ResString(R.string.common_n_day_ago, 1)
    )
}