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
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

import com.demoncat.dcapp.R;

/**
 * @Class: GradientTextView
 * @Description: Gradient text view
 * @Author: hubohua
 * @CreateDate: 2018/4/17
 */
public class GradientTextView extends TextView {
    private int startColor;
    private int endColor;
    private LinearGradient shader;

    public GradientTextView(Context context) {
        super(context);
        init(context, null);
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray styled = context.obtainStyledAttributes(attrs, R.styleable.GradientTextView);
            startColor = styled.getColor(R.styleable.GradientTextView_startColor, 0);
            endColor = styled.getColor(R.styleable.GradientTextView_endColor, 0);
            styled.recycle();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (startColor != 0 && endColor != 0) {
            if (shader == null) {
                // 线性渐变: 从起点(0,0)到终点(width, 0) 水平渐变
                // 模式: Repeat 重复
                shader = new LinearGradient(0, 0, getWidth(), 0, new int[]{startColor, endColor},
                        null, Shader.TileMode.REPEAT);
            }
            getPaint().setShader(shader);
        }
    }
}
