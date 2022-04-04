class PsalmStringData:

	def __init__(self, block_number: int, psalm_string: str, flags: int = 0):
		self.str_data = psalm_string.strip()
		self.is_bold = self.bold_is_set(flags)
		self.is_italic = self.italic_is_set(flags)
		self.block_number = block_number

	@staticmethod
	def bold_is_set(flags: int):
		return bool(flags & 2 ** 4)

	@staticmethod
	def italic_is_set(flags: int):
		return bool(flags & 2 ** 1)
