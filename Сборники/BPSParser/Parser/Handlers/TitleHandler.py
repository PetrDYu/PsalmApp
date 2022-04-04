import re

from Parser import cur_psalm, change_cur_psalm, change_cur_title_part
from Parser.Handlers.BaseHandler import BaseHandler
from Parser.Handlers.PsalmStringData import PsalmStringData
from Parser.Configs import MUSIC_RE, MELODY_RE, TEXT_RE, TEXT_AND_MUSIC_RE, TEXT_RUS_RE


class TitleHandler(BaseHandler):

    def __init__(self, next_handler: BaseHandler = None):
        super().__init__(next_handler)

    def handle_psalm_string(self, s: PsalmStringData):
        if s.str_data.isdigit():
            if s.is_bold:
                change_cur_psalm(int(s.str_data))
                change_cur_title_part('title')
            return

        r = re.match(TEXT_AND_MUSIC_RE, s.str_data)
        if r:
            cur_psalm.text = r.group("authors")
            cur_psalm.music = r.group("authors")
            change_cur_title_part("text and music")
            return

        r = re.match(MUSIC_RE, s.str_data)
        if r:
            cur_psalm.music = r.group("composers")
            change_cur_title_part('music')
            return

        r = re.match(MELODY_RE, s.str_data)
        if r:
            cur_psalm.music = r.group("melody_source")
            change_cur_title_part('melody')
            return

        r = re.match(TEXT_RE, s.str_data)
        if r:
            cur_psalm.text = r.group("authors")
            change_cur_title_part('text')
            return

        r = re.match(TEXT_RUS_RE, s.str_data)
        if r:
            cur_psalm.text_rus = r.group("authors")
            change_cur_title_part("text rus")
            return
        if not s.is_italic:
            change_cur_title_part('body')
        self.is_success = False

