package com.turingoal.common.widget;

/**
 * Created by Administrator on 2017/11/17.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.turingoal.mengbao.teacher.R;


/**
 * 带清除按钮的EditText
 */

public class ClearEditText extends AppCompatEditText implements View.OnFocusChangeListener, TextWatcher {
    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Drawable mClearDrawable; // 删除按钮的引用
    private boolean hasFoucus; // 控件是否有焦点

    private void init() {
        //  获取EditText的DrawableRight，假如没有设置使用默认的图片
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_clear_24dp, null);
        }
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        // 默认设置隐藏图标
        setClearIconVisible(false);
        // 设置焦点改变的监听
        setOnFocusChangeListener(this);
        // 设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight()) && (event.getX() < (getWidth() - getPaddingRight()));
                if (touchable) {
                    this.setText("");
                    if (onTextClearListener != null) { // 清空回调事件
                        onTextClearListener.textClear();
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onFocusChange(View view, boolean hasFoucus) {
        this.hasFoucus = hasFoucus;
        if (hasFoucus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
        if (hasFoucus) {
            setClearIconVisible(charSequence.length() > 0);
        }
    }

    private void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    /**
     * 被清理
     */
    public interface OnTextClearListener {
        void textClear();
    }

    private OnTextClearListener onTextClearListener;

    public void setOnTextClearListener(OnTextClearListener onTextClearListener) {
        this.onTextClearListener = onTextClearListener;
    }
}
