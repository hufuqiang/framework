package com.gale.rocketmq.config;

import com.alibaba.fastjson.JSON;
import com.gale.rocketmq.listener.AbstractMessageListener;
import org.apache.rocketmq.client.consumer.AllocateMessageQueueStrategy;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * Created by diwayou on 2015/10/29.
 */
public class ConsumerConfig {

    /**
     * 消费者集群的group，要做到全局唯一，跟其它应用区分
     */
    private String consumerGroup;

    /**
     * Name Server的地址
     */
    private String namesrvAddr;

    /**
     * 拉取Name Server地址的diamond配置，其中group是DEFAULT_GROUP，如果配置了namesrvAddr，该配置不生效
     */
    private String dataId;

    /**
     * 客户端消费偏移量持久化间隔，单位毫秒。默认5000，表示5秒持久化一次
     * 为了减少客户端重启或者其它异常情况未持久化已消费偏移量所导致的消息重复消费问题，可以调小该值，
     * 但同时也增大了服务器的压力
     */
    private Integer persistConsumerOffsetInterval;

    /**
     * 消费模式，默认是集群消费，还有广播模式
     */
    private MessageModel messageModel;

    /**
     * 每次从哪开始消费，默认是ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET
     */
    private ConsumeFromWhere consumeFromWhere;

    /**
     * 按照时间回溯消费，默认消费30分钟之前的数据，
     * 精度到秒，格式是20131223171201，表示1023年12月23日17点12分01秒
     */
    private String consumeTimestamp;

    /**
     * 队列分配策略
     */
    private AllocateMessageQueueStrategy allocateMessageQueueStrategy;

    /**
     * 最小消费线程数量，默认20
     */
    private Integer consumeThreadMin;

    /**
     * 最大消费线程数量，默认64
     */
    private Integer consumeThreadMax;

    /**
     * 根据该值调整线程池的大小，默认100000
     */
    private Long adjustThreadPoolNumsThreshold;

    /**
     * 单队列并行消费允许的最大跨度，但是不影响顺序消费，默认2000
     */
    private Integer consumeConcurrentlyMaxSpan;

    /**
     * 拉消息本地队列缓存消息最大数，可以做流控，默认1000
     */
    private Integer pullThresholdForQueue;

    /**
     * 拉消息间隔，由于是长轮询，默认为0，
     * 但是如果应用为了流控，也可以设置大于0的值，单位毫秒
     */
    private Long pullInterval;

    /**
     * 批量消费，一次消费多少条消息，也就是消费Listener中的List<MessageExt>的大小，默认为1
     */
    private Integer consumeMessageBatchMaxSize;

    /**
     * 批量拉消息，一次最多拉多少条，默认32
     * 不要设置太大，因为服务器端也会进行限制
     */
    private Integer pullBatchSize;

    /**
     * 是否每次拉取消息的时候都去更新订阅关系，默认为false
     */
    private Boolean postSubscriptionWhenPull;

    /**
     * 消费消息的listener，使用push消息的时候使用
     */
    private AbstractMessageListener messageListener;

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
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

    public ConsumerConfig setDataId(String dataId) {
        this.dataId = dataId;
        return this;
    }

    public Integer getPersistConsumerOffsetInterval() {
        return persistConsumerOffsetInterval;
    }

    public void setPersistConsumerOffsetInterval(Integer persistConsumerOffsetInterval) {
        this.persistConsumerOffsetInterval = persistConsumerOffsetInterval;
    }

    public MessageModel getMessageModel() {
        return messageModel;
    }

    public void setMessageModel(MessageModel messageModel) {
        this.messageModel = messageModel;
    }

    public ConsumeFromWhere getConsumeFromWhere() {
        return consumeFromWhere;
    }

    public void setConsumeFromWhere(ConsumeFromWhere consumeFromWhere) {
        this.consumeFromWhere = consumeFromWhere;
    }

    public String getConsumeTimestamp() {
        return consumeTimestamp;
    }

    public void setConsumeTimestamp(String consumeTimestamp) {
        this.consumeTimestamp = consumeTimestamp;
    }

    public AllocateMessageQueueStrategy getAllocateMessageQueueStrategy() {
        return allocateMessageQueueStrategy;
    }

    public void setAllocateMessageQueueStrategy(AllocateMessageQueueStrategy allocateMessageQueueStrategy) {
        this.allocateMessageQueueStrategy = allocateMessageQueueStrategy;
    }

    public Integer getConsumeThreadMin() {
        return consumeThreadMin;
    }

    public void setConsumeThreadMin(Integer consumeThreadMin) {
        this.consumeThreadMin = consumeThreadMin;
    }

    public Integer getConsumeThreadMax() {
        return consumeThreadMax;
    }

    public void setConsumeThreadMax(Integer consumeThreadMax) {
        this.consumeThreadMax = consumeThreadMax;
    }

    public Long getAdjustThreadPoolNumsThreshold() {
        return adjustThreadPoolNumsThreshold;
    }

    public void setAdjustThreadPoolNumsThreshold(Long adjustThreadPoolNumsThreshold) {
        this.adjustThreadPoolNumsThreshold = adjustThreadPoolNumsThreshold;
    }

    public Integer getConsumeConcurrentlyMaxSpan() {
        return consumeConcurrentlyMaxSpan;
    }

    public void setConsumeConcurrentlyMaxSpan(Integer consumeConcurrentlyMaxSpan) {
        this.consumeConcurrentlyMaxSpan = consumeConcurrentlyMaxSpan;
    }

    public Integer getPullThresholdForQueue() {
        return pullThresholdForQueue;
    }

    public void setPullThresholdForQueue(Integer pullThresholdForQueue) {
        this.pullThresholdForQueue = pullThresholdForQueue;
    }

    public Long getPullInterval() {
        return pullInterval;
    }

    public void setPullInterval(Long pullInterval) {
        this.pullInterval = pullInterval;
    }

    public Integer getConsumeMessageBatchMaxSize() {
        return consumeMessageBatchMaxSize;
    }

    public void setConsumeMessageBatchMaxSize(Integer consumeMessageBatchMaxSize) {
        this.consumeMessageBatchMaxSize = consumeMessageBatchMaxSize;
    }

    public Integer getPullBatchSize() {
        return pullBatchSize;
    }

    public void setPullBatchSize(Integer pullBatchSize) {
        this.pullBatchSize = pullBatchSize;
    }

    public Boolean getPostSubscriptionWhenPull() {
        return postSubscriptionWhenPull;
    }

    public void setPostSubscriptionWhenPull(Boolean postSubscriptionWhenPull) {
        this.postSubscriptionWhenPull = postSubscriptionWhenPull;
    }

    public AbstractMessageListener getMessageListener() {
        return messageListener;
    }

    public void setMessageListener(AbstractMessageListener messageListener) {
        this.messageListener = messageListener;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
