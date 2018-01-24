package com.cepheis.rocketmq.handler;

import com.cepheis.rocketmq.message.SimpleData;
import com.cepheis.rocketmq.message.protocol.MessageDecoder;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

/**
 * Created by diwayou on 2015/10/29.
 */
@Component("transMessageCheckHandler")
public class TransMessageCheckHandler implements TransactionCheckHandler<SimpleData> {

    @Override
    public LocalTransactionState process(SimpleData data, MessageExt message) {
        System.out.println("Received check transaction message from " + message.getTags() + " " + data);

        return LocalTransactionState.COMMIT_MESSAGE;
    }

    @Override
    public MessageDecoder<SimpleData> getMessageDecoder() {
        return JsonMessageDecoderFactory.simpleDataDecoder;
    }
}
