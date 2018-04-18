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
import android.util.Log;
import android.widget.SeekBar;

import com.demoncat.dcapp.common.Constants;
import com.demoncat.dcapp.widget.RoundCornerImageView;

/**
 * @Class: RoundCornerImageActivity
 * @Description: Round corner image activity
 * @Author: hubohua
 * @CreateDate: 2018/4/18
 */
public class RoundCornerImageActivity extends Activity {
    private RoundCornerImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_corner_image);
        mImageView = findViewById(R.id.img_round);
        ((SeekBar) findViewById(R.id.sb_round)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mImageView != null) {
                    int size = (int) (((float) seekBar.getProgress() / seekBar.getMax()) * 20);
                    Log.d(Constants.TAG_DEMONCAT, "size: " + size);
                    mImageView.setRoundSize(size);
                }
            }
        });
    }
}
