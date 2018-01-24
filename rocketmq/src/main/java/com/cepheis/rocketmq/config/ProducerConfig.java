package com.cepheis.rocketmq.config;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.producer.TransactionCheckListener;

/**
 * Created by diwayou on 2015/10/29.
 */
public class ProducerConfig {

    /**
     * 生产者集群的group，要做到全局唯一，跟其它应用区分
     */
    private String producerGroup;

    /**
     * Name Server的地址
     */
    private String namesrvAddr;

    /**
     * 拉取Name Server地址的diamond配置，其中group是DEFAULT_GROUP，如果配置了namesrvAddr，该配置不生效
     */
    private String dataId;

    /**
     * 发送消息超时时间，单位毫秒，默认3000，即3秒
     */
    private Integer sendMsgTimeout;

    /**
     * 超过该值，会对消息进行压缩，默认是4096，即4k
     */
    private Integer compressMsgBodyOverHowmuch;

    /**
     * 消息发送失败时，自动重试次数，默认2次
     */
    private Integer retryTimesWhenSendFailed;

    /**
     * 如果发送消息返回sendResult，但是sendStatus!=SEND_OK，是否重试发送，默认为false
     */
    private Boolean retryAnotherBrokerWhenNotStoreOK;

    /**
     * 客户端能够发送的最大消息，默认是1024 * 128， 即128k
     * 服务端也能够设置该值，客户端设置的值不要超过服务端设置的值
     */
    private Integer maxMessageSize;

    /**
     * 是否需要发送事务消息
     */
    private Boolean useTransaction = Boolean.FALSE;

    /**
     * 事务状态回查的监听器，当发送事务消息的时候，该配置必须设置
     */
    private TransactionCheckListener transactionCheckListener;

    /**
     * Broker回查Producer事务状态时，线程池大小，默认1
     */
    private Integer checkThreadPoolMinSize;

    /**
     * Broker回查Producer事务状态时，线程池大小，默认1
     */
    private Integer checkThreadPoolMaxSize;

    /**
     * Broker回查Producer事务状态时，Producer本地缓冲请求队列大小，默认2000
     */
    private Integer checkRequestHoldMax = 2000;

    public String getProducerGroup() {
        return producerGroup;
    }

    public void setProducerGroup(String producerGroup) {
        this.producerGroup = producerGroup;
    }

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public String getDataId() {
        return dataId;
    }

    public ProducerConfig setDataId(String dataId) {
        this.dataId = dataId;
        return this;
    }

    public Integer getSendMsgTimeout() {
        return sendMsgTimeout;
    }

    public void setSendMsgTimeout(Integer sendMsgTimeout) {
        this.sendMsgTimeout = sendMsgTimeout;
    }

    public Integer getCompressMsgBodyOverHowmuch() {
        return compressMsgBodyOverHowmuch;
    }

    public void setCompressMsgBodyOverHowmuch(Integer compressMsgBodyOverHowmuch) {
        this.compressMsgBodyOverHowmuch = compressMsgBodyOverHowmuch;
    }

    public Integer getRetryTimesWhenSendFailed() {
        return retryTimesWhenSendFailed;
    }

    public void setRetryTimesWhenSendFailed(Integer retryTimesWhenSendFailed) {
        this.retryTimesWhenSendFailed = retryTimesWhenSendFailed;
    }

    public Boolean getRetryAnotherBrokerWhenNotStoreOK() {
        return retryAnotherBrokerWhenNotStoreOK;
    }

    public void setRetryAnotherBrokerWhenNotStoreOK(Boolean retryAnotherBrokerWhenNotStoreOK) {
        this.retryAnotherBrokerWhenNotStoreOK = retryAnotherBrokerWhenNotStoreOK;
    }

    public Integer getMaxMessageSize() {
        return maxMessageSize;
    }

    public void setMaxMessageSize(Integer maxMessageSize) {
        this.maxMessageSize = maxMessageSize;
    }

    public Boolean getUseTransaction() {
        return useTransaction;
    }

    public void setUseTransaction(Boolean useTransaction) {
        this.useTransaction = useTransaction;
    }

    public TransactionCheckListener getTransactionCheckListener() {
        return transactionCheckListener;
    }

    public void setTransactionCheckListener(TransactionCheckListener transactionCheckListener) {
        this.transactionCheckListener = transactionCheckListener;
    }

    public Integer getCheckThreadPoolMinSize() {
        return checkThreadPoolMinSize;
    }

    public void setCheckThreadPoolMinSize(Integer checkThreadPoolMinSize) {
        this.checkThreadPoolMinSize = checkThreadPoolMinSize;
    }

    public Integer getCheckThreadPoolMaxSize() {
        return checkThreadPoolMaxSize;
    }

    public void setCheckThreadPoolMaxSize(Integer checkThreadPoolMaxSize) {
        this.checkThreadPoolMaxSize = checkThreadPoolMaxSize;
    }

    public Integer getCheckRequestHoldMax() {
        return checkRequestHoldMax;
    }

    public void setCheckRequestHoldMax(Integer checkRequestHoldMax) {
        this.checkRequestHoldMax = checkRequestHoldMax;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
