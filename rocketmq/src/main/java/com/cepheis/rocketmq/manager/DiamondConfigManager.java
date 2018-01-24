package com.cepheis.rocketmq.manager;

import com.taobao.diamond.client.impl.DiamondEnvRepo;
import com.taobao.diamond.common.Constants;
import com.taobao.diamond.manager.ManagerListener;
import com.taobao.diamond.manager.ManagerListenerAdapter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

public class DiamondConfigManager {

    private static final Logger log = LoggerFactory.getLogger(DiamondConfigManager.class);

    private static volatile String namesrvAddr = null;

    public static String getNamesrvAddrWithCheck(String dataId) {
        String namesrvAddrCur = getNamesrvAddr(dataId);
        if (StringUtils.isBlank(namesrvAddrCur)) {
            throw new RuntimeException("从diamond拉取namesrvAddr为空，请检查是否配置");
        }
        return namesrvAddrCur;
    }

    public static String getNamesrvAddr(String dataId) {
        if (StringUtils.isBlank(dataId)) {
            log.warn("dataId不能为空");
            return null;
        }
        if (namesrvAddr == null) {
            synchronized (DiamondConfigManager.class) {
                if (namesrvAddr == null) {
                    doGetConfig(dataId);
                }
            }
        }

        return namesrvAddr;
    }

    private static void doGetConfig(final String dataId) {
        String content;
        try {
            content = DiamondEnvRepo.defaultEnv.getConfig(dataId, Constants.DEFAULT_GROUP,
                    Constants.GETCONFIG_LOCAL_SERVER_SNAPSHOT, 1000);
        } catch (IOException e) {
            log.warn("get config fail {} {}", Constants.DEFAULT_GROUP, dataId, e);
            return;
        }

        try {
            if (StringUtils.isBlank(content)) {
                log.warn("{} {} content is blank.", Constants.DEFAULT_GROUP, dataId);
                return;
            }

            parseContent(content);
        } catch (Exception e) {
            log.warn("load {} {} fail. content={}", Constants.DEFAULT_GROUP, dataId, content, e);
            return;
        }

        ManagerListener managerListener = new ManagerListenerAdapter() {
            @Override
            public void receiveConfigInfo(String content) {
                try {
                    parseContent(content);
                } catch (Exception e) {
                    log.warn("refresh {} {} fail. content={}", Constants.DEFAULT_GROUP, dataId, content, e);
                }
            }
        };

        DiamondEnvRepo.defaultEnv.addListeners(dataId, Constants.DEFAULT_GROUP, managerListener);
    }

    private static void parseContent(String content) throws IOException {
        Properties properties = parseAsProperties(content);

        String namesrvAddrFromDiamond = properties.getProperty("namesrvAddr");
        if (StringUtils.isNotBlank(namesrvAddrFromDiamond)) {
            namesrvAddr = namesrvAddrFromDiamond;
        }
    }

    private static Properties parseAsProperties(String content) throws IOException {
        Properties properties = new Properties();
        properties.load(new StringReader(content));

        return properties;
    }
}
