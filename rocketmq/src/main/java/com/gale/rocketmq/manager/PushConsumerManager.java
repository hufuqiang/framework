package com.gale.rocketmq.manager;

import com.google.common.collect.Lists;
import com.gale.rocketmq.config.ConsumerConfig;
import com.gale.rocketmq.handler.MessageHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by diwayou on 2015/10/29.
 */
public class PushConsumerManager {

    private static final Logger logger = LoggerFactory.getLogger(PushConsumerManager.class);

    private DefaultMQPushConsumer pushConsumer;

    private ConsumerConfig consumerConfig;

    public void init() {
        initConfig();
        try {
            subscribe();
            pushConsumer.start();
        } catch (MQClientException e) {
            throw new RuntimeException("PushConsumerManager start fail.", e);
        }
        logger.info("PushConsumerManager start success.");
    }

    private void subscribe() throws MQClientException {
        Map<String, Map<String, MessageHandler>> route = consumerConfig.getMessageListener().getTopicTagHandlerRoute();

        for (Map.Entry<String, Map<String, MessageHandler>> entry : route.entrySet()) {
            String topic = entry.getKey();
            List<String> tags = Lists.newArrayListWithCapacity(entry.getValue().size());
            tags.addAll(entry.getValue().keySet());

            if (CollectionUtils.isEmpty(tags) || (tags.size() == 1 && tags.get(0).equals("*"))) {
                pushConsumer.subscribe(topic, "*");

                if (logger.isInfoEnabled()) {
                    logger.info("Subscribe topic={},pattern={}", topic, "*");
                }
            } else {
                StringBuilder tagPattern = new StringBuilder();
                for (int idx = 0; idx < tags.size() - 1; idx++) {
                    tagPattern.append(tags.get(idx));
                    tagPattern.append(" || ");
                }
                tagPattern.append(tags.get(tags.size() - 1));

                pushConsumer.subscribe(topic, tagPattern.toString());

                if (logger.isInfoEnabled()) {
                    logger.info("Subscribe topic={},pattern={}", topic, tagPattern.toString());
                }
            }
        }
    }

    private void initConfig() {
        if (consumerConfig == null) {
            throw new RuntimeException("必须设置ClientConfig");
        }

        if (StringUtils.isEmpty(consumerConfig.getConsumerGroup())) {
            throw new RuntimeException("必须设置consumerGroup");
        }

        if (StringUtils.isEmpty(consumerConfig.getNamesrvAddr()) && StringUtils.isBlank(consumerConfig.getDataId())) {
            throw new RuntimeException("必须设置namesrv的地址或者dataId");
        }

        if (consumerConfig.getMessageListener() == null) {
            throw new RuntimeException("必须设计MessageListener");
        }

        consumerConfig.getMessageListener().check();
        checkForSubscribe(consumerConfig.getMessageListener().getTopicTagHandlerRoute());

        logger.info("ConsumerConfig: {}", consumerConfig.toString());

        pushConsumer = new DefaultMQPushConsumer(consumerConfig.getConsumerGroup());
        pushConsumer.setMessageListener(consumerConfig.getMessageListener());

        if (StringUtils.isNotBlank(consumerConfig.getNamesrvAddr())) {
            pushConsumer.setNamesrvAddr(consumerConfig.getNamesrvAddr());
        } else if (StringUtils.isNotBlank(consumerConfig.getDataId())) {
            pushConsumer.setNamesrvAddr(DiamondConfigManager.getNamesrvAddrWithCheck(consumerConfig.getDataId()));
        }

        if (consumerConfig.getPersistConsumerOffsetInterval() != null) {
            pushConsumer.setPersistConsumerOffsetInterval(consumerConfig.getPersistConsumerOffsetInterval());
        }
        if (consumerConfig.getMessageModel() != null) {
            pushConsumer.setMessageModel(consumerConfig.getMessageModel());
        }
        if (consumerConfig.getConsumeTimestamp() != null) {
            pushConsumer.setConsumeTimestamp(consumerConfig.getConsumeTimestamp());
        }
        if (consumerConfig.getAllocateMessageQueueStrategy() != null) {
            pushConsumer.setAllocateMessageQueueStrategy(consumerConfig.getAllocateMessageQueueStrategy());
        }
        if (consumerConfig.getConsumeThreadMin() != null) {
            pushConsumer.setConsumeThreadMin(consumerConfig.getConsumeThreadMin());
        }
        if (consumerConfig.getConsumeThreadMax() != null) {
            pushConsumer.setConsumeThreadMax(consumerConfig.getConsumeThreadMax());
        }
        if (consumerConfig.getAdjustThreadPoolNumsThreshold() != null) {
            pushConsumer.setAdjustThreadPoolNumsThreshold(consumerConfig.getAdjustThreadPoolNumsThreshold());
        }
        if (consumerConfig.getConsumeConcurrentlyMaxSpan() != null) {
            pushConsumer.setConsumeConcurrentlyMaxSpan(consumerConfig.getConsumeConcurrentlyMaxSpan());
        }
        if (consumerConfig.getPullThresholdForQueue() != null) {
            pushConsumer.setPullThresholdForQueue(consumerConfig.getPullThresholdForQueue());
        }
        if (consumerConfig.getPullInterval() != null) {
            pushConsumer.setPullInterval(consumerConfig.getPullInterval());
        }
        if (consumerConfig.getConsumeMessageBatchMaxSize() != null) {
            pushConsumer.setConsumeMessageBatchMaxSize(consumerConfig.getConsumeMessageBatchMaxSize());
        }
        if (consumerConfig.getPullBatchSize() != null) {
            pushConsumer.setPullBatchSize(consumerConfig.getPullBatchSize());
        }
        if (consumerConfig.getPostSubscriptionWhenPull() != null) {
            pushConsumer.setPostSubscriptionWhenPull(consumerConfig.getPostSubscriptionWhenPull());
        }
    }

    private void checkForSubscribe(Map<String, Map<String, MessageHandler>> topicTagHandlerRoute) {
        for (Map.Entry<String, Map<String, MessageHandler>> entry : topicTagHandlerRoute.entrySet()) {
            String topic = entry.getKey();
            Map<String, MessageHandler> tagToHandler = entry.getValue();

            if (MapUtils.isEmpty(tagToHandler)) {
                throw new IllegalArgumentException("必须设置handler topic=" + topic);
            }
        }
    }

    public void destroy() {
        if (pushConsumer != null) {
            pushConsumer.shutdown();
        }
    }

    public ConsumerConfig getConsumerConfig() {
        return consumerConfig;
    }

    public void setConsumerConfig(ConsumerConfig consumerConfig) {
        this.consumerConfig = consumerConfig;
    }
}
