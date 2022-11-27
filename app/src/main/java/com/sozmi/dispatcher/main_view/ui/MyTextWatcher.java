package com.sozmi.dispatcher.main_view.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class MyTextWatcher implements TextWatcher {
    private boolean isValid = false;
    private final EditText textView;
    private final String pattern;
    private final String error;

    public MyTextWatcher(EditText txt, String pattern, String error) {
        textView = txt;
        this.pattern = pattern;
        this.error = error;
        txt.addTextChangedListener(this);
    }

    private boolean isNoValidPattern(String text) {
        return !text.matches(pattern);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        var text = textView.getText().toString();
        if (text.isEmpty() || isNoValidPattern(text)) {
            textView.setError(error);
            isValid = false;
            Log.d("TW", "error: pass no valid" + isNoValidPattern(text));
        } else {
            textView.setError(null);
            isValid = true;
            Log.d("TW", "yes");
        }
    }

    public String getText() {
        return textView.getText().toString();
    }

    public boolean isEmpty() {
        return textView.getText().toString().isEmpty();
    }

    public boolean isValid() {
        return isValid;
    }
}
