package net.monsterdev.heimdallr.model;

public class TCPSlot extends NetworkSlot {
    public TCPSlot(String addr, int port) {
        super(addr, port, Protocol.TCP);
    }
}
