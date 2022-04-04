from abc import ABCMeta, abstractmethod


class Textable:
    __metaclass__ = ABCMeta

    @abstractmethod
    def get_text(self) -> str:
        """get plain text presentation of component"""
