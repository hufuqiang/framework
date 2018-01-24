package com.cepheis.rocketmq.handler;

import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * Created by diwayou on 2015/12/8.
 */
public interface MessageHandlerOrderly<T> extends MessageHandler<T> {

    ConsumeOrderlyStatus process(T data, MessageExt message, ConsumeOrderlyContext context);
}
