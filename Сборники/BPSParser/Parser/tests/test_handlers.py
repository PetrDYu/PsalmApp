import re
from unittest import TestCase

from Parser.Elements.PsalmParts import Verse, Chorus
from Parser.Elements.PsalmString import RepeatablePsalmString, PlainString
from Parser.Handlers.StartPsalmPartHandler import StartPsalmPartHandler

from Parser.Configs import REPEAT_WITH_INNER_AND_ANY_OUTER
from Parser.Handlers.RepeatHandler import RepeatHandler


class TestStartPsalmPartHandler(TestCase):

    def test_verse(self):
        exp_part = Verse(1)
        exp_text = "О, как Он любит тебя, о, как Он любит меня!"

        string = "1. О, как Он любит тебя, о, как Он любит меня!"
        part, text = StartPsalmPartHandler.detect_part(string)

        self.assertEqual(exp_part.part_name, part.part_name)
        self.assertEqual(exp_part.number, part.number)
        self.assertEqual(exp_text, text)

    def test_chorus(self):
        exp_part = Chorus(0)
        exp_text = "::Ликуйте! Ликуйте! Воспойте и славьте Христа!::"

        string = "Припев: ::Ликуйте! Ликуйте! Воспойте и славьте Христа!::"
        part, text = StartPsalmPartHandler.detect_part(string)

        self.assertEqual(exp_part.part_name, part.part_name)
        self.assertEqual(exp_part.number, part.number)
        self.assertEqual(exp_text, text)

    def test_plain_string(self):

        string = "Вы новую пеню воспойте Тому, Кто вас спас от греха."
        part, text = StartPsalmPartHandler.detect_part(string)

        self.assertEqual(None, part)


