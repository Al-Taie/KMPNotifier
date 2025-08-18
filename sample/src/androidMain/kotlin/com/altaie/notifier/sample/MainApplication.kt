package com.altaie.notifier.sample

import android.app.Application

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppInitializer.onApplicationStart()
    }
}