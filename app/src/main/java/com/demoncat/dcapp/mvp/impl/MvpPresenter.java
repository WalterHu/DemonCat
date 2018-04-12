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
package com.demoncat.dcapp.mvp.impl;

import com.demoncat.dcapp.mvp.BasePresenter;
import com.demoncat.dcapp.mvp.IView;

import java.util.Random;

/**
 * @Class: MvpPresenter
 * @Description: Demo mvp presenter implements
 * @Author: hubohua
 * @CreateDate: 2018/4/12
 */
public class MvpPresenter extends BasePresenter<MvpPresenter.MvpView> {

    public MvpPresenter(MvpView view) {
        super(view);
    }

    /**
     * Test action one
     */
    public void action1() {
        notifyActionStart(); // notify action start
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    // random notify success or failure
                    Random random = new Random();
                    int r = random.nextInt(5);
                    notifyFinish(r > 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public interface MvpView extends IView {
        // TODO Could define some other function
    }
}
