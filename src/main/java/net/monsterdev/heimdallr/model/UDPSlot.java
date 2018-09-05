package net.monsterdev.heimdallr.model;

public class UDPSlot extends NetworkSlot {
    public UDPSlot(String addr, int port) {
        super(addr, port, Protocol.UDP);
    }
}
