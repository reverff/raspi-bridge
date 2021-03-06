package com.reverff.hap.controller;

import com.beowulfe.hap.HomekitCharacteristicChangeCallback;
import com.beowulfe.hap.accessories.WindowCovering;
import com.beowulfe.hap.accessories.properties.WindowCoveringPositionState;
import com.reverff.hap.model.WindowCoveringInfoBean;
import org.apache.log4j.Logger;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class WindowCoveringComponent implements WindowCovering {
    private static final Logger log = Logger.getLogger(WindowCoveringComponent.class);

    private int id;
    private String label;
    private String manufacturer;
    private String model;
    private String serial;

    private Integer currentPosition = 0;
    private Integer targetPosition = 100;

    private HomekitCharacteristicChangeCallback curPosCallback;
    private HomekitCharacteristicChangeCallback tarPosCallback;
    private HomekitCharacteristicChangeCallback posStateCallback;
    protected HomekitCharacteristicChangeCallback obsDecCallback;

    public WindowCoveringComponent(WindowCoveringInfoBean info) {
        super();
        this.id = info.getId();
        this.label = info.getLabel();
        this.manufacturer = info.getManufacturer();
        this.model = info.getModel();
        this.serial = info.getSerial();
    }

    private void setChanged(HomekitCharacteristicChangeCallback callback) {
        if (callback != null) {
            callback.changed();
        }
    }

    private WindowCoveringPositionState state = WindowCoveringPositionState.STOPPED;

    @Override
    public CompletableFuture<Integer> getCurrentPosition() {
        return CompletableFuture.completedFuture(currentPosition);
    }

    @Override
    public CompletableFuture<Integer> getTargetPosition() {
        return CompletableFuture.completedFuture(targetPosition);
    }

    @Override
    public CompletableFuture<WindowCoveringPositionState> getPositionState() {
        return CompletableFuture.completedFuture(state);
    }

    @Override
    public CompletableFuture<Boolean> getObstructionDetected() {
        return CompletableFuture.completedFuture(false);
    }

    @Override
    public CompletableFuture<Void> setTargetPosition(int i) throws Exception {
        this.targetPosition = i;
        if (!Objects.equals(targetPosition, currentPosition)) {
            setChanged(tarPosCallback);
            state = targetPosition > currentPosition ? WindowCoveringPositionState.INCREASING : WindowCoveringPositionState.DECREASING;
            setChanged(posStateCallback);
        }
        return CompletableFuture.runAsync(go);
    }

    @Override
    public CompletableFuture<Void> setHoldPosition(boolean b) throws Exception {
        Runnable setHold = () -> System.out.println("setHoldPosition " + b);
        return CompletableFuture.runAsync(setHold);
    }

    @Override
    public void subscribeCurrentPosition(HomekitCharacteristicChangeCallback homekitCharacteristicChangeCallback) {
        this.curPosCallback = homekitCharacteristicChangeCallback;
    }

    @Override
    public void subscribeTargetPosition(HomekitCharacteristicChangeCallback homekitCharacteristicChangeCallback) {
        this.tarPosCallback = homekitCharacteristicChangeCallback;
    }

    @Override
    public void subscribePositionState(HomekitCharacteristicChangeCallback homekitCharacteristicChangeCallback) {
        this.posStateCallback = homekitCharacteristicChangeCallback;
    }

    @Override
    public void subscribeObstructionDetected(HomekitCharacteristicChangeCallback homekitCharacteristicChangeCallback) {
        this.obsDecCallback = homekitCharacteristicChangeCallback;
    }

    @Override
    public void unsubscribeCurrentPosition() {
        this.curPosCallback = null;
    }

    @Override
    public void unsubscribeTargetPosition() {
        this.tarPosCallback = null;
    }

    @Override
    public void unsubscribePositionState() {
        this.posStateCallback = null;
    }

    @Override
    public void unsubscribeObstructionDetected() {
        this.obsDecCallback = null;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void identify() {

    }

    @Override
    public String getSerialNumber() {
        return serial;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public String getManufacturer() {
        return manufacturer;
    }

    public void setState(WindowCoveringPositionState state) {
        this.state = state;
    }

    Runnable go = () -> {
        if (!Objects.equals(targetPosition, currentPosition)) {
            log.info(String.format("Running from %s to %s", currentPosition, targetPosition));
            StepperMotorService.getInstance().rotate(targetPosition - currentPosition);
            this.currentPosition = this.targetPosition;
            setChanged(curPosCallback);
            setState(WindowCoveringPositionState.STOPPED);
            setChanged(posStateCallback);
        } else {
            log.info("targetPosition == currentPosition");
        }
    };
}
