package com.cepheis.rocketmq.manager;

import com.cepheis.rocketmq.message.SimpleMessage;
import com.cepheis.rocketmq.message.TransactionMessage;
import com.cepheis.rocketmq.message.SimpleData;
import com.tg.mq.spring.SpringTestEnv;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionExecuter;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by diwayou on 2015/10/29.
 */
public class ProducerManagerTest extends SpringTestEnv {

    @Test
    public void sendMessageTest() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        SimpleData simpleData = new SimpleData("name", "password");
        for (int i = 0; i < 1; i++) {
            SimpleMessage message = new SimpleMessage(String.valueOf(i), simpleData);
            SendResult result = producerManager.send(message);
            System.out.println(result.getMsgId() + message.getMessage());
        }

        TimeUnit.HOURS.sleep(1);
    }

    @Test
    public void sendTransMessageTest() throws MQClientException, InterruptedException {
        SimpleData simpleData = new SimpleData("transName", "transPassword");

        for (int i = 0; i < 1; i++) {
            TransactionMessage message = new TransactionMessage(String.valueOf(i), simpleData);

            TransactionSendResult result = producerManager.sendMessageInTransaction(message, new LocalTransactionExecuter() {
                @Override
                public LocalTransactionState executeLocalTransactionBranch(Message msg, Object arg) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
            }, String.valueOf(i));

            System.out.println(result);
        }

        TimeUnit.HOURS.sleep(1);
    }
}
