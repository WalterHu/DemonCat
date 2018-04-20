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
import android.widget.EditText;

import com.demoncat.dcapp.widget.ClearEditText;

/**
 * @Class: ClearEditActivity
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/4/20
 */
public class ClearEditActivity extends Activity implements ClearEditText.OnEditStateListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear_edit);
    }

    @Override
    public void onTextChanged(EditText editText, int inputType, String content) {
        switch (editText.getId()) {
            case R.id.input_common: {
                break;
            }
            case R.id.input_engine: {
                break;
            }
            case R.id.input_vin: {
                break;
            }
            case R.id.input_plate: {
                break;
            }
            case R.id.input_username: {
                break;
            }
            case R.id.input_password: {
                break;
            }
            case R.id.input_telephone: {
                break;
            }
            case R.id.input_search: {
                break;
            }
        }
    }

    @Override
    public void onFocusChange(EditText editText, int inputType, String content, boolean hasFocus) {
        switch (editText.getId()) {
            case R.id.input_common: {
                break;
            }
            case R.id.input_engine: {
                break;
            }
            case R.id.input_vin: {
                break;
            }
            case R.id.input_plate: {
                break;
            }
            case R.id.input_username: {
                break;
            }
            case R.id.input_password: {
                break;
            }
            case R.id.input_telephone: {
                break;
            }
            case R.id.input_search: {
                break;
            }
        }
    }
}
