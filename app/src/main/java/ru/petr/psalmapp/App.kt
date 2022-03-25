package ru.petr.psalmapp

import android.app.Application
import dev.wirespec.jetmagic.composables.crm
import dev.wirespec.jetmagic.initializeJetmagic
import dev.wirespec.jetmagic.models.ComposableResource

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        initializeJetmagic(this)
        crm.apply {
            addComposableResources(
                mutableListOf(
                    ComposableResource(
                        resourceId = "main_screen_handler"
                    ) { composableInstance ->
                        MainHandler(composableInstance = composableInstance)
                    },
                    ComposableResource(
                        resourceId = "main_screen"
                    ) {
                        Main()
                    }
                )
            )
        }
    }
}