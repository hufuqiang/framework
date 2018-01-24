package com.cepheis.rocketmq.handler;

import com.cepheis.rocketmq.message.SimpleData;
import com.cepheis.rocketmq.message.protocol.MessageDecoder;
import com.cepheis.rocketmq.message.protocol.impl.JsonMessageDecoder;

/**
 * Created by diwayou on 2015/10/30.
 */
public class JsonMessageDecoderFactory {

    public static final MessageDecoder<SimpleData> simpleDataDecoder = new JsonMessageDecoder<SimpleData>(SimpleData.class);
}
