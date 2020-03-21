package com.demo.gis.common.toolUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author zhaokai
 * @since 2019-07-04
 */
public class CollectionUtils {

    public static String toString(Collection<?> collection) {
        return join(collection);
    }

    /**
     * 判断集合是否为空或者是否存在元素
     *
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 分割 List 为多个 List
     *
     * @param dataList
     * @param delta
     */
    public static <T> List<List<T>> subList(List<T> dataList, int delta) {
        List<List<T>> totalList = new ArrayList<>();
        // 限制条数
        if (!isEmpty(dataList)) {
            int size = dataList.size();
            if (delta < size) {
                int part = size / delta + (size % delta == 0 ? 0 : 1);
                // 开始拆分
                for (int i = 0; i < part; i++) {
                    int begin = i * delta;
                    int end = (i + 1) * delta;
                    if (end > size) {
                        end = size;
                    }
                    List<T> listPage = dataList.subList(begin, end);
                    totalList.add(listPage);
                }
            } else {
                totalList.add(dataList);
            }
        }
        return totalList;
    }

    /**
     * 将集合中的元素以英文逗号分隔拼接为字符串返回
     *
     * @param collection
     * @return {@link String}
     */
    public static String join(Collection<?> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return "[]";
        }
        // 返回结果
        StringBuilder result = new StringBuilder("[");
        // 集合长度
        int collectionSize = collection.size();
        // 当前索引
        int index = 0;
        for (Object o : collection) {
            result.append(o.toString());
            if (!(++index == collectionSize)) {
                result.append(",");
            }
        }
        return result.append("]").toString();
    }


    /**
     * 将集合中的元素以英文逗号分隔拼接为字符串返回
     *
     * @param collection
     * @return {@link String}
     */
    public static String join(Collection<?> collection, String splitChar) {
        if (CollectionUtils.isEmpty(collection)) {
            return "";
        }
        // 返回结果
        StringBuilder result = new StringBuilder();
        // 集合长度
        int collectionSize = collection.size();
        // 当前索引
        int index = 0;
        for (Object o : collection) {
            result.append(o.toString());
            if (!(++index == collectionSize)) {
                result.append(splitChar);
            }
        }
        return result.toString();
    }

}