class TestRepeatHandler(TestCase):
    text_inner_with_full_outer = ":: ::О Иисусе, приди.:: О, Иисус, в мое сердце приди.::"
    text_inner_without_outer = "Он всемогущий Хранитель, ::о ты, истомленный грехом.::"
    text_with_3_dp_and_outer = ":: :::И сердечными молитвами::: укрепляется мой дух.::"

    def test_border_in_group(self):
        r = re.match(REPEAT_WITH_INNER_AND_ANY_OUTER, self.text_inner_with_full_outer)
        self.assertTrue(RepeatHandler.border_in_group(10, r, "text_inner"))
        self.assertFalse(RepeatHandler.border_in_group(10, r, "text_outer_right"))

        # left border - 1
        self.assertFalse(RepeatHandler.border_in_group(4, r, "text_inner"))
        # left border
        self.assertTrue(RepeatHandler.border_in_group(5, r, "text_inner"))
        # right border
        self.assertTrue(RepeatHandler.border_in_group(21, r, "text_inner"))
        # right border + 1
        self.assertFalse(RepeatHandler.border_in_group(22, r, "text_inner"))

    def test_get_children_with_3_dp_in_group(self):
        r = re.match(REPEAT_WITH_INNER_AND_ANY_OUTER, self.text_inner_with_full_outer)
        test_repeat_3_dp = RepeatablePsalmString(3)
        test_repeat_3_dp.append_child(PlainString("test content"))
        test_children_3_dp = [test_repeat_3_dp]

        res = RepeatHandler.get_children_with_3_dp(13, test_children_3_dp, r, "text_inner")

        self.assertEqual(3, len(res))
        self.assertEqual("О Иисусе", res[0].get_text())
        self.assertEqual(PlainString, type(res[0]))
        self.assertEqual("test content", res[1].get_text())
        self.assertEqual(RepeatablePsalmString, type(res[1]))
        self.assertEqual(", приди.", res[2].get_text())
        self.assertEqual(PlainString, type(res[2]))

    def test_get_children_with_3_dp_not_in_group(self):
        r = re.match(REPEAT_WITH_INNER_AND_ANY_OUTER, self.text_inner_with_full_outer)
        test_repeat_3_dp = RepeatablePsalmString(3)
        test_repeat_3_dp.append_child(PlainString(" test content "))
        test_children_3_dp = [test_repeat_3_dp]

        res = RepeatHandler.get_children_with_3_dp(25, test_children_3_dp, r, "text_inner")
        self.assertEqual(1, len(res))
        self.assertEqual("О Иисусе, приди.", res[0].get_text())
        self.assertEqual(PlainString, type(res[0]))

    def test__handle_inner_with_outer__full(self):
        children, full_string_handled = RepeatHandler.handle_inner_with_outer(self.text_inner_with_full_outer, [], 0)

        self.assertTrue(full_string_handled)
        self.assertEqual(1, len(children))
        self.assertEqual("О Иисусе, приди. О, Иисус, в мое сердце приди.", children[0].get_text())

        self.assertEqual(RepeatablePsalmString, type(children[0].children[0]))
        self.assertEqual("О Иисусе, приди.", children[0].children[0].get_text())

        self.assertEqual(PlainString, type(children[0].children[1]))
        self.assertEqual("О, Иисус, в мое сердце приди.", children[0].children[1].get_text())

    def test__handle_inner_with_outer__without_outer_repeat(self):
        children, full_string_handled = RepeatHandler.handle_inner_with_outer(self.text_inner_without_outer, [], -1)
        # for child in children:
        #     print(f'child: {child.get_text()}')
        self.assertTrue(full_string_handled)
        self.assertEqual(2, len(children))
        self.assertEqual("Он всемогущий Хранитель,", children[0].get_text())
        self.assertEqual(PlainString, type(children[0]))
        self.assertEqual("о ты, истомленный грехом.", children[1].get_text())
        self.assertEqual(RepeatablePsalmString, type(children[1]))

    def test__handle_inner_with_outer__with_3_dp(self):
        children, left_border, full_string_handled, handling_data = RepeatHandler.handle_3_dp(self.text_with_3_dp_and_outer)

        children, full_string_handled = RepeatHandler.handle_inner_with_outer(handling_data, children, left_border)
        self.assertTrue(full_string_handled)
        self.assertEqual(1, len(children))
        self.assertEqual("И сердечными молитвами укрепляется мой дух.", children[0].get_text())
        self.assertEqual("И сердечными молитвами", children[0].children[0].get_text())
        self.assertEqual("укрепляется мой дух.", children[0].children[1].get_text())

    def test__handle_simple_repeat__left_closed(self):
        text = "::Голос радости звучит на земле Иуды и на перекрестках,"
        children, full_string_handled = RepeatHandler.handle_simple_repeat(text, [], -1)
        self.assertTrue(full_string_handled)
        self.assertEqual(1, len(children))
        self.assertEqual("Голос радости звучит на земле Иуды и на перекрестках,", children[0].get_text())
        self.assertEqual(2, children[0].repetition_rate)
        self.assertFalse(children[0].right_closed)
        self.assertTrue(children[0].left_closed)

    def test__handle_simple_repeat__right_closed(self):
        text = "На перекрестках Иерусалима.::"
        children, full_string_handled = RepeatHandler.handle_simple_repeat(text, [], -1)
        self.assertTrue(full_string_handled)
        self.assertEqual(1, len(children))
        self.assertEqual("На перекрестках Иерусалима.", children[0].get_text())
        self.assertEqual(2, children[0].repetition_rate)
        self.assertTrue(children[0].right_closed)
        self.assertFalse(children[0].left_closed)

    def test__handle_simple_repeat__full_with_3_dp(self):
        children, left_border, full_string_handled, handling_data = RepeatHandler.handle_3_dp(
            self.text_with_3_dp_and_outer)

        children, full_string_handled = RepeatHandler.handle_simple_repeat(handling_data, children, left_border)

        self.assertTrue(full_string_handled)
        self.assertEqual(1, len(children))
        self.assertEqual("И сердечными молитвами укрепляется мой дух.", children[0].get_text())
        self.assertEqual("И сердечными молитвами", children[0].children[0].get_text())
        self.assertEqual("укрепляется мой дух.", children[0].children[1].get_text())