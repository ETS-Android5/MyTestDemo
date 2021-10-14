package com.example.mytestdemo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.mytestdemo.R;

import org.jetbrains.annotations.NotNull;

public class SetProgressBar extends Dialog {
    public SetProgressBar(@NonNull @NotNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_rau_progressbar);
        super.onCreate(savedInstanceState);

        setCancelable(false);

    }

}
