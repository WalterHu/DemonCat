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

/**
 * @Class: Configuration
 * @Description: Configuration params for http
 * @Author: hubohua
 * @CreateDate: 2018/4/12
 */
public class Configuration {
    public static final String HTTP_CACHE_DIR = "http_cache";
    public static final int HTTP_CACHE_SIZE = 50 * 1024 * 1024; // 50Mb
    public static final int HTTP_CONNECT_TIMEOUT = 20; // 20s
    public static final int HTTP_READ_WRITE_TIMEOUT = 20; // 20s
    public static final int HTTP_CACHE_MAX_AGE = 60; // 60s when has network for cache
    public static final int HTTP_CACHE_MAX_STALE = 60 * 60; // 1h when no network for cache
    public static final String HTTP_BASE_URL = "http://www.demoncat.com/"; // the base url should has '/' at end
}
