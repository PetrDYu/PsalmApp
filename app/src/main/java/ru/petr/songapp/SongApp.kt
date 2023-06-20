package ru.petr.songapp

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
import ru.petr.songapp.data.models.datastore.settings.SettingsStore
import ru.petr.songapp.data.models.room.SongAppDB
import ru.petr.songapp.data.repositories.SettingsRepository
import ru.petr.songapp.data.repositories.SongCollectionsRepository
import ru.petr.songapp.data.repositories.SongRepository
import ru.petr.songapp.data.repositories.SongsByCollectionsRepository
import ru.petr.songapp.ui.ComposableResourceIds
import ru.petr.songapp.ui.screens.mainScreen.MainScreenHandler
import ru.petr.songapp.ui.screens.mainScreen.MainScreenViewModel
import ru.petr.songapp.ui.screens.mainScreen.MainScreenViewModelFactory
import ru.petr.songapp.ui.screens.songCollectionScreen.*
import ru.petr.songapp.ui.screens.songScreens.songEditorScreen.SongEditorHandler
import ru.petr.songapp.ui.screens.songScreens.songEditorScreen.SongEditorViewModel
import ru.petr.songapp.ui.screens.songScreens.songEditorScreen.SongEditorViewModelFactory
import ru.petr.songapp.ui.screens.songScreens.SongScreenHandler
import ru.petr.songapp.ui.screens.songScreens.SongScreenViewModel
import ru.petr.songapp.ui.screens.songScreens.SongScreenViewModelFactory
import ru.petr.songapp.ui.screens.songScreens.songViewerScreen.SongViewerHandler
import ru.petr.songapp.ui.screens.songScreens.songViewerScreen.SongViewerViewModel
import ru.petr.songapp.ui.screens.songScreens.songViewerScreen.SongViewerViewModelFactory
import ru.petr.songapp.ui.screens.startScreen.StartScreenHandler
import java.util.*

class SongApp: Application() {
    private val appScope = CoroutineScope(SupervisorJob())
    private val database by lazy { SongAppDB.getDB(this, appScope) }
    private val songCollectionsRepository by lazy { SongCollectionsRepository(database.SongCollectionDao()) }
    private val songListRepository by lazy {
        SongsByCollectionsRepository(database.SongDao(), songCollectionsRepository)
    }
    private val songRepository by lazy {
        SongRepository(database.SongDao())
    }

    private val settingsStore by lazy { SettingsStore(this) }
    private val settingsRepository by lazy { SettingsRepository(settingsStore) }

    companion object {
        lateinit var context: SongApp
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
                //Screens
                mutableListOf(
                    // Main Screens
                    ComposableResource(
                        resourceId = ComposableResourceIds.StartScreen,
                    ) { composableInstance ->
                        StartScreenHandler(composableInstance)
                    },
                    ComposableResource(
                            resourceId = ComposableResourceIds.MainScreen,
                            viewmodelClass = MainScreenViewModel::class.java,
                            onCreateViewmodel = {
                                val activity = currentActivity
                                if (activity != null) {
                                    ViewModelProvider(activity,
                                                      MainScreenViewModelFactory(songCollectionsRepository)
                                    )[MainScreenViewModel::class.java]
                                } else {
                                    MainScreenViewModel(songCollectionsRepository)
                                }
                            }
                    ) {composableInstance ->
                      MainScreenHandler(composableInstance)
                    },
                    ComposableResource(
                        resourceId = ComposableResourceIds.SongCollectionsScreen,
                        viewmodelClass = SongListViewModel::class.java,
                        onCreateViewmodel = {
                            val activity = currentActivity
                            if (activity != null) {
                                ViewModelProvider(activity,
                                                  SongListViewModelFactory(songListRepository)
                                )[SongListViewModel::class.java]
                            } else {
                                SongListViewModel(songListRepository)
                            }
                        }
                    ) { composableInstance ->
//                        Log.d("app", "call main screen handler")
                        SongCollectionScreenHandler(composableInstance = composableInstance)
                    },
                    ComposableResource(
                        resourceId = ComposableResourceIds.SongScreen,
                        viewmodelClass = SongScreenViewModel::class.java,
                        onCreateViewmodel = {
                            val activity = currentActivity
                            if (activity != null) {
                                ViewModelProvider(
                                    activity,
                                    SongScreenViewModelFactory(songRepository, settingsRepository)
                                )[SongScreenViewModel::class.java]
                            } else {
                                SongScreenViewModel(songRepository, settingsRepository)
                            }
                        }
                    ) { composableInstance ->
                        SongScreenHandler(composableInstance = composableInstance)
                    },

                    //Child composables
                    ComposableResource(
                        resourceId = ComposableResourceIds.SongsLists
                    ) { composableInstance ->
//                        Log.d("app", "call main screen")
                        SongsListsHandler(composableInstance)
                    },
                    ComposableResource(
                        resourceId = ComposableResourceIds.SearchSongs
                    ) {
                      SearchSongsListHandler(composableInstance = it)
                    },
                    ComposableResource(
                        resourceId = ComposableResourceIds.SongViewer,
                        viewmodelClass = SongViewerViewModel::class.java,
                        onCreateViewmodel = {
                            val activity = currentActivity
                            if (activity != null) {
                                ViewModelProvider(
                                    activity,
                                    SongViewerViewModelFactory()
                                )[SongViewerViewModel::class.java]
                            } else {
                                SongViewerViewModel()
                            }
                        }
                    ) { composableInstance ->
                        SongViewerHandler(composableInstance = composableInstance)
                    },
                    ComposableResource(
                        resourceId = ComposableResourceIds.SongEditor,
                        viewmodelClass = SongEditorViewModel::class.java,
                        onCreateViewmodel = {
                            val activity = currentActivity
                            if (activity != null) {
                                ViewModelProvider(
                                    activity,
                                    SongEditorViewModelFactory(songRepository, settingsRepository)
                                )[SongEditorViewModel::class.java]
                            } else {
                                SongEditorViewModel(songRepository, settingsRepository)
                            }
                        }
                    ) { composableInstance ->
                        SongEditorHandler(composableInstance = composableInstance)
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