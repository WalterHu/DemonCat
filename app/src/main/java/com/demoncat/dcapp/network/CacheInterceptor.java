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
package com.demoncat.dcapp.network;

import android.app.VoiceInteractor;

import com.demoncat.dcapp.DemonCatApp;
import com.demoncat.dcapp.utils.CommonUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Class: CacheInterceptor
 * @Description: Cache store interceptor for okhttp
 * @Author: hubohua
 * @CreateDate: 2018/4/12
 */

/**
 * Chain 对象表示的是当前的拦截器链条。
 * 通过 Chain 的 request 方法可以获取到当前的 Request 对象。
 * 在使用完 Request 对象之后，通过 Chain 对象的 proceed 方法来继续拦截器链条的执行。
 * 当执行完成之后，可以对得到的 Response 对象进行额外的处理。
 */
public class CacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        boolean isNetworkAvailable = CommonUtils
                .isNetworkAvailable(DemonCatApp.get());
        // 这里就是说判读我们的网络条件，
        if (!isNetworkAvailable) {
            // 要是有网络的话我么就直接获取网络上面的数据，要是没有网络的话我么就去缓存里面取数据
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);
        if (isNetworkAvailable) {
            // 这里设置的max-age为0就是说不进行缓存，我们也可以设置缓存时间
            response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control",
                            "public, max-age=" + Configuration.HTTP_CACHE_MAX_AGE)
                    .build();
        } else {
            // 这里的设置的max-stale是我们的没有网络的缓存时间，想设置多少就是多少
            response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control",
                            "public, only-if-cached, max-stale=" + Configuration.HTTP_CACHE_MAX_STALE)
                    .build();
        }
        return response;
    }
}
