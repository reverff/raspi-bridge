package com.beowulfe.hap.sample;

import com.beowulfe.hap.HomekitRoot;
import com.beowulfe.hap.HomekitServer;
import org.apache.log4j.Logger;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {
	private static final Logger log = Logger.getLogger(Main.class);
    private static final int PORT = 8080;
	
	public static void main(String[] args) throws UnknownHostException {
        log.info("Starting application");
        InetAddress address = Inet4Address.getByName("192.168.1.50");
        log.info("Current environment: " + address.getHostName() + " - " + address.getHostAddress() + " | " + address.getClass().getSimpleName());
		try {
                HomekitServer homeKit = new HomekitServer(address, PORT);
                HomekitRoot bridge = homeKit.createBridge(new RaspiBridgeAuthInfo(), "Test Bridge pc", "TestBridge, Inc.", "G6", "111abe234");
                bridge.addAccessory(new WindowCoveringComponent());
                bridge.start();
		} catch (Exception e) {
            log.error(e.getMessage(), e);
		}
		
	}

}