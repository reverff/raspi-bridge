package com.reverff.hap.app;

import java.io.File;
import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.beowulfe.hap.HomekitAuthInfo;
import com.beowulfe.hap.HomekitServer;
import com.reverff.hap.model.HapBridgeInfoBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class RaspiBridgeAuthInfo implements HomekitAuthInfo {
	private static final Logger log = Logger.getLogger(RaspiBridgeAuthInfo.class);

    private String authPath;

    private String pin;
	private String mac;
	private BigInteger salt;
	private String privateKey;
	private ConcurrentMap<String, byte[]> userKeyMap = new ConcurrentHashMap<>();
	
	public RaspiBridgeAuthInfo(HapBridgeInfoBean bridgeInfo) throws Exception {
        this.authPath = bridgeInfo.getAuthFile();
        Document doc = getXmlDocByPath(authPath);

        pin = getElementFromXml(doc, "pin").getTextContent();

        mac = defineMac(doc);
		salt = defineSalt(doc);
        privateKey = defineKey(doc);
	}

    private Document getXmlDocByPath(String path) throws Exception {
        File fXmlFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        return doc;
    }

    private Node getElementFromXml(Document doc, String nodeName) {
        NodeList nList = doc.getElementsByTagName(nodeName);
        return nList.item(0);
    }

    private void updateXml(Document doc) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(authPath));
        transformer.transform(source, result);
    }

    private String defineMac(Document doc) throws Exception {
        Node nodeMac = getElementFromXml(doc, "mac");
        String param;
        if (StringUtils.isNotBlank(nodeMac.getTextContent())) {
            param = nodeMac.getTextContent();
        } else {
            param = HomekitServer.generateMac();
            nodeMac.setTextContent(param);
            updateXml(doc);
            log.info("Updated with mac value = " + param);
        }
        return param;
    }

    private BigInteger defineSalt(Document doc) throws Exception {
        Node nodeSalt = getElementFromXml(doc, "salt");
        BigInteger param;
        if (StringUtils.isNotBlank(nodeSalt.getTextContent())) {
            param = new BigInteger(nodeSalt.getTextContent());
        } else {
            param = HomekitServer.generateSalt();
            nodeSalt.setTextContent(param.toString());
            updateXml(doc);
            log.info("Updated with salt value = " + param);
        }
        return param;
    }

    private String defineKey(Document doc) throws Exception {
        Node nodeKey = getElementFromXml(doc, "mac");
        String param;
        if (StringUtils.isNotBlank(nodeKey.getTextContent())) {
            param = nodeKey.getTextContent();
        } else {
            param = new String(HomekitServer.generateKey());
            nodeKey.setTextContent(param);
            updateXml(doc);
            log.info("Updated with key value = " + param);
        }
        return param;
    }


	@Override
	public String getPin() {
		return pin;
	}

	@Override
	public String getMac() {
		return mac;
	}

	@Override
	public BigInteger getSalt() {
		return salt;
	}

	@Override
	public byte[] getPrivateKey() {
		return privateKey.getBytes();
	}

    @Override
	public void createUser(String username, byte[] publicKey) {
		userKeyMap.putIfAbsent(username, publicKey);
		log.info("Added pairing for " + username);
	}

	@Override
	public void removeUser(String username) {
		userKeyMap.remove(username);
		log.info("Removed pairing for " + username);
	}

	@Override
	public byte[] getUserPublicKey(String username) {
		return userKeyMap.get(username);
	}

}
