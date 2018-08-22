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
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.demoncat.dcapp.widget.FrameAnimation;
import com.demoncat.dcapp.widget.GlideRoundTransform;

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
        Configuration configuration = getResources().getConfiguration();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Log.d("test", "height: " + metrics.heightPixels + ", width: " + metrics.widthPixels);
        Log.d("test", "configuration.smallestScreenWidthDp: " + configuration.smallestScreenWidthDp);
        final ImageView imageView = findViewById(R.id.image_view);
        Log.d("test", "imageView: " + imageView);
//        GlideRoundTransform transformation =
//                new GlideRoundTransform(getApplicationContext(),
//                        dip2px(getApplicationContext(), 10));
//        transformation.setExceptCorner(false, false, false, false);
//        Glide.with(this).load("https://203.93.252.29:18088/cherym31t/m31t/download/?id=MS5qcGc=").
//                asBitmap().
//                skipMemoryCache(true).
//                placeholder(R.drawable.banner_default).
//                error(R.drawable.banner_default).
//                transform(transformation).into(imageView);
        findViewById(R.id.title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FrameAnimActivity.this, LeakMemActivity.class));
            }
        });
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
