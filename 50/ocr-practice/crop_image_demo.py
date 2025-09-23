import numpy as np
import cv2

def split_by_white_linse(img: np.ndarray, min_line_length=10):
    if img.ndim == 3:
        img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY) # RGB -> grayscale
    height, width = img.shape
    print(f"height: {height}, width: {width}")
    white_rows = np.nonzero(np.all(img==255, axis=1))[0]
    # print(white_rows)
    lines = np.split(white_rows, np.nonzero(np.diff(white_rows > 1))[0] + 1)
    print(lines)

if __name__ == '__main__':
    from pathlib import Path
    img_path = Path('sample.png')
    img = cv2.imread(img_path)
    split_by_white_linse(img)