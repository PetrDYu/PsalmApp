package ru.petr.psalmapp.ui.screens.psalmCollectionScreen

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.lifecycle.asLiveData
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import dev.wirespec.jetmagic.models.ComposableInstance
import kotlinx.coroutines.launch
import ru.petr.psalmapp.*
import ru.petr.psalmapp.data.models.psalm_data.dao.ShortPsalm
import ru.petr.psalmapp.data.repositories.utils.PsalmCollectionView
import ru.petr.psalmapp.ui.screens.psalmCollectionScreen.models.PsalmsListsParams
import ru.petr.psalmapp.ui.theme.PsalmAppTheme

@Composable
fun PsalmsListsHandler(composableInstance: ComposableInstance) {
    val vm = composableInstance.viewmodel as PsalmListViewModel
    Log.d("main_screen", "create vm")
    val psalmCollections = vm.psalmsByCollections.observeAsState().value
    Log.d("main_screen", "create psalmCollections")

    val p = composableInstance.parameters as PsalmsListsParams
    PsalmsLists(psalmCollections = psalmCollections, onChangeCollectionName = p.onChangeCollectionName) { id ->
        vm.updateOrGotoPsalm(
            id
        )
    }
}

@Composable
fun PsalmsLists(psalmCollections: List<PsalmCollectionView>?, onChangeCollectionName: (String)->Unit, onPsalmNameClick: (id:Int) -> Unit) {
    if (psalmCollections.isNullOrEmpty()) {
        Text("Ни одного сборника ещё не добавлено")
    } else {
        Column {
            val pagerState = rememberPagerState(pageCount = psalmCollections.size)
            onChangeCollectionName(psalmCollections[pagerState.currentPage].psalmCollection.name)
            TabsContent(Modifier.weight(1f), pagerState = pagerState, psalmCollections = psalmCollections, onPsalmNameClick = onPsalmNameClick)
            Tabs(tabs = psalmCollections, pagerState = pagerState)
            SearchPsalmBar(/*Modifier.weight(0f)*/)
        }
    }
}

@Composable
fun PsalmList( psalms: List<ShortPsalm>, onPsalmNameClick: (id:Int) -> Unit){
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
                        .clickable { onPsalmNameClick(psalms[index].Id) }
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
fun TabsContent(modifier: Modifier = Modifier, pagerState: PagerState, psalmCollections: List<PsalmCollectionView>, onPsalmNameClick: (id:Int) -> Unit) {
    HorizontalPager(state = pagerState, modifier) { pageNumber ->
        var psalmList = psalmCollections[pageNumber].psalms.asLiveData().observeAsState().value
        if (psalmList.isNullOrEmpty()){
            psalmList = listOf()
        }
        PsalmList(psalms = psalmList, onPsalmNameClick = onPsalmNameClick)
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
    var newText by remember { mutableStateOf("") }
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
//        Main(ComposableInstance("123", composableResId = "main_screen"))
    }
}

@Preview(
    name="PsalmList",
    showBackground = true,
//    heightDp = 50,
    uiMode = Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun PsalmListPreview(){
    PsalmAppTheme {
        PsalmList(psalms = psalms, {})
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview(){
    SearchPsalmBar()
}