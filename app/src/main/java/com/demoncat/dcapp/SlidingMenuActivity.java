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
import android.widget.Button;
import android.widget.Toast;

import com.demoncat.dcapp.widget.SliddingMenu;

/**
 * @Class: SlidingMenuActivity
 * @Description: Sliding menu activity demo
 * @Author: hubohua
 * @CreateDate: 2018/4/18
 */
public class SlidingMenuActivity extends Activity {
    private SliddingMenu mMenu;
    private Button mToggleBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_menu);
        mMenu = findViewById(R.id.sliding_menu);
        mMenu.setOpenedListener(new SliddingMenu.OnOpenedListener() {
            @Override
            public void onOpened() {
                Toast.makeText(getApplicationContext(), "菜单打开", Toast.LENGTH_SHORT).show();
                mToggleBtn.setText("关闭");
            }
        });
        mMenu.setClosedListener(new SliddingMenu.OnClosedListener() {
            @Override
            public void onClosed() {
                Toast.makeText(getApplicationContext(), "菜单关闭", Toast.LENGTH_SHORT).show();
                mToggleBtn.setText("打开");
            }
        });
        mToggleBtn = findViewById(R.id.btn_toggle);
        mToggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMenu != null) {
                    mMenu.toggleMenu();
                }
            }
        });
    }
}
