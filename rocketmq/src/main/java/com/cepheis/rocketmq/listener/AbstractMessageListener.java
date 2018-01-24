package com.cepheis.rocketmq.listener;

import com.cepheis.rocketmq.handler.MessageHandler;
import com.cepheis.rocketmq.handler.MessageHandlerConcurrently;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.common.Pair;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class AbstractMessageListener implements MessageListener {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractMessageListener.class);

    protected Map<String/*topic*/, Map<String/*tag*/, MessageHandlerConcurrently>> topicTagHandlerRoute;

//    public void check() {
//        if (MapUtils.isEmpty(topicTagHandlerRoute)) {
//            throw new RuntimeException("必须设置topicTagHandlerRoute");
//        }
//    }

    public Pair<Integer, MessageHandlerConcurrently> getMessageHandler(MessageExt message) {
        String topic = message.getTopic();
        Map<String, MessageHandlerConcurrently> tagToHandler = topicTagHandlerRoute.get(topic);
        if (MapUtils.isEmpty(tagToHandler)) {
            return new Pair<>(MessageHandler.DELIVERY_ERROR, null);
        }
        MessageHandlerConcurrently messageHandler;
        String tag = message.getTags();
        if (StringUtils.isEmpty(tag)) {
            if (tagToHandler.size() != 1) {
                logger.error("Message doesn't have tag value but tagToHandler size() > 1,topic={}", topic);
                return new Pair<>(MessageHandler.NOT_FOUND, null);
            }
            Map.Entry<String, MessageHandlerConcurrently> defaultHandler = tagToHandler.entrySet().iterator().next();
            if (!defaultHandler.getKey().equals("*")) {
                logger.error("Message have one handler but tag is not *,topic={}", topic);
                return new Pair<>(MessageHandler.NOT_FOUND, null);
            }
            messageHandler = defaultHandler.getValue();
        } else {
            messageHandler = tagToHandler.get(tag);
            if (messageHandler == null) {
                logger.error("Can't find handler for msg={}", message);
                return new Pair<>(MessageHandler.DELIVERY_ERROR, null);
            }
        }
        return new Pair<>(MessageHandler.FOUND, messageHandler);
    }

    public Map<String, Map<String, MessageHandlerConcurrently>> getTopicTagHandlerRoute() {
        return topicTagHandlerRoute;
    }

    public void setTopicTagHandlerRoute(Map<String, Map<String, MessageHandlerConcurrently>> topicTagHandlerRoute) {
        this.topicTagHandlerRoute = topicTagHandlerRoute;
    }
}
