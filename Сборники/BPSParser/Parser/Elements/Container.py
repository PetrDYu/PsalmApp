from typing import Union

from Parser.Elements.ListXMLable import ListXMLable
from Parser.Elements.Textable import Textable


class Container:

    def __init__(self):
        self.children: list[Union[ListXMLable, Textable]] = []

    def append_child(self, child: Union[ListXMLable, Textable]):
        self.children.append(child)

    def append_children(self, children: list[Union[ListXMLable, Textable]]):
        self.children.extend(children)
