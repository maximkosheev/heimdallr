package net.monsterdev.heimdallr.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FilterRule {
    /**
     * Types of filter rules
     */
    public enum Type {
        ACCEPT,
        REJECT
    }

    /**
     * Terminal identifier
     */
    private int deviceId;
    private Type type;

    public boolean apply(DevicePackage devicePackage) {
        return devicePackage.getDeviceId() == deviceId;
    }

    @Override
    public String toString() {
        return "deviceId: " + deviceId + " "
                + "type: " + type;
    }
}
