package com.tg.mq.spring;

import com.gale.rocketmq.manager.ProducerManager;
import com.gale.rocketmq.manager.PushConsumerManager;
import com.tgou.monitor.zpkin.rocketmq.Zipk2RtMqConsumerInterceptor;
import org.junit.After;
import org.junit.Before;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by diwayou on 2015/10/29.
 */
public abstract class SpringTestEnv {

    protected ClassPathXmlApplicationContext classPathXmlApplicationContext;

    protected ProducerManager producerManager;

    protected PushConsumerManager pushConsumerManager;

    protected Zipk2RtMqConsumerInterceptor zipk2RtMqConsumerInterceptor;

    @Before
    public void before() {
        classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:/ctx-root.xml");

        producerManager = classPathXmlApplicationContext.getBean("producerManager", ProducerManager.class);

        pushConsumerManager = classPathXmlApplicationContext.getBean("pushConsumerManager", PushConsumerManager.class);

        zipk2RtMqConsumerInterceptor = classPathXmlApplicationContext.getBean("zipk2RtMqConsumerInterceptor", Zipk2RtMqConsumerInterceptor.class);
    }

    @After
    public void after() {
        classPathXmlApplicationContext.close();
    }
}
