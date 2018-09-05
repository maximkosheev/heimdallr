package net.monsterdev.heimdallr.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.monsterdev.heimdallr.model.NetworkSlot;

@Data
@AllArgsConstructor
public abstract class FilterService implements Runnable {
    private NetworkSlot slot;

}
