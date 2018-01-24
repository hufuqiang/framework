package com.cepheis.rocketmq.manager;

import org.junit.Test;

public class DiamondConfigManagerTest {

    @Test
    public void getNamesrvAddrTest() {
        String namesrvAddr = DiamondConfigManager.getNamesrvAddr("ROCKETMQ_COMMON");
        System.out.println(namesrvAddr);

        namesrvAddr = DiamondConfigManager.getNamesrvAddr("ROCKETMQ_COMMON");
        System.out.println(namesrvAddr);
    }
}
