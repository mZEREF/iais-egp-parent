/*
 * This file is generated by ECQ project skeleton automatically.
 *
 *   Copyright 2019-2049, Ecquaria Technologies Pte Ltd. All rights reserved.
 *
 *   No part of this material may be copied, reproduced, transmitted,
 *   stored in a retrieval system, reverse engineered, decompiled,
 *   disassembled, localised, ported, adapted, varied, modified, reused,
 *   customised or translated into any language in any form or by any means,
 *   electronic, mechanical, photocopying, recording or otherwise,
 *   without the prior written permission of Ecquaria Technologies Pte Ltd.
 */

package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * RedisCacheHelper
 *
 * @author Jinhua
 * @date 2019/7/25 15:35
 */
@Component
public class RedisCacheHelper {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public static final  long SESSION_DEFAULT_EXPIRE = 60L * 30L;
    /**  set do not expire */
    public static final  long NOT_EXPIRE = -1;

    public static RedisCacheHelper getInstance() {
        return SpringContextHelper.getContext().getBean(RedisCacheHelper.class);
    }

    public void set(String key, Object value, long expire){
        this.redisTemplate.opsForValue().set(key, value);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public void set(String key, Object value){
        set(key, value, SESSION_DEFAULT_EXPIRE);
    }

    public void set(String cacheName, String key, Object value){
        set(cacheName + ":" + key, value, SESSION_DEFAULT_EXPIRE);
    }

    public void set(String cacheName, String key, Object value, long expire){
        set(cacheName + ":" + key, value, expire);
    }

    public <T> T get(String key, long expire) {
        Object value = this.redisTemplate.opsForValue().get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }

        return value == null ? null : (T) value;
    }

    public <T> T get(String key) {
        return get(key, NOT_EXPIRE);
    }

    public <T> T get(String cacheName, String key){
        return get(cacheName + ":" + key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void delete(String cacheName, String key) {
        delete(cacheName + ":" + key);
    }

    public void clear(String cacheName) {
        Set<String> keys = redisTemplate.keys(cacheName + ":");
        if (CollectionUtils.isNotEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }
}
