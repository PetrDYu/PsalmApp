import unittest
from Psalm import Psalm
import xml.etree.ElementTree as xml
from io import BytesIO


class TestPsalmToXML(unittest.TestCase):
    psalm = Psalm("А весна пришла такая нежная", 1431)
    psalm.verses = [
        ["А весна пришла такая нежная, ", "Были слышны так ее шаги. ",
         '"Здравствуй!" - ей сказал подснежник вежливо, ', 'Поднимая к небу лепестки.'],
        ["Ожили и птицы, и растения. ", "Реки пробуждаются от сна. ", "Почему же ты стоишь растерянно? ",
         "Посмотри, уже пришла весна."],
        ['Посмотри, какое небо синее. ', 'Солнца луч тебе желает: "Встань, ',
         'И, стряхнув с себя сонливость зимнюю. ', 'Прославляй Спасителя Христа!"']
    ]
    psalm.choruses = {
        1: ['Весна вновь пришла, ', 'посмотри вокруг. ', 'А ты от Христа далеко, мой друг.'],
        3: ['Весна вновь пришла. ', 'Посмотри вокруг. ', 'Иди ко Христу поскорее, друг.']
    }

    def test_full(self):
        with open('1431.xml', 'r', encoding='utf-8') as f:
            res = f.read()
        elem = self.psalm.get_xml()
        doc = xml.ElementTree(elem)
        xml.indent(doc, '    ')

        f = BytesIO()
        doc.write(f, encoding='utf-8', xml_declaration=True)
        self.assertEqual(res, f.getvalue().decode('utf-8'))