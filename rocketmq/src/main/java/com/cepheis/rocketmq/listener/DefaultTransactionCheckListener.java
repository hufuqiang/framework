package com.cepheis.rocketmq.listener;

import com.cepheis.rocketmq.handler.TransactionCheckHandler;
import com.cepheis.rocketmq.message.protocol.MessageDecoder;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionCheckListener;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by diwayou on 2015/10/29.
 */
public class DefaultTransactionCheckListener implements TransactionCheckListener {

    private static final Logger logger = LoggerFactory.getLogger(DefaultTransactionCheckListener.class);

    private Map<String/*topic*/, Map<String/*tag*/, TransactionCheckHandler>> topicTagHandlerRoute;

    @Override
    public LocalTransactionState checkLocalTransactionState(MessageExt msg) {
        String topic = msg.getTopic();

        Map<String, TransactionCheckHandler> tagToHandler = topicTagHandlerRoute.get(topic);
        if (MapUtils.isEmpty(tagToHandler)) {
            String errMsg = "Can't find tagToHandler for topic=" + topic;
            logger.error(errMsg);
            return LocalTransactionState.UNKNOW;
        }

        TransactionCheckHandler transactionCheckHandler;
        String tag = msg.getTags();
        if (StringUtils.isEmpty(tag)) {
            if (MapUtils.isEmpty(tagToHandler)) {
                String errMsg = "Can't find handler for topic=" + topic;
                logger.error(errMsg);

                return LocalTransactionState.UNKNOW;
            }
            if (tagToHandler.size() != 1) {
                String errMsg = "Message doesn't have tag value but tagToHandler size() > 1,topic=" + topic;
                logger.error(errMsg);

                return LocalTransactionState.UNKNOW;
            }

            Map.Entry<String, TransactionCheckHandler> defaultHandler = tagToHandler.entrySet().iterator().next();
            if (!defaultHandler.getKey().equals("*")) {
                String errMsg = "Message have one handler but tag is not *,topic=" + topic;
                logger.error(errMsg);

                return LocalTransactionState.UNKNOW;
            }

            transactionCheckHandler = defaultHandler.getValue();
        } else {
            transactionCheckHandler = tagToHandler.get(tag);
            if (transactionCheckHandler == null) {
                String errMsg = "Can't find handler for topic=" + topic + " tag=" + tag;
                logger.error(errMsg);

                return LocalTransactionState.UNKNOW;
            }
        }

        MessageDecoder decoder = transactionCheckHandler.getMessageDecoder();

        return transactionCheckHandler.process(decoder.decode(msg.getBody()), msg);
    }

    public Map<String, Map<String, TransactionCheckHandler>> getTopicTagHandlerRoute() {
        return topicTagHandlerRoute;
    }

    public void setTopicTagHandlerRoute(Map<String, Map<String, TransactionCheckHandler>> topicTagHandlerRoute) {
        this.topicTagHandlerRoute = topicTagHandlerRoute;
    }
}
