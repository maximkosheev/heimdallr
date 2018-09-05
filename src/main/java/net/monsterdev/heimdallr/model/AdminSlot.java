package net.monsterdev.heimdallr.model;

public class AdminSlot extends TCPSlot {
    public AdminSlot(String addr, int port, Protocol protocol) {
        super(addr, port, protocol);
    }

    @Override
    public void process() {
        //
    }
}
