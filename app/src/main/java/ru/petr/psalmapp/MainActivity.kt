package ru.petr.psalmapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import dev.wirespec.jetmagic.composables.ScreenFactoryHandler
import dev.wirespec.jetmagic.composables.crm
import dev.wirespec.jetmagic.models.ComposableResource
import dev.wirespec.jetmagic.navigation.navman
import kotlinx.coroutines.flow.flowOf
import ru.petr.psalmapp.data.models.psalm_data.PsalmCollection
import ru.petr.psalmapp.data.models.psalm_data.dao.ShortPsalm
import ru.petr.psalmapp.data.repositories.utils.PsalmCollectionView
import ru.petr.psalmapp.ui.ComposableResourceIds
import ru.petr.psalmapp.ui.screens.psalmCollectionScreen.PsalmCollectionScreenHandler
import ru.petr.psalmapp.ui.screens.psalmCollectionScreen.PsalmListViewModel
import ru.petr.psalmapp.ui.screens.psalmCollectionScreen.PsalmListViewModelFactory
import ru.petr.psalmapp.ui.screens.psalmCollectionScreen.PsalmsListsHandler
import ru.petr.psalmapp.ui.screens.psalmScreen.PsalmScreenHandler
import ru.petr.psalmapp.ui.screens.psalmScreen.PsalmViewHandler
import ru.petr.psalmapp.ui.screens.psalmScreen.PsalmViewViewModel
import ru.petr.psalmapp.ui.screens.psalmScreen.PsalmViewViewModelFactory
import ru.petr.psalmapp.ui.theme.PsalmAppTheme


class MainActivity : ComponentActivity() {

    /*private val psalmListViewModel: PsalmListViewModel by viewModels {
        PsalmListViewModelFactory((application as PsalmApp).psalmListRepository)
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        navman.activity = this

        if (navman.totalScreensDisplayed == 0) {
            navman.goto(composableResId = ComposableResourceIds.PsalmCollectionsScreen)
        }

        setContent {
            PsalmAppTheme {
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

val psalms = listOf(
    ShortPsalm(1, 1, "Близко Господь и день Его"),
    ShortPsalm(2, 2, "Хвалите Господа"),
    ShortPsalm(3, 3, "Только в Господе моя радость"),
    ShortPsalm(4, 4, "Господу хвала и имени Его"),
    ShortPsalm(5, 5, "О, Господи, Ты мой свет"),
    ShortPsalm(6, 6, "Услыши меня"),
    ShortPsalm(7, 7, "Будьте здесь"),
    ShortPsalm(8, 8, "Поклоняемся Тебе"),
    ShortPsalm(9, 9, "Magnificat"),
    ShortPsalm(10, 10, "Слава в вышних"),
    ShortPsalm(11, 11, "Мне с Тобой не страшно"),
    ShortPsalm(12, 12, "В ночи, блуждая"),
    ShortPsalm(13, 13, "Даруй, о, Господи"),
    ShortPsalm(14, 14, "О, Господь Иисус, куда"),
    ShortPsalm(15, 15, "В Тебе моя душа находит"),
    ShortPsalm(16, 16, "О, Господь Иисус, Ты")
)

val psalmCollections = listOf(
    PsalmCollectionView(PsalmCollection(1, "Будем петь и славить", "БПС"), flowOf(psalms)),
    PsalmCollectionView(PsalmCollection(2, "Будем петь и славить", "БПС"), flowOf(psalms)),
    PsalmCollectionView(PsalmCollection(3, "Будем петь и славить", "БПС"), flowOf(psalms)),
    PsalmCollectionView(PsalmCollection(4, "Будем петь и славить", "БПС"), flowOf(psalms)),
    PsalmCollectionView(PsalmCollection(5, "Будем петь и славить", "БПС"), flowOf(psalms)),
    PsalmCollectionView(PsalmCollection(6, "Будем петь и славить", "БПС"), flowOf(psalms))
)

