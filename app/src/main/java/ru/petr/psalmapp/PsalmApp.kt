package ru.petr.psalmapp

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import dev.wirespec.jetmagic.composables.crm
import dev.wirespec.jetmagic.initializeJetmagic
import dev.wirespec.jetmagic.models.ComposableResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ru.petr.psalmapp.data.models.PsalmAppDB
import ru.petr.psalmapp.data.repositories.PsalmCollectionsRepository
import ru.petr.psalmapp.data.repositories.PsalmRepository
import ru.petr.psalmapp.data.repositories.PsalmsByCollectionsRepository
import ru.petr.psalmapp.ui.ComposableResourceIds
import ru.petr.psalmapp.ui.screens.psalmCollectionScreen.PsalmCollectionScreenHandler
import ru.petr.psalmapp.ui.screens.psalmCollectionScreen.PsalmListViewModel
import ru.petr.psalmapp.ui.screens.psalmCollectionScreen.PsalmListViewModelFactory
import ru.petr.psalmapp.ui.screens.psalmCollectionScreen.PsalmsListsHandler
import ru.petr.psalmapp.ui.screens.psalmScreen.PsalmScreenHandler
import ru.petr.psalmapp.ui.screens.psalmScreen.PsalmViewHandler
import ru.petr.psalmapp.ui.screens.psalmScreen.PsalmViewViewModel
import ru.petr.psalmapp.ui.screens.psalmScreen.PsalmViewViewModelFactory
import java.util.*

class PsalmApp: Application() {
    val appScope = CoroutineScope(SupervisorJob())
    val database by lazy { PsalmAppDB.getDB(this, appScope) }
    val psalmCollectionsRepository by lazy { PsalmCollectionsRepository(database.PsalmCollectionDao()) }
    val psalmListRepository by lazy {
        PsalmsByCollectionsRepository(database.PsalmDao(), psalmCollectionsRepository)
    }
    val psalmRepository by lazy {
        PsalmRepository(database.PsalmDao())
    }

    companion object {
        lateinit var context: PsalmApp
        var appLocale = "ru"
    }

    fun setAppLocale(language: String) {
        appLocale = language

        // IMPORTANT: Always call applyConfigChangesNow after making any config changes
        // programmatically but before restarting the activity.
        // NOTE: The language change doesn't actually take effect until
        // the activity has been restarted. The app's language is
        // set in onActivityResumed.

        crm.applyConfigChangesNow()
        val act = context.currentActivity as Activity
        act.startActivity(Intent.makeRestartActivityTask(act.componentName))
    }

    private val activityLifecycleTracker: AppLifecycleTracker = AppLifecycleTracker()
    var currentActivity: ComponentActivity?
        get() = activityLifecycleTracker.currentActivity
        private set(value) {}

    override fun onCreate() {
        super.onCreate()

        context = this

        initializeJetmagic(this)
        Log.d("app", "init jetmagic")

        registerActivityLifecycleCallbacks(activityLifecycleTracker)

        crm.apply {
            addComposableResources(
                mutableListOf(
                    //Screens
                    ComposableResource(
                        resourceId = ComposableResourceIds.PsalmCollectionsScreen,
                    ) { composableInstance ->
//                        Log.d("app", "call main screen handler")
                        PsalmCollectionScreenHandler(composableInstance = composableInstance)
                    },
                    ComposableResource(
                        resourceId = ComposableResourceIds.PsalmScreen,
                    ) { composableInstance ->
                        PsalmScreenHandler(composableInstance = composableInstance)
                    },

                    //Child composables
                    ComposableResource(
                        resourceId = ComposableResourceIds.PsalmsLists,
                        viewmodelClass = PsalmListViewModel::class.java,
                        onCreateViewmodel = {
                            val activity = currentActivity
                            if (activity != null) {
                                ViewModelProvider(activity, PsalmListViewModelFactory(psalmListRepository))[PsalmListViewModel::class.java]
                            } else {
                                PsalmListViewModel(psalmListRepository)
                            }
                        }
                    ) { composableInstance ->
//                        Log.d("app", "call main screen")
                        PsalmsListsHandler(composableInstance)
                    },
                    ComposableResource(
                        resourceId = ComposableResourceIds.PsalmView,
                        viewmodelClass = PsalmViewViewModel::class.java,
                        onCreateViewmodel = {
                            val activity = currentActivity
                            if (activity != null) {
                                ViewModelProvider(
                                    activity,
                                    PsalmViewViewModelFactory(psalmRepository)
                                )[PsalmViewViewModel::class.java]
                            } else {
                                PsalmViewViewModel(psalmRepository)
                            }
                        }
                    ) { composableInstance ->
                        PsalmViewHandler(composableInstance = composableInstance)
                    }
                )
            )
        }
    }

    /**
     * Callbacks for handling the lifecycle of activities.
     */
    class AppLifecycleTracker : ActivityLifecycleCallbacks {

        private var currentAct: ComponentActivity? = null

        var currentActivity: ComponentActivity?
            get() = currentAct
            private set(value) {}

        override fun onActivityCreated(activity: Activity, p1: Bundle?) {
        }

        override fun onActivityStarted(activity: Activity) {
        }

        override fun onActivityResumed(activity: Activity) {
            currentAct = activity as ComponentActivity
            context.resources.configuration.setLocale(Locale(appLocale))
            context.resources.updateConfiguration(context.resources.configuration, context.resources.displayMetrics)
        }

        override fun onActivityPaused(p0: Activity) {
        }

        override fun onActivityStopped(activity: Activity) {
            if ((currentAct != null) && (activity == currentAct))
                currentAct = null
        }

        override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
        }

        override fun onActivityDestroyed(p0: Activity) {
        }
    }
}