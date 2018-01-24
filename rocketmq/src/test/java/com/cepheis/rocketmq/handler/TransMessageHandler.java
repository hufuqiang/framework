package com.cepheis.rocketmq.handler;

import com.cepheis.rocketmq.message.SimpleData;
import com.cepheis.rocketmq.message.protocol.MessageDecoder;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

/**
 * Created by diwayou on 2015/10/29.
 */
@Component("transMessageHandler")
public class TransMessageHandler implements MessageHandlerConcurrently<SimpleData> {

    @Override
    public ConsumeConcurrentlyStatus process(SimpleData data, MessageExt message, ConsumeConcurrentlyContext context) {
        System.out.println("Received Transaction message from " + message.getTags() + " " + data);

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    @Override
    public MessageDecoder<SimpleData> getMessageDecoder() {
        return JsonMessageDecoderFactory.simpleDataDecoder;
    }
}
