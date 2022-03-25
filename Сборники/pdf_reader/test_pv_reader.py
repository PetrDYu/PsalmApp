import unittest

from Psalm import Psalm
from pv_reader import get_next_item_index, text_items, parse_text


class TestGetNextItemIndex(unittest.TestCase):
    text = "$#$Если грех твой, как пурпур, $#%Бог, как волну, убелит.%%Если грех мной багряным,"

    def test_cur_elem_is_last(self):
        cur_elem = len(self.text)
        self.assertEqual(get_next_item_index(self.text, cur_elem)[1], len(self.text))

    def test_cur_elem_is_gt_last(self):
        cur_elem = len(self.text) + 1
        self.assertEqual(get_next_item_index(self.text, cur_elem)[1], len(self.text))

    def test_cur_elem_is_lt_last(self):
        cur_elem = 33
        self.assertEqual(get_next_item_index(self.text, cur_elem), (2, 57))

    def test_cur_elem_is_lt_first(self):
        cur_elem = -1
        self.assertEqual(get_next_item_index(self.text, cur_elem), (0, 0))

    def test_cur_elem_is_0(self):
        cur_elem = 0
        self.assertEqual(get_next_item_index(self.text, cur_elem), (1, 31))

    def test_text_is_empty(self):
        text = ""
        cur_elem = 0
        self.assertEqual(get_next_item_index(text, cur_elem)[1], 0)

        cur_elem = 1
        self.assertEqual(get_next_item_index(text, cur_elem)[1], 0)

        cur_elem = -1
        self.assertEqual(get_next_item_index(text, cur_elem)[1], 0)


class TestTextItems(unittest.TestCase):
    text = "$#$Если грех твой, как пурпур, $#%Бог, как волну, убелит.%%Если грех мной багряным,"
    content = [i for i in text_items(text)]

    def test_count_of_items(self):
        count = len(self.content)
        self.assertEqual(count, 3)

    def test_first_elem_content(self):
        first_item = self.content[0]
        self.assertEqual(first_item, (0, "Если грех твой, как пурпур, "))

    def test_second_elem_content(self):
        first_item = self.content[1]
        self.assertEqual(first_item, (1, "Бог, как волну, убелит."))

    def test_last_elem_content(self):
        first_item = self.content[2]
        self.assertEqual(first_item, (2, "Если грех мной багряным,"))


class TestParseText(unittest.TestCase):
    text = '$#$А весна пришла такая нежная, %%Были слышны так ее шаги. %%"Здравствуй!" - ей сказал подснежник вежливо, %%Поднимая к небу лепестки.$#%Весна вновь пришла, %%посмотри вокруг. %%А ты от Христа далеко, мой друг.$#$Ожили и птицы, и растения. %%Реки пробуждаются от сна. %%Почему же ты стоишь растерянно? %%Посмотри, уже пришла весна.$#$Посмотри, какое небо синее. %%Солнца луч тебе желает: "Встань, %%И, стряхнув с себя сонливость зимнюю. %%Прославляй Спасителя Христа!"$#%Весна вновь пришла. %%Посмотри вокруг. %%Иди ко Христу поскорее, друг.'

    def test_verses(self):
        verses = [
            ["А весна пришла такая нежная,", "Были слышны так ее шаги.", '"Здравствуй!" - ей сказал подснежник вежливо,', 'Поднимая к небу лепестки.'],
            ["Ожили и птицы, и растения.", "Реки пробуждаются от сна.", "Почему же ты стоишь растерянно?", "Посмотри, уже пришла весна."],
            ['Посмотри, какое небо синее.', 'Солнца луч тебе желает: "Встань,', 'И, стряхнув с себя сонливость зимнюю.', 'Прославляй Спасителя Христа!"']
        ]
        psalm = Psalm("", 0)
        parse_text(psalm, self.text)
        self.assertEqual(verses, psalm.verses)

    def test_choruses(self):
        choruses = {
            1: ['Весна вновь пришла,', 'посмотри вокруг.', 'А ты от Христа далеко, мой друг.'],
            3: ['Весна вновь пришла.', 'Посмотри вокруг.', 'Иди ко Христу поскорее, друг.']
        }
        psalm = Psalm("", 0)
        parse_text(psalm, self.text)
        self.assertEqual(choruses, psalm.choruses)
