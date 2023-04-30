package ru.petr.songapp.ui.screens.songScreens.models

data class SongSettings(
    val songFontSize: Int,
    val proModeIsActive: Boolean,

    val setSongFontSize: (newFontSize: Int)->Unit
)
