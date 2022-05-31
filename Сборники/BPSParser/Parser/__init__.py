import shutil
import traceback
from pathlib import Path

from Parser.Elements.Psalm import Psalm

COLLECTION_NAME = "Будем петь и славить"

cur_psalm = Psalm()
cur_title_part = ""
cur_section = ""
prev_block_number = [0]


def change_cur_psalm(new_number: int):
    global cur_psalm
    if not cur_psalm.is_empty():
        cur_psalm.write_to_file(COLLECTION_NAME, cur_section)

    cur_psalm.clear(new_number=new_number)


def change_cur_section(new_name: str):
    global cur_section
    full_name = new_name
    if new_name.endswith('...'):
        new_name = new_name[:-3]
    section_dir = Path.cwd() / COLLECTION_NAME / new_name
    if not section_dir.exists():
        section_dir.mkdir()
    print(section_dir.parts[-1])
    section_file = section_dir / 'section.txt'
    section_file.write_text(full_name, encoding='utf-8')
    cur_section = new_name


def append_to_cur_section(additional_text: str):
    global cur_section
    section_dir = Path.cwd() / COLLECTION_NAME / cur_section
    if section_dir.exists():
        shutil.rmtree(section_dir.absolute())

    cur_section += " " + additional_text
    new_section_dir = Path.cwd() / COLLECTION_NAME / cur_section
    if not new_section_dir.exists():
        new_section_dir.mkdir()

    new_section_file = new_section_dir / 'section.txt'
    new_section_file.write_text(cur_section, encoding='utf-8')



def change_cur_title_part(part_name: str):
    global cur_title_part
    cur_title_part = part_name


def get_cur_title_part():
    return cur_title_part


def change_prev_block_nubler(new_number: int):
    global prev_block_number
    prev_block_number[0] = new_number


from Parser.Handlers.PsalmStringData import PsalmStringData
from Parser.Handlers import main_handler

is_broken = False


def get_is_broken():
    return is_broken


def process_block(block: dict):
    for l in block["lines"]:  # iterate through the text lines
        for s in l["spans"]:
            # print(block['number'])
            # print(s)
            try:
                if s['text'].strip():
                    psalm_string_data = PsalmStringData(block['number'], s['text'], s['flags'])
                    if psalm_string_data.str_data == 'I. ПОЙТЕ БОГУ ПЕСНЬ ХВАЛЫ' and not psalm_string_data.is_bold:
                        global is_broken
                        is_broken = True
                        break
                    main_handler.handle(psalm_string_data)
            except Exception as e:
                print(
                    f'In psalm {cur_psalm.number}, in block {block["number"]} with span \'{s}\' an error has occured:')
                print(traceback.format_exc())
