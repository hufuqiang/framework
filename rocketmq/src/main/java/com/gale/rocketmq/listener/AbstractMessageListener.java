package com.gale.rocketmq.listener;

import com.gale.rocketmq.handler.MessageHandler;
import com.gale.rocketmq.handler.MessageHandlerConcurrently;
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

    protected Map<String/*topic*/, Map<String/*tag*/, MessageHandler>> topicTagHandlerRoute;

    public void check() {
        if (MapUtils.isEmpty(topicTagHandlerRoute)) {
            throw new RuntimeException("必须设置topicTagHandlerRoute");
        }
    }

    public Pair<Integer, MessageHandler> getMessageHandler(MessageExt message) {
        String topic = message.getTopic();

        Map<String, MessageHandler> tagToHandler = topicTagHandlerRoute.get(topic);
        if (MapUtils.isEmpty(tagToHandler)) {
            String errMsg = "Can't find tagToHandler for topic=" + topic;
            logger.error(errMsg);

            return new Pair<Integer, MessageHandler>(MessageHandler.DELIVERY_ERROR, null);
        }

        MessageHandlerConcurrently messageHandler;
        String tag = message.getTags();
        if (StringUtils.isEmpty(tag)) {
            if (MapUtils.isEmpty(tagToHandler)) {
                String errMsg = "Can't find handler for topic=" + topic;
                logger.error(errMsg);

                return new Pair(MessageHandler.NOT_FOUND, null);
            }
            if (tagToHandler.size() != 1) {
                String errMsg = "Message doesn't have tag value but tagToHandler size() > 1,topic=" + topic;
                logger.error(errMsg);

                return new Pair<Integer, MessageHandler>(MessageHandler.NOT_FOUND, null);
            }

            Map.Entry<String, MessageHandler> defaultHandler = tagToHandler.entrySet().iterator().next();
            if (!defaultHandler.getKey().equals("*")) {
                String errMsg = "Message have one handler but tag is not *,topic=" + topic;
                logger.error(errMsg);

                return new Pair<Integer, MessageHandler>(MessageHandler.NOT_FOUND, null);
            }

            messageHandler = (MessageHandlerConcurrently) defaultHandler.getValue();
        } else {
            messageHandler = (MessageHandlerConcurrently) tagToHandler.get(tag);
            if (messageHandler == null) {
                String errMsg = "Can't find handler for msg=" + message.toString();
                logger.error(errMsg);

                // broker delivery error because tag is indexed by hash
                return new Pair<Integer, MessageHandler>(MessageHandler.DELIVERY_ERROR, null);
            }
        }

        return new Pair<Integer, MessageHandler>(MessageHandler.FOUND, messageHandler);
    }

    public Map<String, Map<String, MessageHandler>> getTopicTagHandlerRoute() {
        return topicTagHandlerRoute;
    }

    public void setTopicTagHandlerRoute(Map<String, Map<String, MessageHandler>> topicTagHandlerRoute) {
        this.topicTagHandlerRoute = topicTagHandlerRoute;
    }
}
