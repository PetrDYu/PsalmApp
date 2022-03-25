from pathlib import Path
import fitz

if __name__ == "__main__":
	FILENAME = Path("..") / "Будем петь и славить (макет).pdf"

	doc = fitz.open(FILENAME.absolute())
	print("number of pages: %i" % doc.pageCount)

	print(doc.metadata)
	page1 = doc.load_page(10)
	page1text = page1.get_text("text")
	print(page1text)


	# f = FILENAME.open('rb')
	# pdf_reader = PyPDF2.PdfFileReader(f, strict=False)
	# pdf_page = pdf_reader.getPage(12)
	# print(pdf_page.extractText())
