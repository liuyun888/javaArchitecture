/*
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.demo.gis.common.toolUtil;

import com.google.common.collect.Lists;

import java.io.*;
import java.util.Collection;

/**
 * <p>
 * IO 工具类
 * </p>
 *
 * @author hubin
 * @since 2018-03-28
 */
public class IoUtils {

    public static byte[] getBytesFromFile(String file) throws IOException {
        return getBytes(new FileInputStream(file));
    }

    public static byte[] getBytesFromResource(String resource) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return getBytes(classLoader.getResourceAsStream(resource));
    }

    public static void writeFile(String file, byte[] bytes) throws IOException {
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(file);
            output.write(bytes);
        } finally {
            IoUtils.close(output);
        }
    }

    public static byte[] getBytes(InputStream input) throws IOException {
        ByteArrayOutputStream output = null;
        try {
            output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) >= 0) {
                output.write(buffer, 0, length);
            }
            return output.toByteArray();
        } finally {
            IoUtils.close(output, input);
        }
    }

    /**
     * <p>
     * 关闭流对象
     * </p>
     *
     * @param closeables 可关闭的流对象列表
     * @return IOException
     */
    public static IOException close(Closeable... closeables) {
        return close(Lists.newArrayList(closeables));
    }

    /**
     * <p>
     * 关闭流对象
     * </p>
     *
     * @param closeables 可关闭的流对象列表
     * @return IOException
     */
    public static IOException close(Collection<? extends Closeable> closeables) {
        if (CollectionUtils.isEmpty(closeables)) {
            return null;
        }
        IOException exception = null;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    // catch到之后继续保证后面没问题的一定会关掉
                    exception = e;
                }
            }
        }
        return exception;
    }

}
