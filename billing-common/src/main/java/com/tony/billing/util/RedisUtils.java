package com.tony.billing.util;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author tonyjiang
 */
@Component
public class RedisUtils {


    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public RedisUtils(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    private static Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    @Value("${redis.cache.transient.time:300}")
    private long transientTime;

    /**
     * 设置对象持久化
     *
     * @param key 键值
     * @param val 内容
     * @return
     */
    public boolean set(final String key, final Object val) {
        Boolean result = false;
        try {
            result = stringRedisTemplate.opsForValue().setIfAbsent(key, JSON.toJSONString(val));
        } catch (Exception e) {
            logger.error("设置cache错误", e);
        }

        return result != null && result;
    }

    /**
     * 设置对象存活时间,单位秒
     *
     * @param key  键值
     * @param val  内容
     * @param time 过期时间
     */
    public void set(final String key, final Object val, final long time) {
        Preconditions.checkNotNull(key, "key can not be null");
        try {
            stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(val), time);
        } catch (Exception e) {
            logger.error("设置定时cache错误", e);
        }

    }


    /**
     * 获取对象
     *
     * @param key   键值
     * @param clazz 对象类
     * @return 返回键值对
     */
    public <E> Optional<E> get(final String key, Class<E> clazz) {
        Optional<E> result = Optional.empty();
        try {
            String stringValue = stringRedisTemplate.opsForValue().get(key);
            if (StringUtils.isNotEmpty(stringValue)) {
                return Optional.of(JSON.parseObject(stringValue, clazz));
            }
        } catch (Exception e) {
            logger.error("获取缓存信息失败", e);
        }

        return result;
    }

    /**
     * 递增计数器
     *
     * @param key 键值
     * @param by  增加值
     * @return 增加后的值
     */
    public Long incr(final String key, final Long by) {
        return stringRedisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
            byte[] keys = serializer.serialize(key);
            return connection.incrBy(keys, by == null ? 1 : by);
        });
    }

    /**
     * 删除对象
     *
     * @param key 键
     */
    public boolean del(final String key) {
        return stringRedisTemplate.delete(key);
    }

    /**
     * 以hash表的模式保存对象
     *
     * @param hKey     hash key
     * @param fieldKey fieldKey字段key
     * @param value    对象值
     * @return
     */
    public boolean hSet(final String hKey, final String fieldKey, Object value) {
        stringRedisTemplate.opsForHash().put(hKey, fieldKey, JSON.toJSONString(value));
        return true;
    }

    /**
     * 模拟过期的方式以hash表保存对象
     *
     * @param hKey     hashKey
     * @param fieldKey 字段key
     * @param value    对象值
     * @param time     过期时间
     * @return
     */
    public boolean hSetEx(final String hKey, final String fieldKey, Object value, long time) {
        this.set(hKey + fieldKey, 1, time);
        return this.hSet(hKey, fieldKey, value);
    }

    /**
     * 获取hash表中的数据
     *
     * @param hKey     hashKey
     * @param fieldKey 字段key
     * @param clazz    对象类型
     * @return
     */
    public <E> Optional<E> hGet(final String hKey, final String fieldKey, Class<E> clazz) {
        Optional<E> result = Optional.empty();
        try {
            String stringValue = (String)stringRedisTemplate.opsForHash().get(hKey, fieldKey);
            if (StringUtils.isNotEmpty(stringValue)) {
                return Optional.of(JSON.parseObject(stringValue, clazz));
            }
        } catch (Exception e) {
            logger.error("获取缓存信息失败", e);
        }
        return result;
    }

    /**
     * 模拟过期时间的获取hash表中的值
     *
     * @param hKey     hashKey
     * @param fieldKey 字段key
     * @param clazz    对象类型
     * @return
     */
    public <E> Optional<E> hGetEx(final String hKey, final String fieldKey, Class<E> clazz) {
        if (get(hKey + fieldKey, Integer.class).isPresent()) {
            return hGet(hKey, fieldKey, clazz);
        } else {
            hDel(hKey, fieldKey);
            return Optional.empty();
        }
    }


    /**
     * 删除对象
     *
     * @param hKey     hash键
     * @param fieldKey 字段key
     */
    public boolean hDel(final String hKey, final String fieldKey) {
        return stringRedisTemplate.opsForHash().get(hKey, fieldKey) == null || stringRedisTemplate.opsForHash().delete(hKey, fieldKey) > 0;
    }

    public long getTransientTime() {
        return transientTime;
    }

    public void setTransientTime(long transientTime) {
        this.transientTime = transientTime;
    }
}
