package dev.watchwolf.core.entities.entities;

import dev.watchwolf.core.entities.Position;
import dev.watchwolf.core.entities.SocketHelper;

import java.util.ArrayList;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class EnderCrystal extends Entity {
    public EnderCrystal(String UUID, Position position) {
        super(UUID, position);
    }

    public EnderCrystal(Position position) {
        super(position);
    }

    @Override
    public void sendSocketData(ArrayList<Byte> out) {
        SocketHelper.addShort(out, EntityType.ENDER_CRYSTAL.ordinal());
        this.position.sendSocketData(out);
        SocketHelper.addString(out, this.UUID);
    }
}
