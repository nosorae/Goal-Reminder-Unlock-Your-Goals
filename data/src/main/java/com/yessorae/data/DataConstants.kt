package com.yessorae.data

object DataConstants {
    const val PREFERENCE_DATASTORE_NAME = "timeledger-preference-datastore"
    const val PREF_KEY_IS_SCREEN_ON = "is_screen_on"
    const val PREF_KEY_COMPLETE_ON_BOARDING = "on_boarding"
    const val PREF_KEY_COMPLETE_ON_BOARDING_MOCK_DATA = "on_boarding_mock_data"
    const val PREF_KEY_FINAL_GOAL = "final_goal"
    const val PREF_KEY_FINAL_GOAL_YEAR = "final_goal_year"

    const val DATABASE_NAME = "goal_reminder_database"
    const val TABLE_TODO = "todo"
    const val COL_TODO_ID = "todo_id"
    const val COL_START_TIME = "start_time"
    const val COL_END_TIME = "end_time"

    const val TABLE_GOAL = "goal"
    const val COL_GOAL_ID = "goal_id"
    const val COL_DATE_FROM = "date_from"
    const val COL_TOTAL_SCORE = "total_score"
    const val COL_CURRENT_SCORE = "current_score"
    const val COL_UPPER_GOAL_ID = "upper_goal_id"
    const val COL_UPPER_GOAL_CONTRIBUTION_SCORE = "upper_goal_contribution_score"
    const val COL_GOOGLE_CALENDAR_SYNC = "google_calendar_sync"
    const val COL_SERVER_SYNC = "server_sync"
}
