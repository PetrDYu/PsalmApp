from typing import Union
from xml.etree.ElementTree import Element

from Parser.Elements.PsalmString import PsalmString
from Parser.Elements.XMLable import XMLable


class PsalmPart(XMLable):

    def __init__(self, part_name: str, number: int):
        self.strings: list[PsalmString] = []
        self.part_name = part_name
        self.number = number

    def append_strings(self, strings: Union[PsalmString, list[PsalmString]]):
        if type(strings) is list:
            self.strings.extend(strings)
        else:
            self.strings.append(strings)

    def get_xml(self) -> Element:
        part_tag = Element(self.part_name, {'number': str(self.number)})
        for string in self.strings:
            part_tag.append(string.get_xml())
        return part_tag


class Verse(PsalmPart):
    def __init__(self, number: int):
        super().__init__('verse', number)


class Chorus(PsalmPart):
    def __init__(self, number: int):
        super().__init__("chorus", number)

