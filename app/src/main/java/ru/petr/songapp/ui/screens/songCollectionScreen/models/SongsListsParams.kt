package ru.petr.songapp.ui.screens.songCollectionScreen.models

import androidx.compose.ui.Modifier
import com.google.accompanist.pager.PagerState
import dev.wirespec.jetmagic.models.ComposableParams

open class SongsListsParams(
    modifier: Modifier,
    data: Any? = null,
    var onSongNameClick: (id:Int) -> Unit,
    var pagerState: PagerState,
    var songCollections: List<SongCollectionView>
) : ComposableParams(modifier = modifier, data = data)