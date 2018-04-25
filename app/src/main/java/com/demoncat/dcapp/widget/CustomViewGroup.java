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

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.demoncat.dcapp.R;
import com.demoncat.dcapp.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Class: CustomViewGroup
 * @Description: Custom ViewGroup sub class
 * @Author: hubohua
 * @CreateDate: 2018/4/25
 */
public class CustomViewGroup extends ViewGroup {
    private static final String TAG = CustomViewGroup.class.getSimpleName();

    public static final int MAX_PHOTO_NUMBER = 9;

    private int[] mConstImgIds = { R.drawable.p1, R.drawable.p2,
            R.drawable.p3, R.drawable.p4, R.drawable.p5,
            R.drawable.p6, R.drawable.p7, R.drawable.p8,
            R.drawable.p9 };

    // horizontal space among children views
    int hSpace = CommonUtils.dip2px(getContext(), 10);
    // vertical space among children views
    int vSpace = CommonUtils.dip2px(getContext(), 10);

    // every child view width and height.
    int childWidth = 0;
    int childHeight = 0;

    // store images res id
    List<Integer> mImageResArrayList = new ArrayList<Integer>(9);

    private View addPhotoView;

    public CustomViewGroup(Context context) {
        super(context);
    }

    public CustomViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray t = context.obtainStyledAttributes(attrs,
                R.styleable.CustomViewGroup, 0, 0);
        hSpace = t.getDimensionPixelSize(
                R.styleable.CustomViewGroup_hSpace, hSpace);
        vSpace = t.getDimensionPixelSize(
                R.styleable.CustomViewGroup_wSpace, vSpace);
        t.recycle();

        addPhotoView = new View(context);
        addView(addPhotoView);
        mImageResArrayList.add(1); // add a image id for add button
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int rw = MeasureSpec.getSize(widthMeasureSpec);
        int rh = MeasureSpec.getSize(heightMeasureSpec);

        int sw = MeasureSpec.getMode(widthMeasureSpec);
        int sh = MeasureSpec.getMode(heightMeasureSpec);

        Log.d(TAG, "onMeasure rw: " + rw +
                ", rh: " + rh +
                ", sw: " + (sw == MeasureSpec.EXACTLY) +
                ", sh: " + (sh == MeasureSpec.AT_MOST));

        childWidth = (rw - 2 * hSpace) / 3;
        childHeight = childWidth;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i ++) {
            View child = getChildAt(i);
            int specWidth = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            int specHeight = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            child.measure(specWidth, specHeight);
            // 把子View的左上角坐标存储到我们自定义的LayoutParams的left和top二个字段中，Layout阶段会使用
//            LayoutParams lParams = (LayoutParams) child.getLayoutParams();
//            lParams.left = (i % 3) * (childWidth + hSpace);
//            lParams.top = (i / 3) * (childHeight + vSpace);
        }
        int vw = rw;
        int vh = rh;
        if (childCount < 3) {
            vw = childCount * (childWidth + hSpace);
        }
        vh = ((childCount + 3) / 3) * (childHeight + vSpace) + vSpace;
        setMeasuredDimension(vw, vh);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        Log.d(TAG, "onLayout - childCount: " + childCount);
        for (int i = 0; i < childCount; i ++) {
            View child = getChildAt(i);
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            params.left = (i % 3) * (childWidth + hSpace);
            params.top = (i / 3) * (childHeight + vSpace) + vSpace;
            child.layout(params.left, params.top,
                    params.left + childWidth, params.top + childHeight);
            Log.d(TAG, "onLayout - i: " + i + ", size: " + mImageResArrayList.size());
            if (i == mImageResArrayList.size() - 1 && mImageResArrayList.size() <= MAX_PHOTO_NUMBER + 1) {
                // add button
                child.setBackgroundResource(R.mipmap.ic_launcher_round);
                child.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Add photo click
                        getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                addImageResource();
                            }
                        });
                    }
                });
            } else {
                child.setBackgroundResource(mConstImgIds[i]);
                child.setOnClickListener(null);
            }
        }
    }

    private void addImageResource() {
        if (mImageResArrayList.size() <= MAX_PHOTO_NUMBER) {
            View newChild = new View(getContext());
            addView(newChild);
            mImageResArrayList.add(1);
            requestLayout();
            invalidate();
        } else {
            Toast.makeText(getContext(), "已达上限9张", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new CustomViewGroup.LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new CustomViewGroup.LayoutParams(p);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new CustomViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof CustomViewGroup.LayoutParams;
    }

    /**
     * Custom layout params
     */
    public static class LayoutParams extends ViewGroup.LayoutParams {
        public int left = 0;
        public int top = 0;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
