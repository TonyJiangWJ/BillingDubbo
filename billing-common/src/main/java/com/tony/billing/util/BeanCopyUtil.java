package com.tony.billing.util;


import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author by TonyJiang on 2017/6/11.
 */
public class BeanCopyUtil {
    private static final Logger logger = LoggerFactory.getLogger(BeanCopyUtil.class);

    @SuppressWarnings("unchecked")
    public static <S, T> T copy(S s, Class<T> t) {
        if ((s == null) || (t == null)) {
            return null;
        }
        try {
            T nt = t.newInstance();
            PropertyUtils.copyProperties(nt, s);
            return nt;
        } catch (InstantiationException e) {
            logger.error("========= 拷贝时发生实例化对象异常 {}", e.toString());
            return null;
        } catch (IllegalAccessException e) {
            logger.error("========= 拷贝时发生非法访问异常异常 {}", e.toString());
            return null;
        } catch (InvocationTargetException e) {
            logger.error("========= 拷贝时发生调用目标对象异常 {}", e.toString());
            return null;
        } catch (Exception e) {
            logger.error("======== 对象 {} 和 {} 拷贝是发生错误 {}", s.getClass(), t, e.toString());
        }
        return null;
    }

    public static <S, T> List<T> copy(List<S> s, Class<T> t) {
        List<T> lnt = new ArrayList<T>();
        for (Object ss : s) {
            lnt.add(copy(ss, t));
        }
        return lnt;
    }

    public static <S, T> List<T> copy(Collection<S> s, Class<T> t) {
        List<T> lnt = new ArrayList<T>();
        for (Object ss : s) {
            lnt.add(copy(ss, t));
        }
        return lnt;
    }

    public static <S, T, N> Map<S, List<N>> copy(Map<S, List<T>> s, Class<N> t) {
        Map<S, List<N>> map = new HashMap<>();
        Set<Map.Entry<S, List<T>>> set = s.entrySet();
        for (Map.Entry<S, List<T>> e : set) {
            map.put(e.getKey(), copy(e.getValue(), t));
        }
        return map;
    }

    public static <S, T> Map<S, T> copyMap(Map<S, T> map, Class<T> t) {
        Map<S, T> m = new HashMap<>();
        Set<Map.Entry<S, T>> set = map.entrySet();
        for (Map.Entry<S, T> e : set) {
            m.put(e.getKey(), copy(e.getValue(), t));
        }
        return m;
    }

    public static <T> void copySameObject(T dest, T src) {
        try {
            PropertyUtils.copyProperties(dest, src);
        } catch (Exception e) {
            logger.error("相同对象值拷贝是发生异常 {}", e);
        }
    }
}

