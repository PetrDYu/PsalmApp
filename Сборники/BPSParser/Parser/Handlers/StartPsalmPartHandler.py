import re

from Parser import cur_psalm, prev_block_number, change_prev_block_nubler
from Parser.Elements.PsalmParts import Verse, Chorus
from Parser.Handlers.BaseHandler import BaseHandler
from Parser.Handlers.PsalmStringData import PsalmStringData
from Parser.Configs import CHORUS_RE, VERSE_RE


class StartPsalmPartHandler(BaseHandler):

    def __init__(self, next_handler: BaseHandler = None):
        super().__init__(next_handler)

    def handle_psalm_string(self, s: PsalmStringData):

        part, s.str_data = self.detect_part(s.str_data)

        if part:
            cur_psalm.parts.append(part)
            change_prev_block_nubler(s.block_number)

        if not s.is_italic and (cur_psalm.is_empty() or prev_block_number[0] != s.block_number):
            verse = Verse(0)
            cur_psalm.parts.append(verse)
            change_prev_block_nubler(s.block_number)

        self.is_success = False

    @staticmethod
    def detect_part(string: str):
        text = string
        part = None
        r = re.match(VERSE_RE, string)
        if r:
            part = Verse(int(r.group("verse_number")))
            text = r.group("text").strip()
        else:
            r = re.match(CHORUS_RE, string)
            if r:
                part = Chorus(cur_psalm.get_number_of_parts()[0])
                text = r.group("text").strip()

        return part, text
