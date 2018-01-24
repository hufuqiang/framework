package com.gale.rocketmq.handler;

import com.gale.rocketmq.message.protocol.MessageDecoder;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * Created by diwayou on 2015/10/29.
 */
public interface TransactionCheckHandler<T> {

    LocalTransactionState process(T data, MessageExt message);

    MessageDecoder<T> getMessageDecoder();
}