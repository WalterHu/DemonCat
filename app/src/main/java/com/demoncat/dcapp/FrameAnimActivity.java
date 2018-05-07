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

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.Toast;

import com.demoncat.dcapp.widget.FrameAnimation;

/**
 * @Class: FrameAnimActivity
 * @Description: Frame animation activity
 * @Author: hubohua
 * @CreateDate: 2018/5/6
 */
public class FrameAnimActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_anim);
        int[] resIds = new int[52];
        for (int i = 0 ; i < resIds.length; i ++) {
            int id = getResources().
                    getIdentifier("p" + (i + 1), "drawable", getPackageName());
            resIds[i] = id;
        }
        FrameAnimation frameAnimation =
                new FrameAnimation((ImageView) findViewById(R.id.img_vehicle), resIds, 30);
        frameAnimation.setAnimationListener(new FrameAnimation.OnAnimationListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationFinish() {
                Toast.makeText(getApplicationContext(), "动画完成", Toast.LENGTH_SHORT).show();
            }
        });
        frameAnimation.start();
    }
}
