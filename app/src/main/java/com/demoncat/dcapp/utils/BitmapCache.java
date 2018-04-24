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
package com.demoncat.dcapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import java.lang.ref.SoftReference;

/**
 * @Class: BitmapCache
 * @Description: Bitmap cache using LRU policy
 * @Author: hubohua
 * @CreateDate: 2018/4/12
 */
public class BitmapCache {
    private static final String TAG = BitmapCache.class.getSimpleName();

    private LruCache<String, SoftReference<Bitmap>> mMemoryCache;

    public BitmapCache(Context context) {
        initializeMemory();
    }

    private void initializeMemory() {
        int maxMemory =
                (int) Runtime.getRuntime().maxMemory();
        int cacheSize= maxMemory / 16;
        Log.d(TAG, "initializeMemory maxMemory: " + maxMemory + ", cacheSize:" + cacheSize);
        mMemoryCache = new BitmapLruCache(cacheSize);
    }

    /**
     * Add bitmap to memory cache by key
     * @param key
     * @param bitmap
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, new SoftReference<Bitmap>(bitmap));
        }
    }

    /**
     * Get bitmap from memory cache by key
     * @param key
     * @return
     */
    public Bitmap getBitmapFromMemCache(String key) {
        if (mMemoryCache.get(key) != null) {
            return mMemoryCache.get(key).get();
        }
        return null;
    }

    /**
     * Clear all cache memory
     */
    public void clearCache() {
        try {
            if (mMemoryCache.size() > 0) {
                mMemoryCache.evictAll(); // mLruCache.trimToSize(-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class BitmapLruCache extends LruCache<String, SoftReference<Bitmap>> {

        /**
         * @param maxSize for caches that do not override {@link #sizeOf}, this is
         *                the maximum number of entries in the cache. For all other caches,
         *                this is the maximum sum of the sizes of the entries in this cache.
         */
        public BitmapLruCache(int maxSize) {
            super(maxSize);
        }

        @Override
        protected void entryRemoved(boolean evicted, String key, SoftReference<Bitmap> oldValue, SoftReference<Bitmap> newValue) {
            Log.d(TAG, "entryRemoved: "
                    + evicted + ", " + key + ", " + oldValue + ", " + newValue);
            if (evicted) {
                if (oldValue != null && oldValue.get() != null) {
                    oldValue.get().recycle();
                    oldValue.clear();
                }
            }
        }

        @Override
        protected int sizeOf(String key, SoftReference<Bitmap> value) {
            if (value != null && value.get() != null) {
                return value.get().getByteCount();
            }
            return 0;
        }
    }
}
