package com.gale.rocketmq.message;


import com.gale.rocketmq.message.protocol.MessageEncoder;
import com.gale.rocketmq.message.protocol.impl.JsonMessageEncoder;

/**
 * Created by diwayou on 2015/10/29.
 */
public class SimpleMessage extends AbstractMessage<SimpleData> {

    public SimpleMessage(String key, SimpleData data) {
        super("DiwayouTest", "test_tag", key, data);
    }

    @Override
    public MessageEncoder<SimpleData> getEncoder() {
        return JsonMessageEncoder.INSTANCE;
    }
}
