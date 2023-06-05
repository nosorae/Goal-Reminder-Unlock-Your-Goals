package com.yessorae.presentation

interface Destination {
    val route: String
}

object MainDestination : Destination {
    override val route = "main"
}


