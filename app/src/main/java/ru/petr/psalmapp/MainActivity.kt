package ru.petr.psalmapp

import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.*
import dev.wirespec.jetmagic.composables.ScreenFactoryHandler
import dev.wirespec.jetmagic.composables.crm
import dev.wirespec.jetmagic.models.ComposableInstance
import dev.wirespec.jetmagic.navigation.navman
import kotlinx.coroutines.launch
import ru.petr.psalmapp.ui.theme.PsalmAppTheme
import java.time.format.TextStyle


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    "Близко Господь и день Его",
    "Хвалите Господа",
    "Только в Господе моя радость",
    "Господу хвала и имени Его",
    "О, Господи, Ты мой свет",
    "Услыши меня",
    "Будьте здесь",
    "Поклоняемся Тебе",
    "Magnificat",
    "Слава в вышних",
    "Мне с Тобой не страшно",
    "В ночи, блуждая",
    "Даруй, о, Господи",
    "О, Господь Иисус, куда",
    "В Тебе моя душа находит",
    "О, Господь Иисус, Ты"
)

val psalmCollections = listOf(
    PsalmCollection("Будем петь и славить", "БПиС", psalms),
    PsalmCollection("Будем петь и славить", "БПиС", psalms),
    PsalmCollection("Будем петь и славить", "БПиС", psalms),
    PsalmCollection("Будем петь и славить", "БПиС", psalms),
    PsalmCollection("Будем петь и славить", "БПиС", psalms),
    PsalmCollection("Будем петь и славить", "БПиС", psalms)
)

@Composable
fun MainHandler(composableInstance: ComposableInstance) {
    crm.RenderChildComposable(
        parentComposableId = composableInstance.id,
        composableResId = "main_screen",
        childComposableId = "main_screen",
        p = composableInstance.parameters
    )
}

@Composable
fun Main(){
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {TopAppBar(
            title = {Text("Сборники")},
            elevation = 10.dp
        )},
    ) {
        Column {
            val pagerState = rememberPagerState(pageCount = psalmCollections.size)
            TabsContent(Modifier.weight(1f), pagerState = pagerState)
            Tabs(tabs = psalmCollections, pagerState = pagerState)
            SearchPsalmBar(/*Modifier.weight(0f)*/)
        }
    }
}

@Composable
fun PsalmList( psalms: List<String>){
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 0.dp)
    ) {
        items(psalms.size) {index ->
            Text(
                "${index+1}. ${psalms[index]}",
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

@Composable
fun TabsContent(modifier: Modifier = Modifier, pagerState: PagerState) {
    HorizontalPager(state = pagerState, modifier) { pageNumber ->
        PsalmList(psalms = psalmCollections[pageNumber].psalms)
    }
}

@Composable
fun Tabs(tabs: List<PsalmCollection>, pagerState: PagerState) {
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
                text = { Text(tab.shortCollectionName) },
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
        Main()
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