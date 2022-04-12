import xml.etree.ElementTree as xml
from typing import Union

from Parser.Elements.Container import Container
from Parser.Elements.ListXMLable import ListXMLable
from Parser.Elements.RepeatTag import RepeatTag
from Parser.Elements.Textable import Textable
from Parser.Elements.XMLable import XMLable


class PsalmString(XMLable, Textable, Container):

    def __init__(self):
        super().__init__()
        self.children: list[Union[ListXMLable, Textable]] = []

    def __str__(self):
        return f"<PsalmString '{self.get_text()}'>"

    def __repr__(self):
        return self.__str__()

    def get_xml(self) -> xml.Element:
        string_tag = xml.Element("string")
        for child in self.children:
            for tag in child.get_xml_list():
                string_tag.append(tag)

        return string_tag

    def get_text(self) -> str:
        text = ""
        for child in self.children:
            new_text = child.get_text()
            text += " " + new_text

        text = text.strip()
        return text


class RepeatablePsalmString(ListXMLable, Textable, Container):
    current_id = 0

    @classmethod
    def reset_current_id(cls):
        cls.current_id = 0

    def __init__(self, repetition_rate: int = 2, left_closed: bool = True, right_closed: bool = True):
        super().__init__()
        self.repetition_rate: int = repetition_rate
        self.left_closed = left_closed
        self.right_closed = right_closed
        self.id = RepeatablePsalmString.current_id
        RepeatablePsalmString.current_id += 1

    def __str__(self):
        return f"<RepeatablePsalmString '{self.get_text()}' ({self.repetition_rate})>"

    def __repr__(self):
        return self.__str__()

    def get_xml_list(self) -> list[xml.Element]:
        tag_list = []
        if self.left_closed:
            tag_list.append(RepeatTag(self.id, True, self.repetition_rate).get_xml_list()[0])

        for child in self.children:
            tag_list.extend(child.get_xml_list())

        if self.right_closed:
            tag_list.append(RepeatTag(self.id, False, self.repetition_rate).get_xml_list()[0])

        return tag_list

    def get_text(self) -> str:
        text = ""
        for child in self.children:
            new_text = child.get_text()
            text += " " + new_text

        text = text.strip()
        return text


class PlainString(ListXMLable, Textable):

    def __init__(self, content: str):
        self.content = content.strip()

    def __str__(self):
        return f"<PlainString '{self.get_text()}'>"

    def __repr__(self):
        return self.__str__()

    def get_xml_list(self) -> list[xml.Element]:
        plain_tag = xml.Element("plain")
        plain_tag.text = self.content
        return [plain_tag]

    def get_text(self):
        return self.content

