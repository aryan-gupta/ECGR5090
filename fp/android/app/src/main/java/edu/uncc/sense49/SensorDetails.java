package edu.uncc.sense49;

public class SensorDetails {
    public enum SensorState {
        OFF,
        ON,
        AUTO,
        CLOSED,
        LOCKED,
        UNLOCKED,
        OPEN,
        HEAT,
        COOL,
        ACTIVE,
        INACTIVE
    }

    public enum SensorType {
        MAIN_CONTROL,
        GARAGE_DOOR,
        THERMOSTAT_MODE,
        THERMOSTAT_FAN,
        THERMOSTAT_CURRENT_TEMP,
        THERMOSTAT_CONTROL_TEMP,
        LIGHTS,
        LOCKS,
        DOOR_WINDOW,
        MOTION_DETECTOR
    }

    public int mID;
    public String mName;
    public int mNumber;
    public SensorState mState;
    public SensorType mType;
}
