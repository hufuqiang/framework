package com.gale.rocketmq.handler;

import com.gale.rocketmq.message.SimpleData;
import com.gale.rocketmq.message.protocol.MessageDecoder;
import com.gale.rocketmq.message.protocol.impl.JsonMessageDecoder;

/**
 * Created by diwayou on 2015/10/30.
 */
public class JsonMessageDecoderFactory {

    public static final MessageDecoder<SimpleData> simpleDataDecoder = new JsonMessageDecoder<SimpleData>(SimpleData.class);
}
