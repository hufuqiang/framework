package com.cepheis.rocketmq.message.protocol.impl;

import com.cepheis.rocketmq.message.protocol.MessageEncoder;

import java.nio.charset.Charset;

/**
 * Created by diwayou on 2015/10/29.
 */
public class JsonMessageEncoder<T> implements MessageEncoder<T> {

    public static final JsonMessageEncoder INSTANCE = new JsonMessageEncoder();

    @Override
    public byte[] encode(T message) {
        return JsonMapper.buildNonNullMapper().toJson(message).getBytes(Charset.forName("UTF-8"));
    }
}
