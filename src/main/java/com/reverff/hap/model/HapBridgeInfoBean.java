package com.reverff.hap.model;

/**
 * Created by mgavlovy on 24.10.2016.
 *
 * @author Maksym Gavlovych (reverff@gmail.com)
 */
public class HapBridgeInfoBean {

    private String label;
    private String manufacturer;
    private String model;
    private String serial;
    private String host;
    private Integer port;
    private String authFile;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getAuthFile() {
        return authFile;
    }

    public void setAuthFile(String authFile) {
        this.authFile = authFile;
    }

    @Override
    public String toString() {
        return "HapBridgeBean{" +
                "label='" + label + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", serial='" + serial + '\'' +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                '}';
    }
}
