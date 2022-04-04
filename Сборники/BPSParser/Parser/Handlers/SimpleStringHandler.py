import re

from Parser import get_cur_title_part, cur_psalm
from Parser.Elements.PsalmString import PsalmString, PlainString
from Parser.Handlers.BaseHandler import BaseHandler
from Parser.Handlers.PsalmStringData import PsalmStringData
from Parser.Configs import SEPARATE_AUTHOR_RE


class SimpleStringHandler(BaseHandler):

    def __init__(self, next_handler: BaseHandler = None):
        super().__init__(next_handler)

    def handle_psalm_string(self, s: PsalmStringData):
        if not s.is_italic:
            psalm_string = PsalmString()
            psalm_string.append_child(PlainString(s.str_data))
            cur_psalm.parts[-1].strings.append(psalm_string)
            return

        cur_title_part = get_cur_title_part()
        if cur_title_part == 'text and music':
            cur_psalm.text += " " + s.str_data
            cur_psalm.music += " " + s.str_data
        elif cur_title_part == 'text':
            cur_psalm.text += " " + s.str_data
        elif cur_title_part == 'music':
            cur_psalm.music += ' ' + s.str_data
        elif cur_title_part == 'text rus':
            cur_psalm.text_rus += " " + s.str_data
