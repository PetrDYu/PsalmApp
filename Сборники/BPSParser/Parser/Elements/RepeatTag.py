from Parser.Elements.ListXMLable import ListXMLable
from Parser.Elements.Textable import Textable
import xml.etree.ElementTree as xml


class RepeatTag(ListXMLable, Textable):

    def __init__(self, tag_id: int, is_opening: bool, repetition_rate: int = 2):
        super().__init__()
        self.is_opening: bool = is_opening
        if is_opening:
            self.repetition_rate: int = -1
        else:
            self.repetition_rate: int = repetition_rate
        self.id: int = tag_id

    def __str__(self):
        return f"<RepeatTag id={self.id} is_opening={self.is_opening} (rep. rate: {self.repetition_rate})>"

    def __repr__(self):
        return self.__str__()

    def get_xml_list(self) -> list[xml.Element]:
        repeat_tag = xml.Element("repeat", {
            "id": str(self.id),
            "rep_rate": str(self.repetition_rate),
            "is_opening": str(self.is_opening).lower()
        })
        return [repeat_tag]

    def get_text(self) -> str:
        return ""

