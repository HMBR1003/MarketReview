package org.baseballbaedal.baseballbaedal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Administrator on 2017-06-05-005.
 */

public class CustomEditText extends android.support.v7.widget.AppCompatEditText implements TextWatcher, View.OnTouchListener, View.OnFocusChangeListener {

    private Drawable clearDrawable;
    private OnFocusChangeListener onFocusChangeListener;
    private OnTouchListener onTouchListener;
    private boolean isClearButtonSet = true;

    Paint paint = new Paint();

    public void setClearButtonSet(boolean b){
        isClearButtonSet = b;
    }

    public CustomEditText(Context context) {
        super(context);
        InitView();
    }
    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitView();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        InitView();
    }
    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;
    }
    @Override
    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    private void InitView() {
        Drawable tempDrawable = ContextCompat.getDrawable(getContext(), R.drawable.close_button);
        clearDrawable = DrawableCompat.wrap(tempDrawable);
        DrawableCompat.setTintList(clearDrawable, getHintTextColors());
        clearDrawable.setBounds(0, 0, clearDrawable.getIntrinsicWidth(), clearDrawable.getIntrinsicHeight());

        setClearIconVisible(false);

        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(this);
//        this.setOnKeyListener(new OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.rgb(0, 0, 0));

    }
    @Override public void onFocusChange(final View view, final boolean hasFocus) {
        if (hasFocus) {
            if(isClearButtonSet)
                setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
        if (onFocusChangeListener != null) {
            onFocusChangeListener.onFocusChange(view, hasFocus);
        }
    }
    @Override public boolean onTouch(final View view, final MotionEvent motionEvent) {
        final int x = (int) motionEvent.getX();
        if(isClearButtonSet) {
            if (clearDrawable.isVisible() && x > getWidth() - getPaddingRight() - clearDrawable.getIntrinsicWidth()) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    setError(null);
                    setText(null);
                }
                return true;
            }
        }
        if (onTouchListener != null) {
            return onTouchListener.onTouch(view, motionEvent);
        } else {
            return false;
        }
    }
    @Override public final void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
        if(isClearButtonSet) {
            if (isFocused()) {

                setClearIconVisible(s.length() > 0);
            }
        }
    }
    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    @Override public void afterTextChanged(Editable s) {

    }
    private void setClearIconVisible(boolean visible) {
        clearDrawable.setVisible(visible, false);

        setCompoundDrawables(null, null, visible ? clearDrawable : null, null);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        if(this.getMaxLines()>1) {
            int left = getLeft();
            int right = getRight();
            int paddingTop = getPaddingTop();
            int paddingBottom = getPaddingBottom();
            int paddingLeft = getPaddingLeft();
            int paddingRight = getPaddingRight();
            int height = getHeight();
            int lineHeight = getLineHeight();
            int count = (height - paddingTop - paddingBottom) / lineHeight;

            for (int i = 0; i < count; i++) {
                int baseline = lineHeight * (i + 1) + paddingTop;
                canvas.drawLine(left + paddingLeft, baseline, right - paddingRight, baseline, paint);
            }
        }
        super.onDraw(canvas);
    }
}