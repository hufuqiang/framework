package com.gale.rocketmq.manager;

import com.gale.rocketmq.config.ProducerConfig;
import com.gale.rocketmq.message.AbstractMessage;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by diwayou on 2015/10/29.
 */
public class ProducerManager {


    private static final Logger logger = LoggerFactory.getLogger(ProducerManager.class);

    private DefaultMQProducer producer;

    private ProducerConfig producerConfig;

    public void init() {
        initConfig();

        try {
            producer.start();
        } catch (MQClientException e) {
            throw new RuntimeException("ProducerManager 启动失败", e);
        }

        logger.info("ProducerManager start success.");
    }

    private void initConfig() {
        if (producerConfig == null) {
            throw new RuntimeException("必须设置ClientConfig");
        }
        if (StringUtils.isEmpty(producerConfig.getProducerGroup())) {
            throw new RuntimeException("必须设置producerGroup");
        }

        if (StringUtils.isBlank(producerConfig.getNamesrvAddr()) && StringUtils.isBlank(producerConfig.getDataId())) {
            throw new RuntimeException("必须设置namesrv的地址或者dataId");
        }

        boolean useTransaction = BooleanUtils.isTrue(producerConfig.getUseTransaction());
        if (useTransaction && producerConfig.getTransactionCheckListener() == null) {
            throw new RuntimeException("需要发送事务消息必须设置TransactionCheckListener");
        }

        if (useTransaction) {
            TransactionMQProducer transactionMQProducer = new TransactionMQProducer(producerConfig.getProducerGroup());

            transactionMQProducer.setTransactionCheckListener(producerConfig.getTransactionCheckListener());
            if (producerConfig.getCheckThreadPoolMinSize() != null) {
                transactionMQProducer.setCheckThreadPoolMinSize(producerConfig.getCheckThreadPoolMinSize());
            }
            if (producerConfig.getCheckThreadPoolMaxSize() != null) {
                transactionMQProducer.setCheckThreadPoolMaxSize(producerConfig.getCheckThreadPoolMaxSize());
            }
            if (producerConfig.getCheckRequestHoldMax() != null) {
                transactionMQProducer.setCheckRequestHoldMax(producerConfig.getCheckRequestHoldMax());
            }

            producer = transactionMQProducer;
        } else {
            producer = new DefaultMQProducer(producerConfig.getProducerGroup());
        }

        if (StringUtils.isNotBlank(producerConfig.getNamesrvAddr())) {
            producer.setNamesrvAddr(producerConfig.getNamesrvAddr());
        } else if (StringUtils.isNotBlank(producerConfig.getDataId())) {
            producer.setNamesrvAddr(DiamondConfigManager.getNamesrvAddrWithCheck(producerConfig.getDataId()));
        }

        if (producerConfig.getSendMsgTimeout() != null) {
            producer.setSendMsgTimeout(producerConfig.getSendMsgTimeout());
        }
        if (producerConfig.getCompressMsgBodyOverHowmuch() != null) {
            producer.setCompressMsgBodyOverHowmuch(producerConfig.getCompressMsgBodyOverHowmuch());
        }
        if (producerConfig.getRetryTimesWhenSendFailed() != null) {
            producer.setRetryTimesWhenSendFailed(producerConfig.getRetryTimesWhenSendFailed());
        }
        if (producerConfig.getRetryAnotherBrokerWhenNotStoreOK() != null) {
            producer.setRetryAnotherBrokerWhenNotStoreOK(producerConfig.getRetryAnotherBrokerWhenNotStoreOK());
        }
        if (producerConfig.getMaxMessageSize() != null) {
            producer.setMaxMessageSize(producerConfig.getMaxMessageSize());
        }
    }

    public void destroy() {
        try {
            if (producer != null) {
                producer.shutdown();
            }
        } catch (Exception e) {
            //
        }
    }

