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

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.TrustManagerFactory;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.internal.tls.OkHostnameVerifier;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Class: ServiceFactory
 * @Description: Implementation of Retrofit & OkHttp.
 *               Help to create the service of Retrofit format.
 *               Singleton in process.
 * @Author: hubohua
 * @CreateDate: 2018/4/12
 */
public class ServiceFactory {
    private static String TAG = ServiceFactory.class.getSimpleName();

    private static class SingletonHolder {
        private static final ServiceFactory INSTANCE = new ServiceFactory();
    }

    /**
     * Singleton function
     * @return
     */
    public static ServiceFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private OkHttpClient mClient;

    private Retrofit mRetrofit;

    private static Context sContext;

    /**
     * Should be called at beginning of process
     * @param context
     */
    public static void initContext(Context context) {
        sContext = context;
    }

    private ServiceFactory() {
        initRetrofit();
    }

    // init configuration of retrofit and okhttp
    private void initRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // print the log using interceptor
        builder.addInterceptor(
                new HttpLoggingInterceptor().setLevel(
                        HttpLoggingInterceptor.Level.BODY));
        // http request&response cache dir and file
        File file = new File(sContext.getExternalCacheDir(),
                Configuration.HTTP_CACHE_DIR);
        Cache cache = new Cache(file, Configuration.HTTP_CACHE_SIZE);

        TrustManagerFactory factory =
                SSLHelper.getTrustManagerFactory(); // SC JSSE provider factory

        mClient = builder
                .retryOnConnectionFailure(true)
                .connectTimeout(Configuration.HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Configuration.HTTP_READ_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Configuration.HTTP_READ_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new CacheInterceptor()) // ? application interceptor
                .addNetworkInterceptor(new CacheInterceptor()) // ? network interceptor
                .cache(cache)
                .sslSocketFactory(
                        SSLHelper.getSSLSocketFactoryLocally(
                                sContext, "CERT_FILE", factory),
                        SSLHelper.getX509TrustManager(factory))
                .hostnameVerifier(OkHostnameVerifier.INSTANCE)
                .build();
        // init retrofit
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Configuration.HTTP_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // ? async observables
                .client(mClient)
                .build();
    }

    /**
     * Create service object according to retrofit structure
     * @param service
     * @param <T>
     * @return
     */
    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return mRetrofit.create(service);
    }
}
