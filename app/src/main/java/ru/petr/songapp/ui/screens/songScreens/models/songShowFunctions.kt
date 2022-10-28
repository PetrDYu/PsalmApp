package ru.petr.songapp.ui.screens.songScreens.models

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.petr.songapp.data.models.songData.SongCollectionDBModel
import ru.petr.songapp.ui.screens.songScreens.models.songParts.SongPart
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.LineChunk
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.SongPartLine

@Composable
fun SongView(
    modifier: Modifier = Modifier,
    showType: SongShowTypes,
    song: Song,
) {
    Column(modifier) {
        for (part in song.mSongParts) {
            SongPartView(
                showType = showType,
                part = part
            )
        }
    }
}

@Composable
fun SongPartView(
    modifier: Modifier = Modifier,
    showType: SongShowTypes,
    part: SongPart,
) {
    when (part) {
        is SongPart.Chorus -> {
            ChorusView(
                showType = showType,
                chorus = part,
            )
        }
        is SongPart.Verse -> {
            VerseView(
                showType = showType,
                verse = part
            )
        }
        is SongPart.Bridge -> {
            BridgeView(
                showType = showType,
                bridge = part
            )
        }
    }
}

@Composable
fun ChorusView(
    modifier: Modifier = Modifier,
    showType: SongShowTypes,
    chorus: SongPart.Chorus,
) {
    Row(modifier) {
        Text("Припев:")
        Column {
            for (line in chorus.lines) {
                LineView(showType = showType, line = line)
            }
        }
    }
}

@Composable
fun VerseView(
    modifier: Modifier = Modifier,
    showType: SongShowTypes,
    verse: SongPart.Verse,
) {

}

@Composable
fun BridgeView(
    modifier: Modifier = Modifier,
    showType: SongShowTypes,
    bridge: SongPart.Bridge,
) {

}

@Composable
fun LineView(
    modifier: Modifier = Modifier,
    showType: SongShowTypes,
    line: SongPartLine,
) {
    Row(modifier) {
        for (chunk in line.chunks) {
            ChunkView(showType = showType, chunk = chunk)
        }
    }
}

@Composable
fun ChunkView(
    modifier: Modifier = Modifier,
    showType: SongShowTypes,
    chunk: LineChunk,
) {
    Text(chunk.text?.text?: "test")
}


@Preview
@Composable
fun SongPreview() {
    val songColl = SongCollectionDBModel(1, "Test", "Test")
//    val song = Song.getSong(, false, songColl)
//    SongView(showType = SongShowTypes.READ, song = )
}