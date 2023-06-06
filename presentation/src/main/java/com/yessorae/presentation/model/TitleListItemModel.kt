package com.yessorae.presentation.model

data class TitleListItemModel(
    val title: String
)

val mockTitleListItemModels = listOf(
    TitleListItemModel(title = "2023년 목표"),
    TitleListItemModel(title = "12월 목표"),
    TitleListItemModel(title = "1월 목표")
)