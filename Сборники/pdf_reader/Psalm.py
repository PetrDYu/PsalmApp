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