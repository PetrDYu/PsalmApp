package ru.petr.psalmapp

import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.get
import com.google.accompanist.pager.*
import dev.wirespec.jetmagic.composables.ScreenFactoryHandler
import dev.wirespec.jetmagic.composables.crm
import dev.wirespec.jetmagic.models.ComposableInstance
import dev.wirespec.jetmagic.models.ComposableResource
import dev.wirespec.jetmagic.navigation.navman
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import ru.petr.psalmapp.data.models.psalm_data.PsalmCollection
import ru.petr.psalmapp.data.models.psalm_data.dao.ShortPsalm
import ru.petr.psalmapp.data.repositories.utils.PsalmCollectionView
import ru.petr.psalmapp.ui.theme.PsalmAppTheme


class MainActivity : ComponentActivity() {

    private val psalmListViewModel: PsalmListViewModel by viewModels {
        PsalmListViewModelFactory((application as PsalmApp).psalmListRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        crm.apply {
            addComposableResources(
                mutableListOf(
                    ComposableResource(
                        resourceId = "main_screen",
                        viewmodelClass = PsalmListViewModel::class.java,
                        onCreateViewmodel = {
                            Log.d("app", "create psalm list viewmodel")
                            val psalmListViewModel: PsalmListViewModel =
                                ViewModelProvider(this@MainActivity, PsalmListViewModelFactory((application as PsalmApp).psalmListRepository))[PsalmListViewModel::class.java]
                            /*val psalmListViewModel: PsalmListViewModel by viewModels {
                                PsalmListViewModelFactory((application as PsalmApp).psalmListRepository)
                            }*/
                            Log.d("app", "created psalm list viewmodel")
                            psalmListViewModel
                        }
                    ) { composableInstance ->
                        Log.d("app", "call main screen")
                        Main(composableInstance)
                    }
                )
            )
        }

        navman.activity = this

        if (navman.totalScreensDisplayed == 0) {
            navman.goto(composableResId = "main_screen_handler")
        }

        setContent {
            PsalmAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Log.d("main_screen", "screen factory start")
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

@Composable
fun MainHandler(composableInstance: ComposableInstance) {
    Log.d("main_screen", "main handler start")
    crm.RenderChildComposable(
        parentComposableId = composableInstance.id,
        composableResId = "main_screen",
        childComposableId = "main_screen",
        p = composableInstance.parameters
    )
}

@Composable
fun Main(composableInstance: ComposableInstance){
    val scaffoldState = rememberScaffoldState()
    Log.d("main_screen", "create scaffoldState")
    val vm = composableInstance.viewmodel as PsalmListViewModel
    Log.d("main_screen", "create vm")
    val psalmCollections = vm.psalmsByCollections.observeAsState().value
    Log.d("main_screen", "create psalmCollections")


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {TopAppBar(
            title = {Text("Сборники")},
            elevation = 10.dp
        )},
    ) {
        if (psalmCollections.isNullOrEmpty()) {
            Text("Ни одного сборника ещё не добавлено")
        } else {
            Column {
                val pagerState = rememberPagerState(pageCount = psalmCollections.size)
                TabsContent(Modifier.weight(1f), pagerState = pagerState, psalmCollections = psalmCollections)
                Tabs(tabs = psalmCollections, pagerState = pagerState)
                SearchPsalmBar(/*Modifier.weight(0f)*/)
            }
        }
    }
}

@Composable
fun PsalmList( psalms: List<ShortPsalm>){
    if (psalms.isEmpty()){
        Text("В сборник не добавлено ни одного псалма")
    } else {
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 0.dp)
        ) {
            items(psalms.size) { index ->
                Text(
                    "${psalms[index].NumberInCollection}. ${psalms[index].Name}",
                    Modifier
                        .clickable { }
                        .padding(vertical = 10.dp)
                        .fillMaxWidth(),
                    fontSize = 20.sp
                )
                if (index != psalms.size - 1)
                    Divider()
            }
        }
    }
}

@Composable
fun TabsContent(modifier: Modifier = Modifier, pagerState: PagerState, psalmCollections: List<PsalmCollectionView>) {
    HorizontalPager(state = pagerState, modifier) { pageNumber ->
        var psalmList = psalmCollections[pageNumber].psalms.asLiveData().observeAsState().value
        if (psalmList.isNullOrEmpty()){
            psalmList = listOf()
        }
        PsalmList(psalms = psalmList)
    }
}

@Composable
fun Tabs(tabs: List<PsalmCollectionView>, pagerState: PagerState) {
    val scope = rememberCoroutineScope()
    // OR ScrollableTabRow()
    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        contentColor = Color.White,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }) {
        tabs.forEachIndexed { index, tab ->
            // OR Tab()
            LeadingIconTab(
                icon = {},
                text = { Text(tab.psalmCollection.shortName) },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TabsPreview() {
    val pagerState = rememberPagerState(pageCount = psalmCollections.size)
    Tabs(tabs = psalmCollections, pagerState = pagerState)
}

@Composable
fun SearchPsalmBar(modifier: Modifier = Modifier) {
    var newText by remember {mutableStateOf("")}
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        TextField(
            value = newText,
            onValueChange = {data -> newText = data},
            placeholder = {Text(text = "Название или номер псалма")},
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .weight(1f),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White)
        )
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                Modifier
                    .size(35.dp)
                    .padding(end = 6.dp),
                tint = Color.Blue
            )
        }
    }

}

@Composable
fun MainTopBar(name: String) {
    TopAppBar() {
        Text(text = name)
    }
}

@Composable
fun NavBottomBar(){
    BottomAppBar() {

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PsalmAppTheme {
        Main(ComposableInstance("123", composableResId = "main_screen"))
    }
}

@Preview(
    name="PsalmList",
    showBackground = true,
//    heightDp = 50,
    uiMode = UI_MODE_TYPE_NORMAL
)
@Composable
fun PsalmListPreview(){
    PsalmAppTheme {
        PsalmList(psalms = psalms)
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview(){
    SearchPsalmBar()
}