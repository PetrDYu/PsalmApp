from pathlib import Path
import fitz

from Parser import process_block, change_cur_psalm, get_is_broken


def flags_decomposer(flags):
	"""Make font flags human readable."""
	l = []
	if flags & 2 ** 0:
		l.append("superscript")
	if flags & 2 ** 1:
		l.append("italic")
	if flags & 2 ** 2:
		l.append("serifed")
	else:
		l.append("sans")
	if flags & 2 ** 3:
		l.append("monospaced")
	else:
		l.append("proportional")
	if flags & 2 ** 4:
		l.append("bold")
	return ", ".join(l)


if __name__ == "__main__":
	FILENAME = Path("..") / "Будем петь и славить (макет).pdf"

	doc = fitz.open(FILENAME.absolute())
	print("number of pages: %i" % doc.pageCount)

	# print(doc.metadata)
	# page = doc[119]
	# blocks = page.get_text("dict", flags=11)["blocks"]
	# for b in blocks:
	# 	for l in b['lines']:
	# 		for s in l['spans']:
	# 			print(s)

	## Основной блок:
	for page in doc:
		blocks = page.get_text("dict", flags=11)["blocks"]
		for b in blocks:
			process_block(b)
			if get_is_broken():
				break
		if get_is_broken():
			break
	change_cur_psalm(0)

	##
	# print(blocks[1]['lines'][0]['spans'][0])
	# for b in blocks:  # iterate through the text blocks
	# 	for l in b["lines"]:  # iterate through the text lines
	# 		for s in l["spans"]:  # iterate through the text spans
	# 			print(f"{s['text']} : {flags_decomposer(s['flags'])} : {b['number']}")
	# page1 = doc.load_page(2)
	# page1text = page1.get_text("text")
	# print(page1text)
