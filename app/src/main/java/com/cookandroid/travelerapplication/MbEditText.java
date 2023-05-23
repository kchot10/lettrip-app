package com.cookandroid.travelerapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.Toast;

public class MbEditText extends androidx.appcompat.widget.AppCompatEditText {

    public MbEditText(Context context ) {
        super( context );
    }
    public MbEditText(Context context, AttributeSet attrs ) {
        super( context, attrs );
    }

    public boolean onKeyPreIme( int keyCode, KeyEvent event ) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                this.clearFocus();
            }
        }
        return super.onKeyPreIme( keyCode, event );
    }

}