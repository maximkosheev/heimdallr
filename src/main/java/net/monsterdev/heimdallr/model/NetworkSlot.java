package net.monsterdev.heimdallr.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class NetworkSlot {
    public enum Protocol {
        UDP,
        TCP
    }
    private String addr;
    private int port;
    private Protocol protocol;

    @Override
    public String toString() {
        return "addr: " + addr + " " +
                "port: " + port + " " +
                "protocol: " + protocol;
    }
}
