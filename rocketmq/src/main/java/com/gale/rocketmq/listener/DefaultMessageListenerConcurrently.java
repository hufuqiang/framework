package com.gale.rocketmq.listener;

import com.gale.rocketmq.handler.MessageHandler;
import com.gale.rocketmq.handler.MessageHandlerConcurrently;
import com.gale.rocketmq.message.protocol.MessageDecoder;
import com.gale.rocketmq.util.ConsumeResult;
import com.tgou.monitor.zpkin.rocketmq.Zipk2RtMqConsumerInterceptor;
import com.tiangou.baseCommon.mapper.JsonMapper;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.Pair;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

import static com.gale.rocketmq.util.ConsumerSpanTagsUtil.tagDeliverErr;
import static com.gale.rocketmq.util.ConsumerSpanTagsUtil.tagDeliverNotFound;
import static com.tgou.monitor.zpkin.util.InterceptIfNotNull.*;

/**
 * 该listener每次只消费一个消息，也就是msgs的大小是1。
 * 当ConsumerConfig中consumeMessageBatchMaxSize大于1，不能使用该listener
 * Created by diwayou on 2015/10/29.
 */
public class DefaultMessageListenerConcurrently extends AbstractMessageListener implements MessageListenerConcurrently {

    public DefaultMessageListenerConcurrently() {
        
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        for (MessageExt message : msgs) {
            ConsumeResult result = consumeMessage(message, context, msgs);
            if (result.t == null) {
                completeProcess(zipk2RtMqConsumerInterceptor, message, result.consumeStatus, null);//2
            } else {
                exceptionProcess(zipk2RtMqConsumerInterceptor, message, result.t, null);//3
            }
            return result.consumeStatus;
        }

        throw new RuntimeException("Because consume one message once, code must not execute here.");
    }

    private ConsumeResult consumeMessage(MessageExt message, ConsumeConcurrentlyContext context, List<MessageExt> msgs) {
        ConsumeResult res = checkgAndGetMessageHandler(message, context);
        if (res.consumeStatus != null || res.messageHandler == null) {
            return res;
        }
        MessageHandlerConcurrently messageHandler = res.messageHandler;
        MessageDecoder decoder = messageHandler.getMessageDecoder();
        try {
            ConsumeConcurrentlyStatus status = messageHandler.process(decoder.decode(message.getBody()), message, context);
            res.consumeStatus = status;
        } catch (Throwable t) {
            logger.warn("处理消息失败: msg={}", JsonMapper.buildNonNullMapper().toJson(msgs), t);
            context.setDelayLevelWhenNextConsume(10); // 6 minutes later重新消费
            res.consumeStatus = ConsumeConcurrentlyStatus.RECONSUME_LATER;
            res.t = t;
        }
        return res;
    }

    private ConsumeResult checkgAndGetMessageHandler(MessageExt message, ConsumeConcurrentlyContext context) {
        ConsumeResult result = ConsumeResult.create(message);

        Pair<Integer, MessageHandler> handlerPair = getMessageHandler(message);
        switch (handlerPair.getObject1()) {
            case MessageHandler.DELIVERY_ERROR:
                tagDeliverErr(result.zipkinTags);
                result.consumeStatus = ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                break;
            case MessageHandler.NOT_FOUND:
                context.setDelayLevelWhenNextConsume(10); // 6 minutes later重新消费
                tagDeliverNotFound(result.zipkinTags);
                result.consumeStatus = ConsumeConcurrentlyStatus.RECONSUME_LATER;
                break;
            case MessageHandler.FOUND:
                result.messageHandler = (MessageHandlerConcurrently) handlerPair.getObject2();
                break;
            default:
                throw new IllegalStateException("Can't arrive here.");
        }
        return result;
    }

    public Zipk2RtMqConsumerInterceptor getZipk2RtMqConsumerInterceptor() {
        return zipk2RtMqConsumerInterceptor;
    }

    public void setZipk2RtMqConsumerInterceptor(Zipk2RtMqConsumerInterceptor zipk2RtMqConsumerInterceptor) {
        this.zipk2RtMqConsumerInterceptor = zipk2RtMqConsumerInterceptor;
    }
}
