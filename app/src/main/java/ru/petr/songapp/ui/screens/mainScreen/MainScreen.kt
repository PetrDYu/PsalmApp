package ru.petr.songapp.ui.screens.mainScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.wirespec.jetmagic.models.ComposableInstance

@Composable
fun MainScreenHandler(composableInstance: ComposableInstance) {
    val vm = composableInstance.viewmodel as MainScreenViewModel
    val allCollections by vm.allCollections.collectAsState()
    Scaffold { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            val collections = allCollections
            if (collections != null)
            {
                LazyColumn {
                    items(collections.size) {index ->
                        val collection = collections[index]
                        SongCollectionListItem(
                                name = collection.name,
                                marker = Icons.Default.AddCircle,
                                markerColor = Color(0xFFCF6741)
                        ) { vm.goToSongCollection(collection.id) }
                    }
                }
            } else {

            }

        }
    }
}

@Composable
fun SongCollectionListItem(modifier: Modifier = Modifier,
                           name: String,
                           marker: ImageVector,
                           markerColor: Color,
                           onClick: ()->Unit
) {
    Card(modifier = modifier.fillMaxSize(),
         onClick = onClick
    ) {
        Row (Modifier.padding(vertical = 10.dp, horizontal = 5.dp)) {
            Icon(
                    modifier = Modifier.padding(all = 5.dp),
                    imageVector = marker,
                    contentDescription = null,
                    tint = markerColor
            )
            Text(name.uppercase(), Modifier.padding(all = 5.dp))
        }
    }
}