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
import ru.petr.songapp.data.models.SongAppDB
import ru.petr.songapp.data.repositories.SongCollectionsRepository
import ru.petr.songapp.data.repositories.SongRepository
import ru.petr.songapp.data.repositories.SongsByCollectionsRepository
import ru.petr.songapp.ui.ComposableResourceIds
import ru.petr.songapp.ui.screens.songCollectionScreen.SongCollectionScreenHandler
import ru.petr.songapp.ui.screens.songCollectionScreen.SongListViewModel
import ru.petr.songapp.ui.screens.songCollectionScreen.SongListViewModelFactory
import ru.petr.songapp.ui.screens.songCollectionScreen.SongsListsHandler
import ru.petr.songapp.ui.screens.songScreens.songViewerScreen.SongScreenHandler
import ru.petr.songapp.ui.screens.songScreens.songViewerScreen.SongViewHandler
import ru.petr.songapp.ui.screens.songScreens.songViewerScreen.SongViewViewModel
import ru.petr.songapp.ui.screens.songScreens.songViewerScreen.SongViewViewModelFactory
import java.util.*

class SongApp: Application() {
    val appScope = CoroutineScope(SupervisorJob())
    val database by lazy { SongAppDB.getDB(this, appScope) }
    val mSongCollectionsRepository by lazy { SongCollectionsRepository(database.SongCollectionDao()) }
    val songListRepository by lazy {
        SongsByCollectionsRepository(database.SongDao(), mSongCollectionsRepository)
    }
    val mSongRepository by lazy {
        SongRepository(database.SongDao())
    }

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
                mutableListOf(
                    //Screens
                    ComposableResource(
                        resourceId = ComposableResourceIds.SongCollectionsScreen,
                    ) { composableInstance ->
//                        Log.d("app", "call main screen handler")
                        SongCollectionScreenHandler(composableInstance = composableInstance)
                    },
                    ComposableResource(
                        resourceId = ComposableResourceIds.SongScreen,
                    ) { composableInstance ->
                        SongScreenHandler(composableInstance = composableInstance)
                    },

                    //Child composables
                    ComposableResource(
                        resourceId = ComposableResourceIds.SongsLists,
                        viewmodelClass = SongListViewModel::class.java,
                        onCreateViewmodel = {
                            val activity = currentActivity
                            if (activity != null) {
                                ViewModelProvider(activity, SongListViewModelFactory(songListRepository))[SongListViewModel::class.java]
                            } else {
                                SongListViewModel(songListRepository)
                            }
                        }
                    ) { composableInstance ->
//                        Log.d("app", "call main screen")
                        SongsListsHandler(composableInstance)
                    },
                    ComposableResource(
                        resourceId = ComposableResourceIds.SongView,
                        viewmodelClass = SongViewViewModel::class.java,
                        onCreateViewmodel = {
                            val activity = currentActivity
                            if (activity != null) {
                                ViewModelProvider(
                                    activity,
                                    SongViewViewModelFactory(mSongRepository)
                                )[SongViewViewModel::class.java]
                            } else {
                                SongViewViewModel(mSongRepository)
                            }
                        }
                    ) { composableInstance ->
                        SongViewHandler(composableInstance = composableInstance)
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