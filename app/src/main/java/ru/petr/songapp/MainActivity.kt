package ru.petr.songapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.core.os.BuildCompat
import dev.wirespec.jetmagic.composables.ScreenFactoryHandler
import dev.wirespec.jetmagic.composables.crm
import dev.wirespec.jetmagic.navigation.navman
import ru.petr.songapp.data.models.room.songData.SongCollectionDBModel
import ru.petr.songapp.data.models.room.songData.dao.SongDataForCollection
import ru.petr.songapp.ui.ComposableResourceIds
import ru.petr.songapp.ui.screens.songCollectionScreen.models.SongCollectionView
import ru.petr.songapp.ui.theme.SongAppTheme


class MainActivity : ComponentActivity() {

    /*private val songListViewModel: SongListViewModel by viewModels {
        SongListViewModelFactory((application as SongApp).songListRepository)
    }*/

    @BuildCompat.PrereleaseSdkCheck
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navman.activity = this

        if (navman.totalScreensDisplayed == 0) {
            navman.goto(composableResId = ComposableResourceIds.StartScreen)
        }

        if (BuildCompat.isAtLeastT()) {
//            onBackInvokedDispatcher.registerOnBackInvokedCallback(
//                OnBackInvokedDispatcher.PRIORITY_DEFAULT
//            ) {
//                mainBackPressedCallback()
//            }
            onBackPressedDispatcher.addCallback(this /* lifecycle owner */, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    mainBackPressedCallback()
                }
            })
        } else {
            onBackPressedDispatcher.addCallback(this /* lifecycle owner */, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    mainBackPressedCallback()
                }
            })
        }


        setContent {
            SongAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
//                    Log.d("main_screen", "screen factory start")
                    ScreenFactoryHandler()
                }
            }
        }
    }

    fun mainBackPressedCallback() {
        if(!navman.goBack())
            finish()
    }

    override fun onDestroy() {
        crm.onConfigurationChanged()
        super.onDestroy()
    }
}

val songs = listOf(
    SongDataForCollection(1, 1, "Близко Господь и день Его"),
    SongDataForCollection(2, 2, "Хвалите Господа"),
    SongDataForCollection(3, 3, "Только в Господе моя радость"),
    SongDataForCollection(4, 4, "Господу хвала и имени Его"),
    SongDataForCollection(5, 5, "О, Господи, Ты мой свет"),
    SongDataForCollection(6, 6, "Услыши меня"),
    SongDataForCollection(7, 7, "Будьте здесь"),
    SongDataForCollection(8, 8, "Поклоняемся Тебе"),
    SongDataForCollection(9, 9, "Magnificat"),
    SongDataForCollection(10, 10, "Слава в вышних"),
    SongDataForCollection(11, 11, "Мне с Тобой не страшно"),
    SongDataForCollection(12, 12, "В ночи, блуждая"),
    SongDataForCollection(13, 13, "Даруй, о, Господи"),
    SongDataForCollection(14, 14, "О, Господь Иисус, куда"),
    SongDataForCollection(15, 15, "В Тебе моя душа находит"),
    SongDataForCollection(16, 16, "О, Господь Иисус, Ты")
)

val songCollections = listOf(
    SongCollectionView(SongCollectionDBModel(1, "Будем петь и славить", "БПС"), songs.toMutableStateList()),
    SongCollectionView(SongCollectionDBModel(2, "Будем петь и славить", "БПС"), songs.toMutableStateList()),
    SongCollectionView(SongCollectionDBModel(3, "Будем петь и славить", "БПС"), songs.toMutableStateList()),
    SongCollectionView(SongCollectionDBModel(4, "Будем петь и славить", "БПС"), songs.toMutableStateList()),
    SongCollectionView(SongCollectionDBModel(5, "Будем петь и славить", "БПС"), songs.toMutableStateList()),
    SongCollectionView(SongCollectionDBModel(6, "Будем петь и славить", "БПС"), songs.toMutableStateList())
)

