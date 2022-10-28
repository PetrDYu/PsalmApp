package ru.petr.songapp.ui.screens.songScreens.models.parsing

import org.junit.Assert
import org.junit.Test
import java.io.File

class FromTagToTagExtractorTest {
//    val mContent: String = File("C:\\Users\\petry\\YandexDisk\\Rabota\\MyProjects\\PsalmApp\\app\\src\\test\\java\\ru\\petr\\songapp\\ui\\screens\\songScreens\\models\\parsing\\282 Радуйтесь всегда, молитесь непрестанно.xml").readText()

    @Test
    fun `whenExtractingPartFromTagToTag with good text in multiple lines should return correct outputin multiple line`() {
        val testIn = File("C:\\Users\\petry\\YandexDisk\\Rabota\\MyProjects\\PsalmApp\\app\\src\\test\\java\\ru\\petr\\songapp\\ui\\screens\\songScreens\\models\\parsing\\test1_input.xml").readText()
        val correctTestOut = File("C:\\Users\\petry\\YandexDisk\\Rabota\\MyProjects\\PsalmApp\\app\\src\\test\\java\\ru\\petr\\songapp\\ui\\screens\\songScreens\\models\\parsing\\test1_output.xml").readText()
        val extractor = FromTagToTagExtractor()
        extractor.setStartPoint(3, 22)
        extractor.setEndPoint(12, 12)

        Assert.assertEquals(correctTestOut, extractor.extractPart(testIn))
    }

    @Test
    fun `whenExtractingPartFromTagToTag with good text in one line should return correct output in one line`() {
        val testIn = File("C:\\Users\\petry\\YandexDisk\\Rabota\\MyProjects\\PsalmApp\\app\\src\\test\\java\\ru\\petr\\songapp\\ui\\screens\\songScreens\\models\\parsing\\test2_input.xml").readText()
        val correctTestOut = File("C:\\Users\\petry\\YandexDisk\\Rabota\\MyProjects\\PsalmApp\\app\\src\\test\\java\\ru\\petr\\songapp\\ui\\screens\\songScreens\\models\\parsing\\test2_output.xml").readText()

        val extractor = FromTagToTagExtractor()
        extractor.setStartPoint(1, 212)
        extractor.setEndPoint(1, 523)

        Assert.assertEquals(correctTestOut, extractor.extractPart(testIn))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `whenExtractingPartFromTagToTag with startLine greater than endLine should throw IllegalArgumentException`() {

        val extractor = FromTagToTagExtractor()
        extractor.setStartPoint(2, 4)
        extractor.setEndPoint(1, 5)

        extractor.extractPart("")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `whenExtractingPartFromTagToTag with startLine = 0 should throw IllegalArgumentException`(){
        val extractor = FromTagToTagExtractor()
        extractor.setStartPoint(0, 1)
        extractor.setEndPoint(1, 1)
        extractor.extractPart("")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `whenExtractingPartFromTagToTag with startColumn = 0 should throw IllegalArgumentException`(){
        val extractor = FromTagToTagExtractor()
        extractor.setStartPoint(1, 0)
        extractor.setEndPoint(1, 1)
        extractor.extractPart("")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `whenExtractingPartFromTagToTag with endLine = 0 should throw IllegalArgumentException`(){
        val extractor = FromTagToTagExtractor()
        extractor.setStartPoint(1, 1)
        extractor.setEndPoint(0, 1)
        extractor.extractPart("")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `whenExtractingPartFromTagToTag with endColumn = 0 should throw IllegalArgumentException`(){
        val extractor = FromTagToTagExtractor()
        extractor.setStartPoint(1, 1)
        extractor.setEndPoint(1, 0)
        extractor.extractPart("")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `whenExtractingPartFromTagToTag with empty string should throw IllegalArgumentException`(){
        val extractor = FromTagToTagExtractor()
        extractor.setStartPoint(1, 1)
        extractor.setEndPoint(1, 1)
        extractor.extractPart("")
    }
}