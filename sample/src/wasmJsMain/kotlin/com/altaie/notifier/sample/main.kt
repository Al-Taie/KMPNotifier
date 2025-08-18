package com.altaie.notifier.sample

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    onApplicationStartPlatformSpecific()
    CanvasBasedWindow(canvasElementId = "ComposeTarget") { App() }
}