from pathlib import Path
import fitz

if __name__ == "__main__":
    FILENAME = Path("..") / "Будем петь и славить (макет).pdf"

    doc = fitz.open(FILENAME.absolute())
    print("number of pages: %i" % doc.pageCount)

    print(doc.metadata)
    page1 = doc.load_page(1)
    page1text = page1.get_text("text")
    print('_' * 20)
    print(page1text)

integers = dict(I=1, V=5, X=10, L=50, C=100, D=500, M=1000)


def roman_to_arabic(roman):
    result = 0
    for i, c in enumerate(roman):
        if i + 1 < len(roman) and integers[roman[i]] < integers[roman[i + 1]]:
            result -= integers[roman[i]]
        else:
            result += integers[roman[i]]
    return str(result)