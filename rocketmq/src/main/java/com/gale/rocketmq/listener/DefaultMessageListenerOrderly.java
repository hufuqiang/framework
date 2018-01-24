package com.gale.rocketmq.listener;

import com.gale.rocketmq.handler.MessageHandler;
import com.gale.rocketmq.handler.MessageHandlerOrderly;
import com.gale.rocketmq.message.protocol.MessageDecoder;
import com.tiangou.baseCommon.mapper.JsonMapper;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.Pair;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by diwayou on 2015/10/29.
 */
public class DefaultMessageListenerOrderly extends AbstractMessageListener implements MessageListenerOrderly {

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
        for (MessageExt message : msgs) {
            Pair<Integer, MessageHandler> handlerPair = getMessageHandler(message);
            MessageHandlerOrderly messageHandler;
            switch (handlerPair.getObject1()) {
                case MessageHandler.DELIVERY_ERROR:
                    return ConsumeOrderlyStatus.SUCCESS;

                case MessageHandler.NOT_FOUND:
                    // 10分钟后重新消费
                    context.setSuspendCurrentQueueTimeMillis(TimeUnit.MINUTES.toMillis(10));
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;

                case MessageHandler.FOUND:
                    messageHandler = (MessageHandlerOrderly) handlerPair.getObject2();
                    break;

                default:
                    throw new IllegalStateException("Can't arrive here.");
            }

            MessageDecoder decoder = messageHandler.getMessageDecoder();

            try {
                return messageHandler.process(decoder.decode(message.getBody()), message, context);
            } catch (Throwable t) {
                logger.warn("处理消息失败: msg={}", JsonMapper.buildNonNullMapper().toJson(msgs), t);

                // 10分钟后重新消费
                context.setSuspendCurrentQueueTimeMillis(TimeUnit.MINUTES.toMillis(10));
                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            }
        }

        throw new RuntimeException("Because consume one message once, code must not execute here.");
    }

}
