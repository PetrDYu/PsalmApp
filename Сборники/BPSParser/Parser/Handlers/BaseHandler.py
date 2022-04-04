from abc import ABCMeta, abstractmethod
from datetime import timedelta

from Parser.Handlers.PsalmStringData import PsalmStringData


class BaseHandler:
	__metaclass__ = ABCMeta

	next_handler = None
	is_success = False

	def __init__(self, next_handler=None):
		self.next_handler = next_handler

	def next(self, s: PsalmStringData):
		if self.next_handler:
			self.next_handler.handle(s)

	def set_next_handler(self, next_handler):
		self.next_handler = next_handler

	@abstractmethod
	def handle_psalm_string(self, s: PsalmStringData):
		"""Process psalm string"""

	def handle(self, s: PsalmStringData):
		self.is_success = True
		self.handle_psalm_string(s)
		if not self.is_success:
			self.next(s)

	@staticmethod
	def timedelta_to_float(delta: timedelta):
		return delta.seconds + delta.microseconds / 1000000