    public SendResult send(Message msg, long timeout) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        SendResult sendResult;
        try {
            sendResult = producer.send(msg, timeout);
        } catch (Exception e) {
            throw e;
        }
        return sendResult;
    }

    public SendResult send(AbstractMessage msg) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        SendResult sendResult;
        try {
            sendResult = producer.send(msg.getMessage());
        } catch (Exception e) {
            throw e;
        }
        return sendResult;
    }

    public SendResult send(final AbstractMessage msg, final long timeout) throws MQClientException,
            RemotingException, MQBrokerException, InterruptedException {
        SendResult sendResult;
        try {
            sendResult = producer.send(msg.getMessage(), timeout);
        } catch (Exception e) {
            throw e;
        }
        return sendResult;
    }


    public void sendAsync(final AbstractMessage msg, final SendCallback sendCallback) throws MQClientException,
            RemotingException, InterruptedException {
        try {
            producer.send(msg.getMessage(), sendCallback);
        } catch (Exception e) {
            throw e;
        }
    }


    public void sendAsync(final AbstractMessage msg, final SendCallback sendCallback, final long timeout)
            throws MQClientException, RemotingException, InterruptedException {
        try {
            producer.send(msg.getMessage(), sendCallback, timeout);
        } catch (Exception e) {
            throw e;
        }
    }


    public void sendOneway(final AbstractMessage msg) throws MQClientException, RemotingException,
            InterruptedException {
        try {
            producer.sendOneway(msg.getMessage());
        } catch (Exception e) {
            throw e;
        }
    }


    public SendResult send(final AbstractMessage msg, final MessageQueue mq) throws MQClientException,
            RemotingException, MQBrokerException, InterruptedException {
        SendResult sendResult;
        try {
            sendResult = producer.send(msg.getMessage(), mq);
        } catch (Exception e) {
            throw e;
        }
        return sendResult;

    }


    public SendResult send(final AbstractMessage msg, final MessageQueue mq, final long timeout)
            throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        SendResult sendResult;
        try {
            sendResult = producer.send(msg.getMessage(), mq, timeout);
        } catch (Exception e) {
            throw e;
        }
        return sendResult;
    }


    public void sendAsync(final AbstractMessage msg, final MessageQueue mq, final SendCallback sendCallback)
            throws MQClientException, RemotingException, InterruptedException {
        try {
            producer.send(msg.getMessage(), mq, sendCallback);
        } catch (Exception e) {
            throw e;
        }
    }


    public void sendAsync(final AbstractMessage msg, final MessageQueue mq, final SendCallback sendCallback, long timeout)
            throws MQClientException, RemotingException, InterruptedException {
        try {
            producer.send(msg.getMessage(), mq, sendCallback, timeout);
        } catch (Exception e) {
            throw e;
        }
    }


    public void sendOneway(final AbstractMessage msg, final MessageQueue mq) throws MQClientException,
            RemotingException, InterruptedException {
        try {
            producer.sendOneway(msg.getMessage(), mq);
        } catch (Exception e) {
            throw e;
        }
    }


    public SendResult send(final AbstractMessage msg, final MessageQueueSelector selector, final Object arg)
            throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        SendResult sendResult;
        try {
            sendResult = producer.send(msg.getMessage(), selector, arg);
        } catch (Exception e) {
            throw e;
        }
        return sendResult;
    }


    public SendResult send(final AbstractMessage msg, final MessageQueueSelector selector, final Object arg,
                           final long timeout) throws MQClientException, RemotingException, MQBrokerException,
            InterruptedException {
        SendResult sendResult;
        try {
            sendResult = producer.send(msg.getMessage(), selector, arg, timeout);
        } catch (Exception e) {
            throw e;
        }
        return sendResult;
    }


    public void sendAsync(final AbstractMessage msg, final MessageQueueSelector selector, final Object arg,
                          final SendCallback sendCallback) throws MQClientException, RemotingException,
            InterruptedException {
        try {
            producer.send(msg.getMessage(), selector, arg, sendCallback);
        } catch (Exception e) {
            throw e;
        }
    }


    public void sendAsync(final AbstractMessage msg, final MessageQueueSelector selector, final Object arg,
                          final SendCallback sendCallback, final long timeout) throws MQClientException, RemotingException,
            InterruptedException {
        try {
            producer.send(msg.getMessage(), selector, arg, sendCallback, timeout);
        } catch (Exception e) {
            throw e;
        }
    }


    public void sendOneway(final AbstractMessage msg, final MessageQueueSelector selector, final Object arg)
            throws MQClientException, RemotingException, InterruptedException {
        try {
            producer.sendOneway(msg.getMessage(), selector, arg);
        } catch (Exception e) {
            throw e;
        }
    }


    public TransactionSendResult sendMessageInTransaction(final AbstractMessage msg,
                                                          final LocalTransactionExecuter tranExecuter,
                                                          final Object arg) throws MQClientException {
        TransactionSendResult sendResult;
        try {
            sendResult = producer.sendMessageInTransaction(msg.getMessage(), tranExecuter, arg);
        } catch (Exception e) {
            throw e;
        }
        return sendResult;
    }

    public ProducerConfig getProducerConfig() {
        return producerConfig;
    }

    public void setProducerConfig(ProducerConfig producerConfig) {
        this.producerConfig = producerConfig;
    }

    public DefaultMQProducer getProducer() {
        return producer;
    }
}
