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
package com.demoncat.dcapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.demoncat.dcapp.widget.LayoutIndicator;

/**
 * @Class: LayoutIndicatorActivity
 * @Description: Layout indicator activity
 * @Author: hubohua
 * @CreateDate: 2018/4/18
 */
public class LayoutIndicatorActivity extends Activity {
    private LayoutIndicator mIndicator;
    private int mPage = 0; // current page index

    private static final int MAX_COUNT = 5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_indicator);
        mIndicator = findViewById(R.id.indicator);
        mIndicator.createIndicator(MAX_COUNT); // initialize with 5
        mIndicator.updateIndicatorSelected(mPage); // initialize the first page selected
        findViewById(R.id.btn_pre).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previous();
            }
        });
        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
    }

    private void next() {
        if (mIndicator != null) {
            mPage ++;
            if (mPage >= MAX_COUNT) {
                mPage = 0;
            }
            mIndicator.updateIndicatorSelected(mPage);
        }
    }

    private void previous() {
        if (mIndicator != null) {
            mPage --;
            if (mPage < 0) {
                mPage = MAX_COUNT - 1;
            }
            mIndicator.updateIndicatorSelected(mPage);
        }
    }
}
