package com.student.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@EnableAutoConfiguration
public class RedisUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 存放对象
     * @param key
     * @param object
     */
    public void saveObject(final String key, Object object) {

        final byte[] vbytes = SerializeUtil.serialize(object);
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                connection.set(redisTemplate.getStringSerializer().serialize(key), vbytes);
                return null;
            }
        });
    }

    /**
     * 存放含有过期时间的对象
     * @param key
     * @param object
     * @param time
     * @param unit
     */
    public void saveObject4Expire(final String key, Object object, int time, TimeUnit unit) {

        final byte[] vbytes = SerializeUtil.serialize(object);
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                connection.set(redisTemplate.getStringSerializer().serialize(key), vbytes);
                redisTemplate.expire(key, time, unit);
                return null;
            }
        });
    }
    /**
     * 获取对象
     * @param key
     */
    public <T> T getObject(final String key, Class<T> elementType) {
        return redisTemplate.execute(new RedisCallback<T>() {
            @Override
            public T doInRedis(RedisConnection connection)
                    throws DataAccessException {
                byte[] keybytes = redisTemplate.getStringSerializer().serialize(key);
                if (connection.exists(keybytes)) {
                    byte[] valuebytes = connection.get(keybytes);
                    @SuppressWarnings("unchecked")
                    T value = (T) SerializeUtil.unserialize(valuebytes);
                    return value;
                }
                return null;
            }
        });
    }

    /**
     * 存放有过期时间的字符串
     * @param key
     * @param val
     * @param time
     * @param unit
     */
    public void setValue(String key, String val, int time, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, val, time, unit);
    }
    
    /**
     * 获取值
     * @param key
     * @return
     */
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void multiSet(Map<String, String> map) {
        redisTemplate.opsForValue().multiSet(map);
    }

    public List<String> multiGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    public long incr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public void lpush(String key, String value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    public List<String> range(String key, int start, int end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    public Object rpop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    public void setHash(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    public Object getHash(String key, String prop) {
        return redisTemplate.opsForHash().get(key, prop);
    }

    public Map getHashAll(String key) {
        Map map = new HashMap();
        map.put("keys", redisTemplate.opsForHash().keys(key));
        map.put("vals", redisTemplate.opsForHash().values(key));
        return map;
    }

}
