package com.autotest.photo.editor.polish;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.AppCompatEditText;

import com.autotest.photo.editor.fragment.TextFragment;

public class PolishEditText extends AppCompatEditText {
    private TextFragment textFragment;

    public PolishEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setTextFragment(TextFragment textFragment) {
        this.textFragment = textFragment;
    }

    public boolean onKeyPreIme(int i, KeyEvent keyEvent) {
        if (i == 4) {
            ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getWindowToken(), 0);
            this.textFragment.dismissAndShowSticker();
        }
        return false;
    }
}
