import re

#
SECTION_RE = re.compile(r'(?P<section_n>I|II|III|IV|V|VI|VII|VIII|IX|X|XI|XII|XIII|XIV|XV)\. (?P<section_name>[А-Я\.\s,!]{2,})')

# ищет автора музыки
MUSIC_RE = re.compile(r'(Муз\.|Музыка) (?P<composers>.+)')

# ищет источник мелодии (фразы типа "Традиционная еврейская мелодия")
MELODY_RE = re.compile(r'(?P<melody_source>.+) мелодия')

# ищет авторов текста
TEXT_RE = re.compile(r'Сл\. (?P<authors>.+)')

# ищет авторов музыки и текста
TEXT_AND_MUSIC_RE = re.compile(r'(С|C)л\. и муз\. (?P<authors>.+)')

# ищет авторов русского перевода
TEXT_RUS_RE = re.compile(r'(по переводу|Рус\. [тТ]\.) (?P<authors>.+)')
SEPARATE_AUTHOR_RE = re.compile(r'[А-Я]\. [А-Я]\w+') # это использовать не с match, а с find
# Алгоритм для выделения повторений:
#   1. запускаем REPEAT_3_DBP_RE и, если есть, выделяем и удаляем
#   2. запускаем REPEAT_WITH_INNER_AND_ANY_OUTER и обрабатываем результаты
#   3. если 2 ничего не выявило, запускаем REPEAT_RE и определяем просто начало
#       или конец одиночного повторения (или просто строку без повторов)


REPEAT_3_DBP_RE = re.compile(r':::(?P<text>.{2,}):::')  # 1. сначала этим для выделения тройных, если есть

# это regexp ищет все варианты с вложенным повторением:
# ":: ::Йешуа! Ты – Мессия,:: (3 р.)" - с началом внешнего повторения
# ":: ::О Иисусе, приди.:: О, Иисус, в мое сердце приди.::" - полностью завершённое внешнее повторением
# "::Эта милость Божия.:: ::" - с концом внешнего повторения
# "::АШРЕ́Й АИ́Ш АШЕ́Р ЛО АЛА́Х БА́АЦА́Т РЕШАИ́М:: (4 р.)" - одинарное повторение без внешнего (считается внутренним)
# "внешний левый текст::внутренний текст:: (4 р.) внешний правый текст" - повторение,
# окружённое обычным текстом (может быть окружён как с одной, так и с обеих сторон)

# ###### ВНИМАНИЕ!!! Не выделяет одинарное только открытое или только закрытое повторение!

REPEAT_WITH_INNER_AND_ANY_OUTER = re.compile(r'(?P<dbp_outer_left>::)?\s?(?P<text_outer_left>[^:]{2,})?::(?P<text_inner>[^:]{2,})::\s?(\((?P<n_inner>\d) р.\))?(?P<text_outer_right>[^:]{2,})?(?P<dbp_outer_right>::)?\s?(\((?P<n_outer>\d) р.\))?')
# группы, представленные в данном regexp:
# dbp_outer_left - открывающая внешнее повторение пара двоеточий
# dbp_outer_right - закрывающая внешнее повторение пара двоеточий
# text_outer_left - внешний текст слева от внутреннего повторения
# text_outer_right - внешний текст справа от внутреннего повторения
# text_inner - текст внутреннего повторения
# n_outer - количество повторов внешнего повторения
# n_inner - количество повторов внешнего повторения
#
# все текстовые группы считаются таковыми при наличии не менее 2х символов, не содержащих двоеточия

# этот regexp ищет все варианты с одиночным повторением и без него
REPEAT_RE = re.compile(r'(?P<dbp_left>::)?\s?(?P<text>[^:]{2,})\s?(?P<dbp_right>::)?\s?(\((?P<n>\d) р.\))?')

# ищет начало куплета с цифры
VERSE_RE = re.compile(r'(?P<verse_number>\d)\.\s(?P<text>.{2,})')

# ищет начало припева со слова "Припев: "
CHORUS_RE = re.compile(r'Припев: (?P<text>.{2,})')


# RE для повторений, заменянные на REPEAT_WITH_INNER_AND_ANY_OUTER:
#
# REPEAT_INNER_FULL_OUTER_RE = r':: ::(?P<text_inner>.+)::\s?(\((?P<n_inner>\d) р.\))?(?P<text_outer>.+)::\s?(\((' \
#                              r'?P<n_outer>\d) р.\))?'  # 2. потом этим для выделения вложенных, если есть
# REPEAT_INNER_AND_START_OUTER_RE = r':: ::(?P<text_inner>.+)::\s?(\((?P<n_inner>\d) р.\))?(?P<text_outer>.+)?'
# # 3. затем этим для определения, есть ли открытое внешнее повторение и полное вложенное
# REPEAT_INNER_AND_END_OUTER_RE = r''
# REPEAT_2_DBP_RE = r'::(.+):: (\((?P<n>\d) р.\))?'  # 5. затем этим для выделения просто повторений
