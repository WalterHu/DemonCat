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
package com.demoncat.dcapp.mvp;

import java.util.Random;

/**
 * @Class: BasePresenter
 * @Description: Base presenter defines the common action with view and presenter
 * @Author: hubohua
 * @CreateDate: 2018/4/12
 */
public abstract class BasePresenter<V extends IView> implements IPresenter {
    protected V mView;

    public BasePresenter(V view) {
        mView = view;
    }

    // notify about the start action
    protected void notifyActionStart() {
        if (mView != null) {
            mView.onActionStart();
        }
    }

    // notify about the finish action with result
    protected void notifyFinish(boolean success) {
        if (mView != null) {
            if (success) {
                mView.onActionSuccess();
            } else {
                mView.onActionFailure();
            }
        }
    }

    /**
     * Destory and release the binding with view
     */
    public void destroy() {
        mView = null;
    }
}
