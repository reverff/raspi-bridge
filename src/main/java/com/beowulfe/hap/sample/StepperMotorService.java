package com.beowulfe.hap.sample;

import com.pi4j.component.motor.impl.GpioStepperMotorComponent;
import com.pi4j.io.gpio.*;
import org.apache.log4j.Logger;

public class StepperMotorService {
    private static final Logger log = Logger.getLogger(StepperMotorService.class);
    private static StepperMotorService instance;
    private static int STEPS_FOR_COMPLETE = 2038;
    private GpioStepperMotorComponent motor;

    private StepperMotorService() {
        GpioController gpio = GpioFactory.getInstance();
        GpioPinDigitalOutput[] pins = {
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW)};

        gpio.setShutdownOptions(true, PinState.LOW, pins);

        motor = new GpioStepperMotorComponent(pins);
        byte[] single_step_sequence = new byte[4];
        single_step_sequence[0] = (byte) 0b0001;
        single_step_sequence[1] = (byte) 0b0010;
        single_step_sequence[2] = (byte) 0b0100;
        single_step_sequence[3] = (byte) 0b1000;

        motor.setStepInterval(2);
        motor.setStepSequence(single_step_sequence);
        motor.setStepsPerRevolution(STEPS_FOR_COMPLETE);
    }

    public static StepperMotorService getInstance() {
        instance = instance == null ? new StepperMotorService() : instance;
        return instance;
    }

    public void rotate(int delta) {
        int stepCount = STEPS_FOR_COMPLETE / 100 * delta;
        log.info("Step count for this time: " + stepCount);
        motor.step(stepCount);
    }

}