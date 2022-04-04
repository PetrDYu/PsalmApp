from unittest import TestCase
from xml.etree.ElementTree import Element, tostring

from Parser.Elements.PsalmString import PlainString, RepeatablePsalmString


class TestPlainString(TestCase):

    def test(self):
        exp_res = Element("plain")
        exp_res.text = "test content"
        exp_res = tostring(exp_res, encoding='utf8', method='xml')

        res = PlainString("test content1").get_xml()
        res = tostring(res, encoding='utf8', method='xml')
        self.assertEqual(exp_res, res)


class TestRepeatString(TestCase):

    def test_single_child_xml(self):
        exp_res = Element("repeat", {
            "rep_rate": str(2),
            "right_closed": str(True).lower(),
            "left_closed": str(True).lower()
        })
        exp_res.append(PlainString("test content").get_xml())
        exp_res = tostring(exp_res, encoding='utf8', method='xml')

        res = RepeatablePsalmString()
        res.append_child(PlainString("test content"))
        res = tostring(res.get_xml(), encoding='utf8', method='xml')

        self.assertEqual(exp_res, res)

    def test_some_children_xml(self):
        rep_child = RepeatablePsalmString(3, True, False)
        rep_child.append_child(PlainString("test content inner"))
        children = [
            PlainString("test content outer 1"),
            rep_child,
            PlainString("test content outer 2")
        ]

        exp_res = Element("repeat", {
            "rep_rate": str(4),
            "right_closed": str(False).lower(),
            "left_closed": str(True).lower()
        })
        for child in children:
            exp_res.append(child.get_xml())
        exp_res = tostring(exp_res, encoding='utf8', method='xml')

        res = RepeatablePsalmString(repetition_rate=4, right_closed=False)
        res.append_children(children)
        res = tostring(res.get_xml(), encoding='utf8', method='xml')

        self.assertEqual(exp_res, res)

    def test_single_child_text(self):
        exp_res = "test content"

        res = RepeatablePsalmString()
        res.append_child(PlainString("test content"))
        res = res.get_text()

        self.assertEqual(exp_res, res)

    def test_some_children_text(self):
        rep_child = RepeatablePsalmString(3, True, False)
        rep_child.append_child(PlainString(" test content inner "))
        children = [
            PlainString("test content outer 1"),
            rep_child,
            PlainString("test content outer 2")
        ]

        exp_res = "test content outer 1 test content inner test content outer 2"

        res = RepeatablePsalmString(repetition_rate=4, right_closed=False)
        res.append_children(children)
        res = res.get_text()

        self.assertEqual(exp_res, res)