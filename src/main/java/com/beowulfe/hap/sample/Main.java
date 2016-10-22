package com.beowulfe.hap.sample;

import com.beowulfe.hap.HomekitRoot;
import com.beowulfe.hap.HomekitServer;
import org.apache.log4j.Logger;

public class Main {
	private static final Logger log = Logger.getLogger(Main.class);
	private static final int PORT = 9123;
	
	public static void main(String[] args) {
        log.info("Starting application");
		try {
                HomekitServer homeKit = new HomekitServer(PORT);
                HomekitRoot bridge = homeKit.createBridge(new RaspiBridgeAuthInfo(), "Test Bridge", "TestBridge, Inc.", "G6", "111abe234");
                bridge.addAccessory(new WindowCoveringComponent());
                bridge.start();
		} catch (Exception e) {
            log.error(e.getMessage(), e);
		}
		
	}

}