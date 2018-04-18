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
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.demoncat.dcapp.R;
import com.demoncat.dcapp.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Class: LayoutIndicator
 * @Description: Layout indicator for page count
 *               Used with ViewPager.OnPageChangeListener
 * @Author: hubohua
 * @CreateDate: 2018/4/18
 */
public class LayoutIndicator extends LinearLayout implements ViewPager.OnPageChangeListener {

    private Context mContext;

    private List<ImageView> mIndicators = new ArrayList<>();

    private int mIndicatorWidth;
    private int mIndicatorHeight;
    private int mIndicatorMargin;
    private int mIndicatorSelectedResId;
    private int mIndicatorUnselectedResId;

    private int mCount = 0;

    private static final int DEFAULT_IMG_SIZE = 4; // dp
    private static final int DEFAULT_IMG_PADDING = 3; // dp

    public LayoutIndicator(Context context) {
        super(context);
        initView(context);
    }

    public LayoutIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LayoutIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        mIndicatorWidth = CommonUtils.dip2px(context, DEFAULT_IMG_SIZE);
        mIndicatorHeight = CommonUtils.dip2px(context, DEFAULT_IMG_SIZE);
        mIndicatorMargin = CommonUtils.dip2px(context, DEFAULT_IMG_PADDING);
        mIndicatorSelectedResId = R.drawable.page_indicator_selected;
        mIndicatorUnselectedResId = R.drawable.page_indicator_unselected;
    }

    /**
     * Create indicator image view
     * @param size
     */
    public void createIndicator(int size) {
        mIndicators.clear();
        removeAllViews();
        if (size > 0) {
            mCount = size;
            for (int i = 0; i < mCount; i++) {
                ImageView indicator = new ImageView(mContext);
                indicator.setScaleType(ImageView.ScaleType.CENTER_CROP);
                LinearLayout.LayoutParams params
                        = new LinearLayout.LayoutParams(mIndicatorWidth, mIndicatorHeight);
                params.leftMargin = mIndicatorMargin;
                params.gravity = Gravity.CENTER_VERTICAL;
                if (i == 0) {
                    indicator.setImageResource(mIndicatorSelectedResId);
                } else {
                    indicator.setImageResource(mIndicatorUnselectedResId);
                }
                mIndicators.add(indicator);
                addView(indicator, params);
            }
        }
    }

    /**
     * Change the selected icon of indicator
     * @param position
     */
    public void updateIndicatorSelected(int position) {
        if (mIndicators != null && !mIndicators.isEmpty()) {
            // position = toRealPosition(position);
            if (position >= mCount) position = 0;

            for (int i = 0; i < mIndicators.size(); i++) {
                ImageView indicator = mIndicators.get(i);
                if (indicator != null) {
                    indicator.setImageResource(
                            position == i ? mIndicatorSelectedResId : mIndicatorUnselectedResId);
                    indicator.invalidate();
                }
            }
        }
    }

    @Deprecated
    private int toRealPosition(int position) {
        int realPosition = (position - 1) % mCount;
        if (realPosition < 0)
            realPosition += mCount;
        return realPosition;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        // update currentItem
        updateIndicatorSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
