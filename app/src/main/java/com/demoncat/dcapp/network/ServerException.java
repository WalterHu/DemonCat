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
 * @Class: ServerException
 * @Description: Common sever exception definition wrapper of error code.
 *               Used for custom error code in server protocol.
 * @Author: hubohua
 * @CreateDate: 2018/4/13
 */
public class ServerException extends RuntimeException {
    public String code;

    public ServerException(String code) {
        this.code = code;
    }

    /*-------Common Error Definition--------*/
    public static final String ERROR_CODE_SUCCESS = "0200";

}
