package com.altaie.notifier.sample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application


fun main() = application {
    AppInitializer.onApplicationStart()
    Window(
        onCloseRequest = ::exitApplication,
        title = "Notifier Desktop",
    ) {
        println("Desktop app is started")
        App()

    }
}