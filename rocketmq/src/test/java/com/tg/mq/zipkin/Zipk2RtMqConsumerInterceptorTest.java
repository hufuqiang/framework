package com.tg.mq.zipkin;

import com.tgou.monitor.zpkin.common.tracing.TracingFactory;
import com.tgou.monitor.zpkin.rocketmq.Zipk2RtMqConsumerInterceptor;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangyang on 2017/11/16.
 */
public class Zipk2RtMqConsumerInterceptorTest {
    @Test
    public void test() throws Exception {
        TracingFactory factory = new TracingFactory("test", "1");
        Zipk2RtMqConsumerInterceptor interceptor = Zipk2RtMqConsumerInterceptor.create(factory);

        List<Thread> list = new ArrayList<>();
        for (int j = 0; j < 100; ++j) {
            Thread thread = new Thread() {
                public void run() {
                    MessageExt message = mockmqMessage();
                    interceptor.preProcess(message, null);
                    interceptor.completeProcess(message, null, null);
                }
            };
            list.add(thread);
        }
        for (Thread thread : list) {
            thread.start();
            thread.join();
        }
        Thread.sleep(10 * 1000 * 1000);
    }

    private MessageExt mockmqMessage() {
        MessageExt message = new MessageExt();
        message.putUserProperty("X-B3-TraceId", "111111");
        message.putUserProperty("X-B3-SpanId", "222222");
        message.putUserProperty("X-B3-Sampled", "1");
        message.putUserProperty("X-B3-ParentSpanId", "33333");
        return message;
    }
}
