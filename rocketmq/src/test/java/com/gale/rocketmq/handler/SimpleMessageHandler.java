package com.gale.rocketmq.handler;

import com.gale.rocketmq.message.SimpleData;
import com.gale.rocketmq.message.protocol.MessageDecoder;
import com.gale.rocketmq.message.protocol.impl.JsonMessageDecoder;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by diwayou on 2015/10/29.
 */
@Component("simpleMessageHandler")
public class SimpleMessageHandler implements MessageHandlerConcurrently<SimpleData> {

    private static final MessageDecoder<SimpleData> messageDecoder = new JsonMessageDecoder<SimpleData>(SimpleData.class);

    @Override
    public ConsumeConcurrentlyStatus process(SimpleData data, MessageExt message, ConsumeConcurrentlyContext context) {
        System.out.println("Received simple message from " + message.getTags() + " " + data);

        try {
            TimeUnit.MINUTES.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
    }

    @Override
    public MessageDecoder<SimpleData> getMessageDecoder() {
        return messageDecoder;
    }
}
