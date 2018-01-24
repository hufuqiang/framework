package com.cepheis.rocketmq.message.protocol;

/**
 * Created by diwayou on 2015/10/29.
 */
public interface MessageDecoder<T> {

    T decode(byte[] message);
}
