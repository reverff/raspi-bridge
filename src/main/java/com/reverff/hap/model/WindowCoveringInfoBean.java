package com.reverff.hap.model;

/**
 * Created by mgavlovy on 24.10.2016.
 *<id>421994</id>
 <label>Window Covering</label>
 <manufacturer>Maksym Gavlovych</manufacturer>
 <model>RaspiBridge</model>
 <serial>001</serial>
 *
 *
 *
 * @author Maksym Gavlovych (reverff@gmail.com)
 */
public class WindowCoveringInfoBean {
    private Integer id;
    private String label;
    private String manufacturer;
    private String model;
    private String serial;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    @Override
    public String toString() {
        return "WindowCoveringInfoBean{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", serial='" + serial + '\'' +
                '}';
    }
}
