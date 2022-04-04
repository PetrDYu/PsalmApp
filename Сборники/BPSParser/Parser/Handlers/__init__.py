from Parser.Handlers.RepeatHandler import RepeatHandler
from Parser.Handlers.SectionHandler import SectionHandler
from Parser.Handlers.SimpleStringHandler import SimpleStringHandler
from Parser.Handlers.StartPsalmPartHandler import StartPsalmPartHandler
from Parser.Handlers.TitleHandler import TitleHandler

section_handler = SectionHandler()
title_handler = TitleHandler()
start_psalm_part_handler = StartPsalmPartHandler()
repeat_handler = RepeatHandler()
simple_string_handler = SimpleStringHandler()

main_handler = section_handler
section_handler.set_next_handler(title_handler)
title_handler.set_next_handler(start_psalm_part_handler)
start_psalm_part_handler.set_next_handler(repeat_handler)
repeat_handler.set_next_handler(simple_string_handler)
