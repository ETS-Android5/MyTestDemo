package com.example.mytestdemo.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class RollTextView extends TextView {
    public RollTextView (Context context) {
        super(context);
    }
    public RollTextView (Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public RollTextView (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}