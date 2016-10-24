package com.reverff.hap.app;

import com.beowulfe.hap.HomekitRoot;
import com.reverff.hap.controller.HomekitServerComponent;
import com.reverff.hap.controller.WindowCoveringComponent;
import com.reverff.hap.model.HapBridgeInfoBean;
import com.reverff.hap.util.SpringBeanUtils;
import org.apache.log4j.Logger;

import java.net.UnknownHostException;

public class Main {
    private static final Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) throws UnknownHostException {
        log.info("Starting application");
        try {
            SpringBeanUtils sbUtils = SpringBeanUtils.getInstance();
            HapBridgeInfoBean bridgeInfo = sbUtils.getHapBridgeBean();
            log.info("Current environment - " + bridgeInfo.toString());

            HomekitServerComponent homeKit = new HomekitServerComponent(bridgeInfo);
            HomekitRoot bridge = homeKit.createBridge(new RaspiBridgeAuthInfo(bridgeInfo));
            bridge.addAccessory(new WindowCoveringComponent(sbUtils.getWindowCoveringInfoBean()));

            bridge.start();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

}