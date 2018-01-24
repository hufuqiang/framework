package com.tg.mq.zipkin;

import com.cepheis.rocketmq.message.SimpleData;
import com.cepheis.rocketmq.message.ZipkinSimpleMessage;
import com.tg.mq.spring.SpringTestEnv;
import com.tgou.monitor.zpkin.common.tracing.TracingFactory;
import com.tgou.monitor.zpkin.rocketmq.Zipk2RtMqProducerInterceptor;
import com.tgou.monitor.zpkin.util.ZipkinTagsContainer;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by diwayou on 2015/10/29.
 */
public class ZipkinProducerManagerTest extends SpringTestEnv {

    @Test
    public void sendMessageTest() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        TracingFactory factory = new TracingFactory("test", "1");
        Zipk2RtMqProducerInterceptor interceptor = Zipk2RtMqProducerInterceptor.create(factory);
        producerManager.setInterceptor(interceptor);
        ZipkinTagsContainer.putTag("msgId", "12345677788");

        SimpleData simpleData = new SimpleData("name", "password");
        for (int i = 0; i < 1; i++) {
            ZipkinSimpleMessage message = new ZipkinSimpleMessage(String.valueOf(i), simpleData);
            SendResult result = null;
            try {
                result = producerManager.send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result != null) {
                System.out.println(result.getMsgId() + message.getMessage());
            }
        }
        ZipkinTagsContainer.clearTags();
        TimeUnit.HOURS.sleep(1);
    }
}
