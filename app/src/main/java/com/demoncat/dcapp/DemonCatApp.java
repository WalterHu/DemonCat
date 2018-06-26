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

import android.app.Application;

import com.demoncat.dcapp.network.ServiceFactory;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

/**
 * @Class: DemonCatApp
 * @Description: Base application
 * @Author: hubohua
 * @CreateDate: 2018/4/12
 */
public class DemonCatApp extends Application {
    private static DemonCatApp mInstance;

    static {
        BouncyCastleProvider bouncyCastleProvider =
                new org.spongycastle.jce.provider.BouncyCastleProvider();
        // set default JCA/JCE structure to BouncyCastleProvider
        Security.insertProviderAt(bouncyCastleProvider, 1);
        // add JSSE(SSL/TLS) structure to BouncyCastleJsseProvider
        Security.addProvider(
                new org.spongycastle.jsse.provider.BouncyCastleJsseProvider(bouncyCastleProvider));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        // init context of service factory
        ServiceFactory.initContext(mInstance);
    }

    public static DemonCatApp get() {
        return mInstance;
    }
}