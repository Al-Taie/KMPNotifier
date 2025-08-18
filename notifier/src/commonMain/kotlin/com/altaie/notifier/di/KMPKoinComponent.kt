package com.altaie.notifier.di

import org.koin.core.Koin
import org.koin.core.component.KoinComponent

internal abstract class KMPKoinComponent : KoinComponent {
    override fun getKoin(): Koin = LibDependencyInitializer.koinApp?.koin!!
}