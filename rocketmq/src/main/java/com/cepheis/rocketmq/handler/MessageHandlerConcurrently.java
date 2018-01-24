package com.cepheis.rocketmq.handler;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * Created by diwayou on 2015/12/8.
 */
public interface MessageHandlerConcurrently<T> extends MessageHandler<T> {

    ConsumeConcurrentlyStatus process(T data, MessageExt message, ConsumeConcurrentlyContext context);
}
