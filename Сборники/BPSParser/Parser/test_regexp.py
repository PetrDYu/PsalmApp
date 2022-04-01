import unittest
import re
from Configs import REPEAT_WITH_INNER_AND_ANY_OUTER, REPEAT_RE


class TestRepeatWithInnerAndAnyOuterRE(unittest.TestCase):

    def test_start_outer(self):
        text = ':: ::Йешуа! Ты – Мессия,:: (3 р.)'
        r = re.match(REPEAT_WITH_INNER_AND_ANY_OUTER, text)
        self.assertEqual(r.group('dbp_outer_left'), '::')
        self.assertEqual(r.group('dbp_outer_right'), None)
        self.assertEqual(r.group('text_outer_left'), None)
        self.assertEqual(r.group('text_outer_right'), None)
        self.assertEqual(r.group('text_inner'), 'Йешуа! Ты – Мессия,')
        self.assertEqual(r.group('n_outer'), None)
        self.assertEqual(r.group('n_inner'), '3')

    def test_end_outer(self):
        text = '::Эта милость Божия.:: ::'
        r = re.match(REPEAT_WITH_INNER_AND_ANY_OUTER, text)
        self.assertEqual(r.group('dbp_outer_left'), None)
        self.assertEqual(r.group('dbp_outer_right'), '::')
        self.assertEqual(r.group('text_outer_left'), None)
        self.assertEqual(r.group('text_outer_right'), None)
        self.assertEqual(r.group('text_inner'), 'Эта милость Божия.')
        self.assertEqual(r.group('n_outer'), None)
        self.assertEqual(r.group('n_inner'), None)

    def test_full_outer(self):
        text = ':: ::О Иисусе, приди.:: О, Иисус, в мое сердце приди.::'
        r = re.match(REPEAT_WITH_INNER_AND_ANY_OUTER, text)
        self.assertEqual(r.group('dbp_outer_left'), '::')
        self.assertEqual(r.group('dbp_outer_right'), '::')
        self.assertEqual(r.group('text_outer_left'), None)
        self.assertEqual(r.group('text_outer_right'), 'О, Иисус, в мое сердце приди.')
        self.assertEqual(r.group('text_inner'), 'О Иисусе, приди.')
        self.assertEqual(r.group('n_outer'), None)
        self.assertEqual(r.group('n_inner'), None)

    def test_no_outer(self):
        text = '::АШРЕ́Й АИ́Ш АШЕ́Р ЛО АЛА́Х БА́АЦА́Т РЕШАИ́М:: (4 р.)'
        r = re.match(REPEAT_WITH_INNER_AND_ANY_OUTER, text)
        self.assertEqual(r.group('dbp_outer_left'), None)
        self.assertEqual(r.group('dbp_outer_right'), None)
        self.assertEqual(r.group('text_outer_left'), None)
        self.assertEqual(r.group('text_outer_right'), None)
        self.assertEqual(r.group('text_inner'), 'АШРЕ́Й АИ́Ш АШЕ́Р ЛО АЛА́Х БА́АЦА́Т РЕШАИ́М')
        self.assertEqual(r.group('n_outer'), None)
        self.assertEqual(r.group('n_inner'), '4')

    def test_full(self):
        text = ':: внешний левый текст::внутренний текст:: (4 р.) внешний правый текст:: (5 р.)'
        r = re.match(REPEAT_WITH_INNER_AND_ANY_OUTER, text)
        self.assertEqual('::', r.group('dbp_outer_left'))
        self.assertEqual('::', r.group('dbp_outer_right'))
        self.assertEqual('внешний левый текст', r.group('text_outer_left'))
        self.assertEqual(' внешний правый текст', r.group('text_outer_right'))
        self.assertEqual('внутренний текст', r.group('text_inner'))
        self.assertEqual('5', r.group('n_outer'))
        self.assertEqual('4', r.group('n_inner'))

    def test_with_outer_without_repeat(self):
        text = 'внешний левый текст::внутренний текст:: (4 р.) внешний правый текст'
        r = re.match(REPEAT_WITH_INNER_AND_ANY_OUTER, text)
        self.assertEqual(None, r.group('dbp_outer_left'))
        self.assertEqual(None, r.group('dbp_outer_right'))
        self.assertEqual('внешний левый текст', r.group('text_outer_left'))
        self.assertEqual(' внешний правый текст', r.group('text_outer_right'))
        self.assertEqual('внутренний текст', r.group('text_inner'))
        self.assertEqual(None, r.group('n_outer'))
        self.assertEqual('4', r.group('n_inner'))
        

class TestRepeatRE(unittest.TestCase):
    
    def test_full(self):
        text = ':: полный тест :: (3 р.)'
        r = re.match(REPEAT_RE, text)
        self.assertEqual('::', r.group('dbp_left'))
        self.assertEqual('::', r.group('dbp_right'))
        self.assertEqual('полный тест ', r.group('text'))
        self.assertEqual('3', r.group('n'))

    def test_end(self):
        text = 'тест с концом :: (3 р.)'
        r = re.match(REPEAT_RE, text)
        self.assertEqual(None, r.group('dbp_left'))
        self.assertEqual('::', r.group('dbp_right'))
        self.assertEqual('тест с концом ', r.group('text'))
        self.assertEqual('3', r.group('n'))

    def test_start(self):
        text = ':: тест с началом'
        r = re.match(REPEAT_RE, text)
        self.assertEqual('::', r.group('dbp_left'))
        self.assertEqual(None, r.group('dbp_right'))
        self.assertEqual('тест с началом', r.group('text'))
        self.assertEqual(None, r.group('n'))

    def test_no_repeat(self):
        text = 'тест без повторений'
        r = re.match(REPEAT_RE, text)
        self.assertEqual(None, r.group('dbp_left'))
        self.assertEqual(None, r.group('dbp_right'))
        self.assertEqual('тест без повторений', r.group('text'))
        self.assertEqual(None, r.group('n'))

