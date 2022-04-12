from abc import ABCMeta, abstractmethod
from xml.etree.ElementTree import Element


class ListXMLable:
    __metaclass__ = ABCMeta

    @abstractmethod
    def get_xml_list(self) -> Element:
        """get xml presentation of component"""
