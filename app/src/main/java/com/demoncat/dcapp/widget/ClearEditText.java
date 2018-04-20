/**
 * Copyright 2018 hubohua
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.demoncat.dcapp.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.demoncat.dcapp.R;
import com.demoncat.dcapp.utils.CommonUtils;

import java.nio.charset.Charset;

/**
 * @Class: ClearEditText
 * @Description: Auto show clear button when input.
 *               All input text cleared by clicking button.
 * @Author: hubohua
 * @CreateDate: 2018/4/19
 */
@SuppressLint("AppCompatCustomView")
public class ClearEditText extends EditText implements View.OnFocusChangeListener, TextWatcher {
    private static final String TAG = ClearEditText.class.getSimpleName();

    protected Context mContext;
    protected int mSize;
    protected Drawable mClearDrawable;

    private boolean mHasFocus;
    private boolean mIsLock; // when lock, edit text could not show clear button

    private OnEditStateListener mEditStateListener;

    private int mInputType;

    // Input type for business design
    public static final int COMMON = 0; // common input type
    private static final int PLATE = 1; // vehicle plate input type
    private static final int ENGINECODE = 2; // engine code input type
    private static final int VINCODE = 3; // vin code input type
    private static final int PINCODE = 4; // pin code input type
    private static final int USERNAME = 5; // use name input type
    private static final int PASSWORD = 6; // password input type
    private static final int TELEPHONE = 7; // telephone input type
    private static final int SEARCH = 8; // keyword search input type

    // input min length
    private static final int TEXT_PASSWORD_MIN_LENGTH = 6;

    // input regular or max length
    private static final int TEXT_COMMON_LENGTH = 50;
    private static final int TEXT_USERNAME_LENGTH = 20;
    private static final int TEXT_TELEPHONE_LENGTH = 11;
    private static final int TEXT_PASSWORD_LENGTH = 14;
    private static final int TEXT_PLATE_LENGTH = 7;
    private static final int TEXT_VINCODE_LENGTH = 17;
    private static final int TEXT_ENGINE_LENGTH = 8;
    private static final int TEXT_PINCODE_LENGTH = 6;

    // validate input char
    private static final String REX_COMMON = "^[\\u4e00-\\u9fa5\\x21-\\x7e]+$";
    private static final String REX_PLATE = "^[\\u4e00-\\u9fa5A-Z0-9]+$";
    private static final String REX_VINCODE = "^[A-Z0-9]+$";
    private static final String REX_ENGINECODE = "^[a-zA-Z0-9]+$";
    private static final String REX_USERNAME = "^[\\u4E00-\\u9FA5A-Za-z0-9\\x21-\\x2F\\x3A-\\x40\\x5B-\\x60\\x7B-\\x7E]+$";
    private static final String REX_TELEPHONE = "^[0-9]\\d*$";
    private static final String REX_PASSWORD = "^[\\x21-\\x7e]+$";
    private static final String REX_PINCODE = "^[0-9]+$";
    private static final String REX_SEARCH = "^[\\u4e00-\\u9fa5a-zA-Z0-9]+$";

