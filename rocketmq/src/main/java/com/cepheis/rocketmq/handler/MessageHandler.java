package com.cepheis.rocketmq.handler;

import com.cepheis.rocketmq.message.protocol.MessageDecoder;

/**
 * Created by diwayou on 2015/10/29.
 */
public interface MessageHandler<T> {

    int FOUND = 1;

    int NOT_FOUND = 2;

    int DELIVERY_ERROR = 3;

    MessageDecoder<T> getMessageDecoder();
}
