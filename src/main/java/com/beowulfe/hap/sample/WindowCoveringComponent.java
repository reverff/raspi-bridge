package com.beowulfe.hap.sample;

import com.beowulfe.hap.HomekitCharacteristicChangeCallback;
import com.beowulfe.hap.accessories.WindowCovering;
import com.beowulfe.hap.accessories.properties.WindowCoveringPositionState;

import java.util.concurrent.CompletableFuture;

public class WindowCoveringComponent implements WindowCovering {

    private Integer currentPosition = 0;
    private Integer targetPosition = 100;

    private HomekitCharacteristicChangeCallback curPosCallback;
    private HomekitCharacteristicChangeCallback tarPosCallback;
    private HomekitCharacteristicChangeCallback posStateCallback;
    private HomekitCharacteristicChangeCallback obsDecCallback;

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
        setChanged(tarPosCallback);
        state = targetPosition > currentPosition ? WindowCoveringPositionState.INCREASING :WindowCoveringPositionState.DECREASING;
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
        return 10;
    }

    @Override
    public String getLabel() {
        return "WindowCovering";
    }

    @Override
    public void identify() {

    }

    @Override
    public String getSerialNumber() {
        return "1111";
    }

    @Override
    public String getModel() {
        return "null";
    }

    @Override
    public String getManufacturer() {
        return "null";
    }

    public void setState(WindowCoveringPositionState state) {
        this.state = state;
    }

    Runnable go = () -> {
        System.out.println(String.format("Running from %s to %s", currentPosition, targetPosition));
        this.currentPosition = this.targetPosition;
        setChanged(curPosCallback);
        setState(WindowCoveringPositionState.STOPPED);
        setChanged(posStateCallback);
    };
}
