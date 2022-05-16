package ru.petr.psalmapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import dev.wirespec.jetmagic.composables.crm
import dev.wirespec.jetmagic.initializeJetmagic
import dev.wirespec.jetmagic.models.ComposableResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ru.petr.psalmapp.data.models.PsalmAppDB
import ru.petr.psalmapp.data.repositories.PsalmCollectionsRepository
import ru.petr.psalmapp.data.repositories.PsalmsByCollectionsRepository

class PsalmApp: Application() {
    val appScope = CoroutineScope(SupervisorJob())
    val database by lazy { PsalmAppDB.getDB(this, appScope) }
    val psalmCollectionsRepository by lazy { PsalmCollectionsRepository(database.PsalmCollectionDao()) }
    val psalmListRepository by lazy {
        PsalmsByCollectionsRepository(database.PsalmDao(), psalmCollectionsRepository)
    }
    override fun onCreate() {
        super.onCreate()
        initializeJetmagic(this)
        Log.d("app", "init jetmagic")
        crm.apply {
            addComposableResources(
                mutableListOf(
                    ComposableResource(
                        resourceId = "main_screen_handler"
                    ) { composableInstance ->
                        Log.d("app", "call main screen handler")
                        MainHandler(composableInstance = composableInstance)
                    },
                    // Этот ресурс создаётся в MainActivity
                    /*ComposableResource(
                        resourceId = "main_screen",
                        viewmodelClass = PsalmListViewModel::class.java,
                        onCreateViewmodel = {
                            Log.d("app", "create psalm list viewmodel")
//                            PsalmListViewModel(psalmListRepository)

                            ViewModelProvider(this, PsalmListViewModelFactory(psalmListRepository)).get(PsalmListViewModel::class.java)

                        }
                    ) { composableInstance ->
                        Log.d("app", "call main screen")
                        Main(composableInstance)
                    }*/
                )
            )
        }
    }
}