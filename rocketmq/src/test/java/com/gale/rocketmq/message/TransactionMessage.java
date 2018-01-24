package com.gale.rocketmq.message;


import com.gale.rocketmq.message.protocol.MessageEncoder;
import com.gale.rocketmq.message.protocol.impl.JsonMessageEncoder;

/**
 * Created by diwayou on 2015/10/29.
 */
public class TransactionMessage extends AbstractMessage<SimpleData> {

    public TransactionMessage(String key, SimpleData data) {
        super("DiwayouTest", "trans_tag", key, data);
    }

    @Override
    public MessageEncoder<SimpleData> getEncoder() {
        return JsonMessageEncoder.INSTANCE;
    }
}