    // valid input regular
    // VIN码正则表达式验证，最简单的验证(17位字母和数字的组合)
    public static final String REGEX_VIN = "^[a-zA-Z0-9]{17}$";
    // engine码正则表达式验证，最简单的验证(16位字母和数字的组合)
    public static final String REGEX_ENGINE = "^[a-zA-Z0-9]{8}$";
    // 车牌号正则表达式验证(鄂AL94J7)
    public static final String REGEX_PLATE = "^[\u4E00-\u9FFF]{1}+[A-Z0-9]{6}$";
    // 邮箱正则表达式
    public static final String REGEX_EMAIL = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    // 手机号正则表达式
    public static final String REGEX_MOBILE = "^[1][34578]\\d{9}$";
    // 6位PIN码正则表达式
    public static final String REGUX_PINCODE = "^[0-9]{6}$";
    // 6位重复表达式
    public static final String PIN_REPEAT_REGUX = "^(?=\\d+)(?!([\\d])\\1{5})[\\d]{6}$";
    // Password正则表达式(6-14字母符号数字至少两种组合)
    public static final String REGEX_PASSWORD = "^(?![a-zA-Z]+$)(?!\\d+$)(?![\\W_]+$)\\S{6,14}$";
    // 用户名允许输入字符正则(中文英文数字和特殊字符)
    public static final String REGEX_UNAME_ALLOWED = "^[\\u4E00-\\u9FA5A-Za-z0-9\\x21-\\x2F\\x3A-\\x40\\x5B-\\x60\\x7B-\\x7E]+$";
    // 重复表达式
    public static final String REPEAT_REGEX = "^.*(.)\\1{2}.*$";

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        // init attrs of input type
        initAttrs(attrs);
        // init input filter
        initInputFilter();
        // clear drawable part
        initClearDrawables();
        // set on text change listener
        addTextChangedListener(this);
        // set on focus change listener
        setOnFocusChangeListener(this);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.ClearEditText);
        mInputType = typedArray.getInt(R.styleable.ClearEditText_cet_type, COMMON); // default common input type
        int hintSize = (int) typedArray.getDimension(R.styleable.ClearEditText_hint_size,
                CommonUtils.dip2px(mContext, 14)); // default 14dp size of hint
        setHintTextColor(Color.parseColor("#88ffffff"));
        if (getHint() != null) {
            setHintText(getHint().toString(), hintSize);
        }
        typedArray.recycle();
    }

    /**
     * Set hint text size different from content part
     * @param hintText
     * @param hintSize px
     */
    public void setHintText(String hintText, int hintSize) {
        SpannableString spannableString = new SpannableString(hintText);
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(hintSize, false);
        spannableString.setSpan(absoluteSizeSpan, 0,
                spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setHint(new SpannedString(spannableString));
    }

    // init clear button drawable
    private void initClearDrawables() {
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable =
                    getResources().getDrawable(R.drawable.icon_account_management_cancel);
        }
        mSize = CommonUtils.dip2px(mContext, 24); // default 24dp drawable
        mClearDrawable.setBounds(0, 0, mSize, mSize);
        // mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        // set default clear icon
        setClearIconVisible(false);
    }

    // init input filter for length and valid chars
    private void initInputFilter() {
        int inputLength = TEXT_COMMON_LENGTH;
        int originalType = InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_NORMAL;
        switch (mInputType) {
            case USERNAME: {
                inputLength = TEXT_USERNAME_LENGTH;
                break;
            }
            case TELEPHONE: {
                originalType = InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_VARIATION_NORMAL;
                inputLength = TEXT_TELEPHONE_LENGTH;
                break;
            }
            case PLATE: {
                inputLength = TEXT_PLATE_LENGTH;
                break;
            }
            case VINCODE: {
                inputLength = TEXT_VINCODE_LENGTH;
                break;
            }
            case ENGINECODE: {
                inputLength = TEXT_ENGINE_LENGTH;
                break;
            }
            case PINCODE: {
                originalType = InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_VARIATION_NORMAL;
                inputLength = TEXT_PINCODE_LENGTH;
                break;
            }
            case PASSWORD: {
                originalType = InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                inputLength = TEXT_PASSWORD_LENGTH;
                // password could not be past or copy
                setLongClickable(false);
                setTextIsSelectable(false);
                setCustomSelectionActionModeCallback(new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        return false;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {

                    }
                });
                break;
            }
        }
        setInputType(originalType);
        setFilters(new InputFilter[]{mInputVerifyFilter,
                new InputFilter.LengthFilter(inputLength)});
        setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
    }

    /**
     * Input verify filter
     * Filter for invalid char for each input type
     */
    private InputFilter mInputVerifyFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String rex = REX_COMMON;
            switch (mInputType) {
                case SEARCH: {
                    rex = REX_SEARCH;
                    break;
                }
                case USERNAME: {
                    rex = REX_USERNAME;
                    break;
                }
                case TELEPHONE: {
                    rex = REX_TELEPHONE;
                    break;
                }
                case PASSWORD: {
                    rex = REX_PASSWORD;
                    break;
                }
                case PINCODE: {
                    rex = REX_PINCODE;
                    break;
                }
                case PLATE: {
                    rex = REX_PLATE;
                    break;
                }
                case VINCODE: {
                    rex = REX_VINCODE;
                    break;
                }
                case ENGINECODE: {
                    rex = REX_ENGINECODE;
                    break;
                }
            }
            // check the input byte length more then 50 bytes
            String sourceStr = source.toString();
            int sourceByteLength =
                    sourceStr.getBytes(Charset.forName("utf-8")).length;
            String spannedStr = dest.toString();
            int spannedByteLength =
                    spannedStr.getBytes(Charset.forName("utf-8")).length;
            // return null means not match and don't input.
            // return "" means valid input.
            return !CommonUtils.regexMatch(rex, source.toString()) ||
                    (spannedByteLength + sourceByteLength > TEXT_COMMON_LENGTH) ? "" : null;
        }
    };

    /**
     * Input length valid
     * Help for check input content satisfy the request length
     * @return
     */
    public boolean inputLengthValid() {
        int length = getText().toString().length();
        switch (mInputType) {
            case PLATE: {
                return length == TEXT_PLATE_LENGTH;
            }
            case ENGINECODE: {
                return length == TEXT_ENGINE_LENGTH;
            }
            case VINCODE: {
                return length == TEXT_VINCODE_LENGTH;
            }
            case PINCODE: {
                return length == TEXT_PINCODE_LENGTH;
            }
            case PASSWORD: {
                return length >= TEXT_PASSWORD_MIN_LENGTH &&
                        length <= TEXT_PASSWORD_LENGTH;
            }
            case TELEPHONE: {
                return length == TEXT_TELEPHONE_LENGTH;
            }
            default:
                return length > 0;
        }
    }

    /**
     * Valid input regex of commit
     * Check the input whether validate to use or not
     * @return
     */
    public boolean inputCommitValid() {
        final String contents = getText().toString();
        boolean validRex = false; // input char of content valid
        switch (mInputType) {
            case PLATE: {
                validRex = CommonUtils.regexMatch(contents, REX_PLATE);
                break;
            }
            case VINCODE: {
                validRex = CommonUtils.regexMatch(contents, REX_VINCODE);;
                break;
            }
            case ENGINECODE: {
                 validRex = CommonUtils.regexMatch(contents, REX_ENGINECODE);
                break;
             }
            case PINCODE: {
                validRex = CommonUtils.regexMatch(contents, REX_PINCODE);
                break;
            }
            case TELEPHONE: {
                validRex = CommonUtils.regexMatch(contents, REX_TELEPHONE);
                break;
            }
            case PASSWORD: {
                validRex = CommonUtils.regexMatch(contents, REX_PASSWORD);
                break;
            }
            case USERNAME: {
                validRex = CommonUtils.regexMatch(contents, REX_USERNAME);
                break;
            }
            case SEARCH: {
                validRex = CommonUtils.regexMatch(contents, REX_SEARCH);
                break;
            }
            default: {
                validRex = CommonUtils.regexMatch(contents, REX_COMMON);
                break;
            }
        }
        // input valid and length valid
        return validRex && inputLengthValid();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mClearDrawable != null && event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            //判断触摸点是否在水平范围内
            boolean isInnerWidth = (x > (getWidth() - getTotalPaddingRight())) &&
                    (x < (getWidth() - getPaddingRight()));
            //获取删除图标的边界，返回一个Rect对象
            Rect rect = mClearDrawable.getBounds();
            //获取删除图标的高度
            int height = rect.height();
            int y = (int) event.getY();
            //计算图标底部到控件底部的距离
            int distance = (getHeight() - height) / 2;
            //判断触摸点是否在竖直范围内(可能会有点误差)
            //触摸点的纵坐标在distance到（distance+图标自身的高度）之内，则视为点中删除图标
            boolean isInnerHeight = (y > distance) && (y < (distance + height));
            if (isInnerHeight && isInnerWidth) {
                onRightDrawableClick();
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * On drawable click
     */
    protected void onRightDrawableClick() {
        if (mIsLock) {
            return;
        }
        setText("");
    }

    /**
     * Set the visibility of clear icon
     *
     * @param visible
     */
    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1],
                right, getCompoundDrawables()[3]);
    }

    /**
     * When text focus change to set the visibility of clear icon
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        String content = getText().toString();
        onFocusChangeNotify(content, hasFocus);
        this.mHasFocus = hasFocus;
        setClearIconVisible(mHasFocus && !mIsLock && content.length() > 0);
    }

    /**
     * When input changed to notify the clear icon
     */
    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        setClearIconVisible(mHasFocus && !mIsLock && text.length() > 0);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        onTextChangeNotify(s.toString());
    }

    /**
     * Shake animation
     */
    public void setShakeAnimation() {
        this.setAnimation(shakeAnimation(5));
    }

    /**
     * Shake animation
     *
     * @param counts shake times per 1 sec
     * @return
     */
    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

    /**
     * Force dismiss clear icon or show
     */
    public void lock(boolean lock) {
        if (mIsLock == lock) {
            return;
        }
        mIsLock = lock;
        setClearIconVisible(!mIsLock);
        if (mIsLock) {
            setTextColor(getResources().getColor(android.R.color.darker_gray));
            // setBackground(null);
        } else {
            setTextColor(getResources().getColor(android.R.color.black));
            // setBackgroundResource(R.drawable.input_edittext_bg);
        }
        setEnabled(!mIsLock);
        // setFocusable(!mIsLock);
    }

    private void onFocusChangeNotify(String contents, boolean hasFocus) {
        if (mEditStateListener != null) {
            mEditStateListener.onFocusChange(this, mInputType, contents, hasFocus);
        }
    }

    private void onTextChangeNotify(String contents) {
        if (mEditStateListener != null) {
            mEditStateListener.onTextChanged(this, mInputType, contents);
        }
    }

    /**
     * Set on text change listener
     *
     * @param listener
     */
    public void setOnEditStateListener(OnEditStateListener listener) {
        this.mEditStateListener = listener;
    }

    /**
     * On text change listener
     */
    public interface OnEditStateListener {
        void onTextChanged(EditText editText, int inputType, String content);
        void onFocusChange(EditText editText, int inputType, String content, boolean hasFocus);
    }

    /* No paste and copy */
    @Override
    public boolean onTextContextMenuItem(int id) {
        return mInputType == PASSWORD;
    }

    boolean canPaste() {
        return mInputType != PASSWORD;
    }

    boolean canCut() {
        return mInputType != PASSWORD;
    }

    boolean canCopy() {
        return mInputType != PASSWORD;
    }

    boolean canSelectAllText() {
        return mInputType != PASSWORD;
    }

    boolean canSelectText() {
        return mInputType != PASSWORD;
    }

    boolean textCanBeSelected() {
        return mInputType != PASSWORD;
    }
}
