import os
import re
import xml.etree.ElementTree as xml
from io import BytesIO
from pathlib import Path

from Parser.Elements.PsalmParts import PsalmPart, Verse, Chorus


class Psalm:
    def __init__(self, name="", number=-1, canon=False, text="", text_rus="", music="", additional_info=""):
        self.name = name
        self.number = number
        self.canon = canon
        self.text = text
        self.text_rus = text_rus
        self.music = music
        self.additional_info = additional_info
        self.parts: list[PsalmPart] = []

    def get_xml(self):
        psalm_tag = xml.Element('song', {
            'number': str(self.number),
            'name': self.name,
            'canon': str(self.canon).lower(),
            'text': self.text,
            'textRus': self.text_rus,
            'music': self.music,
            'additionalInfo': self.additional_info
        })
        for part in self.parts:
            psalm_tag.append(part.get_xml())

        return psalm_tag

    def write_to_file(self, collection_name: str, section_name: str):
        self.set_name_from_self()
        elem = self.get_xml()
        doc = xml.ElementTree(elem)
        xml.indent(doc, '    ')

        f_temp = BytesIO()
        doc.write(f_temp, encoding='utf-8', xml_declaration=True)
        dirpath = Path.cwd() / collection_name / section_name
        if not dirpath.exists():
            dirpath.mkdir()
        # dirname = f'{collection_name}\\{section_name}'
        # if not os.path.exists(dirname):
        #     os.makedirs(dirname)
        # filename = f'{dirname}\\{self.number} {self.name}.xml'
        filename = dirpath / f'{self.number} {self.name}.xml'

        try:
            with open(filename.absolute(), 'w', encoding='utf-8') as f:
                f.write(f_temp.getvalue().decode('utf-8'))
        except FileNotFoundError:
            print(f'file not found, psalm {self.number}')
        except Exception as e:
            print(e)

    def is_empty(self):
        return not bool(self.parts)

    def set_name_from_self(self):
        if not self.name:
            self.name = self.parts[0].strings[0].get_text()
            if self.name.endswith('.'):
                self.name = self.name[:-1]
        self.name = self.remove_unavailable_characters_in_filename(self.name)

    @staticmethod
    def remove_unavailable_characters_in_filename(name: str):
        char_re = re.compile(r'[^А-Яа-я, ]')
        res = re.sub(char_re, "", name)
        space_re = re.compile(r'\s{2,}')
        return re.sub(space_re, ' ', res)

    def get_number_of_parts(self):
        n_verse, n_chorus = 0, 0
        for part in self.parts:
            if type(part) is Verse:
                n_verse += 1
            elif type(part) is Chorus:
                n_chorus += 1

        return n_verse, n_chorus

    def clear(self, new_number: int = -1, name="", canon=False, text="", text_rus="", music="", additional_info=""):
        self.number = new_number
        self.name = name
        self.canon = canon
        self.text = text
        self.text_rus = text_rus
        self.music = music
        self.additional_info = additional_info
        self.parts: list[PsalmPart] = []
