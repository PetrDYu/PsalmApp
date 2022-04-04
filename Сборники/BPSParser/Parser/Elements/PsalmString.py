import xml.etree.ElementTree as xml
from typing import Union

from Parser.Elements.Textable import Textable
from Parser.Elements.XMLable import XMLable


class PsalmString(XMLable, Textable):

    def __init__(self):
        self.children: list[Union[XMLable, Textable]] = []

    def __str__(self):
        return f"<PsalmString '{self.get_text()}'>"

    def __repr__(self):
        return self.__str__()

    def append_child(self, child: Union[XMLable, Textable]):
        self.children.append(child)

    def append_children(self, children: list[Union[XMLable, Textable]]):
        self.children.extend(children)

    def get_xml(self) -> xml.Element:
        string_tag = xml.Element("string")
        for child in self.children:
            string_tag.append(child.get_xml())

        return string_tag

    def get_text(self) -> str:
        text = ""
        for child in self.children:
            new_text = child.get_text()
            text += " " + new_text

        text = text.strip()
        return text


class RepeatablePsalmString(PsalmString):

    def __init__(self, repetition_rate: int = 2, right_closed: bool = True, left_closed: bool = True):
        super().__init__()
        self.repetition_rate: int = repetition_rate
        self.right_closed: bool = right_closed
        self.left_closed: bool = left_closed

    def __str__(self):
        return f"<RepeatablePsalmString '{self.get_text()}' ({self.repetition_rate})>"

    def __repr__(self):
        return self.__str__()

    def get_xml(self) -> xml.Element:
        repeat_tag = xml.Element("repeat", {
            "rep_rate": str(self.repetition_rate),
            "left_closed": str(self.left_closed).lower(),
            "right_closed": str(self.right_closed).lower()
        })
        for child in self.children:
            repeat_tag.append(child.get_xml())

        return repeat_tag


class PlainString(XMLable, Textable):

    def __init__(self, content: str):
        self.content = content.strip()

    def __str__(self):
        return f"<PlainString '{self.get_text()}'>"

    def __repr__(self):
        return self.__str__()

    def get_xml(self) -> xml.Element:
        plain_tag = xml.Element("plain")
        plain_tag.text = self.content
        return plain_tag

    def get_text(self):
        return self.content

