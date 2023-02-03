package ru.petr.songapp.ui.screens.songScreens.songEditorScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import dev.wirespec.jetmagic.models.ComposableInstance
import kotlinx.coroutines.launch
import ru.petr.songapp.R
import ru.petr.songapp.ui.screens.songScreens.models.Song
import ru.petr.songapp.ui.screens.songScreens.models.SongParams
import ru.petr.songapp.ui.screens.songScreens.models.SongShowTypes
import ru.petr.songapp.ui.screens.songScreens.models.SongView

@Composable
fun SongEditorHandler(composableInstance: ComposableInstance) {
    val scope = rememberCoroutineScope()

    val vm = composableInstance.viewmodel as SongEditorViewModel
    val p = composableInstance.parameters as SongParams
    val song by vm.getSongById(p.songId).observeAsState()

    val fontSize = vm.fontSize.collectAsState()
    val proModeIsActive = vm.proModeIsActive.collectAsState()
    
    val settingsSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    Row(Modifier.horizontalScroll(rememberScrollState())){
        Text("111111111")
        TextField(value = "long1 long1 long1 long1 long1 long1 long1 long1 long1 long1 long1 long1 long1 long1 long1 ", onValueChange = {})
        Text("1111111")
    }
    /*ModalBottomSheetLayout(
        sheetContent = {
            SettingsSheetContent(fontSize.value) { newSize ->
                vm.saveFontSizeSetting(newSize)
            }
        },
        sheetState = settingsSheetState,
        sheetShape = RoundedCornerShape(20.dp),
        sheetElevation = 50.dp,
    ) {
        ConstraintLayout() {
            val (viewer, settingsButton, editButton) = createRefs()
            SongViewer(
                Modifier.constrainAs(viewer) {
                    top.linkTo(parent.top, margin = 20.dp)
                    bottom.linkTo(parent.bottom, margin = 20.dp)
                    start.linkTo(parent.start, margin = 30.dp)
                    end.linkTo(parent.end, margin = 30.dp)
                },
                song = song,
                fontSize = fontSize.value,
            )

            FloatingActionButton(
                onClick = {
                    scope.launch {
                        settingsSheetState.show()
                    }
                },
                Modifier.constrainAs(settingsButton) {
                    end.linkTo(parent.end, margin = 30.dp)
                    bottom.linkTo(parent.bottom, margin = 30.dp)
                },
            ) {
                Icon(Icons.Default.Settings, stringResource(id = R.string.settings_button_description))
            }

            if (proModeIsActive.value) {
                FloatingActionButton(
                    onClick = {

                    },
                    Modifier.constrainAs(editButton) {
                        start.linkTo(parent.start, margin = 30.dp)
                        bottom.linkTo(parent.bottom, margin = 30.dp)
                    },
                ) {
                    Icon(Icons.Default.Edit, stringResource(id = R.string.edit_button_description))
                }
            }
        }
    }*/
    
}

@Composable
fun SettingsSheetContent(fontSize: Int, saveFontSize: (newSize: Int) -> Unit) {
    val maxFontSize = integerResource(id = R.integer.max_font_size)
    val minFontSize = integerResource(id = R.integer.min_font_size)
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        SongFontSizeSettingField(fontSize, minFontSize, maxFontSize) { newSize ->
            saveFontSize(newSize)
        }
    }
}

@Composable
fun SongFontSizeSettingField(fontSize: Int, minFontSize: Int, maxFontSize: Int, onFontSizeChange: (newSize: Int) -> Unit) {
    var expanded by remember{ mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    Column() {
        Text(
            modifier = Modifier.padding(top = 5.dp),
            text = stringResource(id = R.string.font_size_setting_label),
            color = Color.Gray
        )
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {expanded = !expanded}) {
            OutlinedTextField(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .clickable { expanded = true },
                value = fontSize.toString(),
                onValueChange = {},
                singleLine = true,
                readOnly = true,
                textStyle = TextStyle(fontSize = fontSize.sp),
//            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false; focusManager.clearFocus() }) {
                for (curFontSize in minFontSize..maxFontSize) {
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            onFontSizeChange(curFontSize)
                        }
                    ) {
                        Text(text = curFontSize.toString(), fontSize = curFontSize.sp)
                    }
                    if (curFontSize != maxFontSize)
                        Divider(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp, horizontal = 10.dp))
                }
            }
        } 
    }
}

@Composable
fun SongViewer(modifier: Modifier, song: Song?, fontSize: Int) {
    Box(modifier = modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
//        .horizontalScroll(rememberScrollState())
    ) {
        if (song == null) {
            Text("Ошибка в процессе загрузки")
        } else {
            SongView(showType = SongShowTypes.VIEW, song = song, fontSize = fontSize)
        }
    }
}