from abc import ABCMeta, abstractmethod
from xml.etree.ElementTree import Element


class XMLable:
    __metaclass__ = ABCMeta

    @abstractmethod
    def get_xml(self) -> Element:
        """get xml presentation of component"""
