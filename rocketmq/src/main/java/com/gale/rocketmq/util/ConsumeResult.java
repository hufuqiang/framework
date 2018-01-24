//package com.gale.rocketmq.util;
//
//import com.gale.rocketmq.handler.MessageHandlerConcurrently;
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
//import org.apache.rocketmq.common.message.MessageExt;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class ConsumeResult {
//
//    public MessageExt message;
//
//    public ConsumeConcurrentlyStatus consumeStatus;
//
//    public Map<String, String> zipkinTags = new HashMap<>();
//
//    public Throwable t;
//
//    public MessageHandlerConcurrently messageHandler;
//
//    public static ConsumeResult create(MessageExt message) {
//        ConsumeResult res = new ConsumeResult();
//        res.message = message;
//        return res;
//    }
//}
