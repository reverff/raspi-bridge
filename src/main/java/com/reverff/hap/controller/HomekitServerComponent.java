package com.reverff.hap.controller;

import com.beowulfe.hap.HomekitAuthInfo;
import com.beowulfe.hap.HomekitRoot;
import com.beowulfe.hap.HomekitServer;
import com.reverff.hap.model.HapBridgeInfoBean;

import java.net.Inet4Address;

/**
 * Created by mgavlovy on 24.10.2016.
 *
 * @author Maksym Gavlovych (reverff@gmail.com)
 */
public class HomekitServerComponent extends HomekitServer {

    private HapBridgeInfoBean bridgeInfo;

    public HomekitServerComponent(HapBridgeInfoBean bridgeInfo) throws Exception {
        super(Inet4Address.getByName(bridgeInfo.getHost()), bridgeInfo.getPort());
        this.bridgeInfo = bridgeInfo;
    }

    public HomekitRoot createBridge(HomekitAuthInfo homekitAuthInfo) throws Exception {
        return super.createBridge(homekitAuthInfo, bridgeInfo.getLabel(), bridgeInfo.getManufacturer(), bridgeInfo.getModel(), bridgeInfo.getSerial());
    }
}
