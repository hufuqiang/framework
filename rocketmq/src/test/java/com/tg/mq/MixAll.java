package com.tg.mq;

import com.google.common.collect.Maps;
import com.tiangou.baseCommon.mapper.JsonMapper;
import org.apache.commons.collections.MapUtils;
import org.junit.Test;

import java.util.Map;

/**
 * Created by diwayou on 2015/10/29.
 */
public class MixAll {

    @Test
    public void jsonParseMapTest() {
        Map<String, String> map = Maps.newHashMap();
        map.put("1", "one");
        map.put("2", "two");

        String message = JsonMapper.buildNonNullMapper().toJson(map);
        System.out.println(message);

        Map<String, String> parsedMap = JsonMapper.buildNonNullMapper().fromJson(message, Map.class);
        System.out.println(map);
    }
}
