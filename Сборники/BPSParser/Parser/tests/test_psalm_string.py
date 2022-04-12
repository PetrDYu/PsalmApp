from unittest import TestCase
from xml.etree.ElementTree import Element, tostring

from Parser.Elements.PsalmString import PlainString, RepeatablePsalmString
from Parser.Elements.RepeatTag import RepeatTag


class TestPlainString(TestCase):

    def test(self):
        exp_res = Element("plain")
        exp_res.text = "test content"
        exp_res = tostring(exp_res, encoding='utf8', method='xml')

        res = PlainString("test content").get_xml_list()
        res = tostring(res[0], encoding='utf8', method='xml')
        self.assertEqual(exp_res, res)


class TestRepeatString(TestCase):

    def test_single_child_xml(self):
        RepeatablePsalmString.reset_current_id()
        exp_res = [Element("repeat", {
                        "id": "0",
                        "rep_rate": str(2),
                        "is_opening": str(True).lower(),

                    }),
                   PlainString("test content").get_xml_list()[0],
                   Element("repeat", {
                       "id": "0",
                       "rep_rate": str(2),
                       "is_opening": str(False).lower(),
                    })
                   ]
        el = Element("main")
        el.extend(exp_res)
        exp_res = tostring(el, encoding='utf8', method='xml')

        RepeatablePsalmString.reset_current_id()
        res = RepeatablePsalmString()
        res.append_child(PlainString("test content"))
        el = Element("main")
        el.extend(res.get_xml_list())
        res = tostring(el, encoding='utf8', method='xml')

        self.assertEqual(exp_res, res)

    def test_some_children_xml(self):
        RepeatablePsalmString.current_id = 1
        rep_child = RepeatablePsalmString(3, True, False)
        rep_child.append_child(PlainString("test content inner"))
        children = [
            PlainString("test content outer 1"),
            rep_child,
            PlainString("test content outer 2")
        ]

        exp_res = [Element("repeat", {
                        "id": "0",
                        "rep_rate": str(4),
                        "is_opening": str(True).lower(),

                    })]
        for child in children:
            exp_res.extend(child.get_xml_list())
        # exp_res.append(Element("repeat", {
        #                 "id": "2",
        #                 "rep_rate": str(4),
        #                 "is_opening": str(False).lower(),
        #
        #                 }))
        el = Element("main")
        el.extend(exp_res)
        exp_res = tostring(el, encoding='utf8', method='xml').decode("utf-8")

        RepeatablePsalmString.reset_current_id()
        res = RepeatablePsalmString(repetition_rate=4, right_closed=False)
        res.append_children(children)
        el = Element("main")
        el.extend(res.get_xml_list())
        res = tostring(el, encoding='utf8', method='xml').decode("utf-8")

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