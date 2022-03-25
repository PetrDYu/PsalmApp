import enum
import os.path

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
        data = item[1].strip()
        if item[0] == ItemTypes.verse.index:
            save_to_psalm(parent_item, psalm)
            parent_item = {
                'type': cur_type,
                'children': [data]
            }
            verses_counter += 1
        elif item[0] == ItemTypes.chorus.index:
            save_to_psalm(parent_item, psalm)
            parent_item = {
                'type': cur_type,
                'children': [data],
                'number': verses_counter
            }
        elif item[0] == ItemTypes.string.index:
            parent_item['children'].append(data)

    save_to_psalm(parent_item, psalm)


def save_to_psalm(part, psalm: Psalm):
    if part:
        if part['type'] == ItemTypes.chorus.index:
            psalm.choruses[part['number']] = part['children']
        elif part['type'] == ItemTypes.verse.index:
            psalm.verses.append(part['children'])


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


if __name__ == "__main__":
    res = requests.get("http://app.blagoyouth.com:8880/api/appSong/v2?hymnalId=1&time=1000")
    COLLECTION_NAME = "Песнь Возрождения"
    if not os.path.isdir(COLLECTION_NAME):
        os.mkdir(COLLECTION_NAME)
    try:
        for item in res.json():
            psalm = Psalm(item[2], item[1])
            parse_text(psalm, item[3])
            psalm.write_to_file(COLLECTION_NAME)
    except Exception as e:
        print(e)