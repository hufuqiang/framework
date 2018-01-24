package com.gale.rocketmq.message;

import com.gale.rocketmq.message.protocol.MessageEncoder;
import com.gale.rocketmq.message.protocol.impl.JsonMessageEncoder;

/**
 * Created by jiangyang on 2017/11/16.
 */
public class ZipkinSimpleMessage extends AbstractMessage<SimpleData> {

    public ZipkinSimpleMessage(String key, SimpleData data) {
        super("DiwayouTest", "test_tag_zipkin2", key, data);
    }

    @Override
    public MessageEncoder<SimpleData> getEncoder() {
        return JsonMessageEncoder.INSTANCE;
    }
}