import xml.etree.ElementTree as xml
from io import BytesIO

class Psalm:
    def __init__(self, name, number, canon = False, text = "", text_rus = "", music = ""):
        self.name = name
        self.number = number
        self.canon = canon
        self.text = text
        self.text_rus = text_rus
        self.music = music
        self.verses = []
        self.choruses = dict()

    def get_xml(self):
        psalm_tag = xml.Element('psalm', {
            'number': str(self.number),
            'name': self.name,
            'canon': str(self.canon).lower(),
            'text': self.text,
            'textRus': self.text_rus,
            'music': self.music
        })
        for i, verse in enumerate(self.verses):
            verse_tag = xml.SubElement(psalm_tag, 'verse', {'number': str(i + 1)})
            for string in verse:
                string_tag = xml.SubElement(verse_tag, 'string')
                plain_tag = xml.SubElement(string_tag, "plain")
                plain_tag.text = string

            if i + 1 in self.choruses:
                chorus_tag = xml.SubElement(psalm_tag, 'chorus', {'number': str(i + 1)})
                for string in self.choruses[i + 1]:
                    string_tag = xml.SubElement(chorus_tag, 'string')
                    plain_tag = xml.SubElement(string_tag, "plain")
                    plain_tag.text = string

        return psalm_tag

    def write_to_file(self, collectionName: str):
        elem = self.get_xml()
        doc = xml.ElementTree(elem)
        xml.indent(doc, '    ')

        f_temp = BytesIO()
        doc.write(f_temp, encoding='utf-8', xml_declaration=True)
        with open(f'{collectionName}\\{self.number} {self.name}.xml', 'w', encoding='utf-8') as f:
            f.write(f_temp.getvalue().decode('utf-8'))
