package com.tw.commonsdk.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.tw.commonsdk.utils.ToastUtils;

/**
 * @Author: Sunzhipeng
 * @Date 2019/5/21
 * @Time 17:46
 */
public class LimitedNnumberTextWatcher implements TextWatcher {
    private CharSequence temp;
    private int editStart;
    private int editEnd;
    private EditText mEditText;
    private TextView mTvSize;
    private int mLimiteSize;

    public LimitedNnumberTextWatcher(EditText editText, TextView tvSize, int limiteSize) {
        this.mEditText = editText;
        this.mTvSize = tvSize;
        this.mLimiteSize = limiteSize;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        temp = s;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }


    @Override
    public void afterTextChanged(Editable s) {
        mEditText.removeTextChangedListener(this);
        //        editStart = mEditText.getSelectionStart();
        //        editEnd = mEditText.getSelectionEnd();
        if (temp.length() > mLimiteSize) {
            ToastUtils.showToast("你输入的字数已经超过了限制！");
            s.delete(mLimiteSize, temp.length());
            mEditText.setText(s);
            mEditText.setSelection(mLimiteSize);
        }
        mTvSize.setText("" + s.length());
        mEditText.addTextChangedListener(this);
    }
}
