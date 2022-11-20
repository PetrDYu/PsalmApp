package ru.petr.songapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import dev.wirespec.jetmagic.composables.ScreenFactoryHandler
import dev.wirespec.jetmagic.composables.crm
import dev.wirespec.jetmagic.navigation.navman
import kotlinx.coroutines.flow.flowOf
import ru.petr.songapp.data.models.songData.SongCollectionDBModel
import ru.petr.songapp.data.models.songData.dao.ShortSong
import ru.petr.songapp.data.repositories.utils.SongCollectionFlow
import ru.petr.songapp.ui.ComposableResourceIds
import ru.petr.songapp.ui.screens.songCollectionScreen.models.SongCollectionView
import ru.petr.songapp.ui.theme.SongAppTheme


class MainActivity : ComponentActivity() {

    /*private val songListViewModel: SongListViewModel by viewModels {
        SongListViewModelFactory((application as SongApp).songListRepository)
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        navman.activity = this

        if (navman.totalScreensDisplayed == 0) {
            navman.goto(composableResId = ComposableResourceIds.SongCollectionsScreen)
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

    override fun onBackPressed() {
        if(!navman.goBack())
            super.onBackPressed()
    }

    override fun onDestroy() {
        crm.onConfigurationChanged()
        super.onDestroy()
    }
}

val songs = listOf(
    ShortSong(1, 1, "Близко Господь и день Его"),
    ShortSong(2, 2, "Хвалите Господа"),
    ShortSong(3, 3, "Только в Господе моя радость"),
    ShortSong(4, 4, "Господу хвала и имени Его"),
    ShortSong(5, 5, "О, Господи, Ты мой свет"),
    ShortSong(6, 6, "Услыши меня"),
    ShortSong(7, 7, "Будьте здесь"),
    ShortSong(8, 8, "Поклоняемся Тебе"),
    ShortSong(9, 9, "Magnificat"),
    ShortSong(10, 10, "Слава в вышних"),
    ShortSong(11, 11, "Мне с Тобой не страшно"),
    ShortSong(12, 12, "В ночи, блуждая"),
    ShortSong(13, 13, "Даруй, о, Господи"),
    ShortSong(14, 14, "О, Господь Иисус, куда"),
    ShortSong(15, 15, "В Тебе моя душа находит"),
    ShortSong(16, 16, "О, Господь Иисус, Ты")
)

val songCollections = listOf(
    SongCollectionView(SongCollectionDBModel(1, "Будем петь и славить", "БПС"), songs.toMutableStateList()),
    SongCollectionView(SongCollectionDBModel(2, "Будем петь и славить", "БПС"), songs.toMutableStateList()),
    SongCollectionView(SongCollectionDBModel(3, "Будем петь и славить", "БПС"), songs.toMutableStateList()),
    SongCollectionView(SongCollectionDBModel(4, "Будем петь и славить", "БПС"), songs.toMutableStateList()),
    SongCollectionView(SongCollectionDBModel(5, "Будем петь и славить", "БПС"), songs.toMutableStateList()),
    SongCollectionView(SongCollectionDBModel(6, "Будем петь и славить", "БПС"), songs.toMutableStateList())
)

