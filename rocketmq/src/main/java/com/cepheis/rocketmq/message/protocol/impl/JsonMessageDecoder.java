package com.cepheis.rocketmq.message.protocol.impl;

import com.cepheis.rocketmq.message.protocol.MessageDecoder;

import java.nio.charset.Charset;

/**
 * Created by diwayou on 2015/10/29.
 */
public class JsonMessageDecoder<T> implements MessageDecoder<T> {

    private Class<T> resultClass;

    public JsonMessageDecoder(Class resultClass) {
        this.resultClass = resultClass;
    }

    @Override
    public T decode(byte[] message) {
        return JsonMapper.buildNonNullMapper().fromJson(new String(message, Charset.forName("UTF-8")), resultClass);
    }
}
