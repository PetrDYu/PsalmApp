import re

from Parser import change_cur_section, append_to_cur_section, change_cur_psalm
from Parser.Handlers.BaseHandler import BaseHandler
from Parser.Handlers.PsalmStringData import PsalmStringData
from Parser.Configs import SECTION_RE


class SectionHandler(BaseHandler):

    def __init__(self, next_handler: BaseHandler = None):
        super().__init__(next_handler)

    def handle_psalm_string(self, s: PsalmStringData):
        r = re.match(SECTION_RE, s.str_data)
        if r:
            change_cur_psalm(0)
            change_cur_section(s.str_data)
        elif s.is_bold and s.str_data.upper() == s.str_data and not s.str_data.isdigit():
            append_to_cur_section(s.str_data)
        else:
            self.is_success = False

