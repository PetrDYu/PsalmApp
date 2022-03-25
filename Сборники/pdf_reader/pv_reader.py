import enum

import requests
from scipy.constants import value

from Psalm import Psalm

VERSE_START = "$#$"
CHORUS_START = "$#%"
STRING_START = "%%"


class ItemTypes(enum.Enum):
    verse = (0, VERSE_START)
    chorus = (1, CHORUS_START)
    string = (2, STRING_START)

    def __init__(self, index, divider):
        self.index = index
        self.divider = divider


def parse_text(psalm: Psalm, text: str):
    parent_item = {}
    verses_counter = 0
    for item in text_items(text):
        cur_type = item[0]
        if item[0] == ItemTypes.verse.index:
            if parent_item:
                psalm.choruses[parent_item['number']] = parent_item['children']
            parent_item = {
                'type': cur_type,
                'children': [item[1]]
            }
            verses_counter += 1
        elif item[0] == ItemTypes.chorus.index:
            if parent_item:
                psalm.verses.append(parent_item['children'])
            parent_item = {
                'type': cur_type,
                'children': [item[1]],
                'number': verses_counter
            }
        elif item[0] == ItemTypes.string.index:
            parent_item['children'].append(item[1])


def text_items(text: str):
    cur_type = ItemTypes.verse.index
    cur_elem_start = 0
    while cur_elem_start != len(text):
        next_type, next_elem_start = get_next_item_index(text, cur_elem_start)
        shift = 2 if cur_type == ItemTypes.string.index else 3
        yield cur_type, text[cur_elem_start + shift: next_elem_start]
        cur_type = next_type
        cur_elem_start = next_elem_start


def get_next_item_index(text: str, cur_elem_start):
    next_verse_start_temp = text.find(ItemTypes.verse.divider, cur_elem_start + 1)
    next_chorus_start_temp = text.find(ItemTypes.chorus.divider, cur_elem_start + 1)
    next_string_start_temp = text.find(ItemTypes.string.divider, cur_elem_start + 1)
    next_verse_start = next_verse_start_temp if next_verse_start_temp != -1 else len(text)
    next_chorus_start = next_chorus_start_temp if next_chorus_start_temp != -1 else len(text)
    next_string_start = next_string_start_temp if next_string_start_temp != -1 else len(text)
    return min(enumerate([next_verse_start, next_chorus_start, next_string_start]), key=lambda x: x[1])


def write_to_file(psalm: Psalm):
    pass


if __name__ == "__main__":
    res = requests.get("http://app.blagoyouth.com:8880/api/appSong/v2?hymnalId=1&time=1000")
    try:
        for item in res.json():
            psalm = Psalm(item[2], item[1])
            parse_text(psalm, item[3])
            write_to_file(psalm)
    except:
        pass