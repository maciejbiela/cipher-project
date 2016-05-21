package project;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

class LengthDocumentFilter extends DocumentFilter {

    private final int size;

    LengthDocumentFilter(int size) {
        this.size = size;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
        if (fb.getDocument().getLength() + text.length() <= size) {
            fb.insertString(offset, text, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (fb.getDocument().getLength() + text.length() - length <= size) {
            fb.replace(offset, length, text, attrs);
        }
    }
}
