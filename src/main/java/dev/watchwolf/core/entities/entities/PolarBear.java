package dev.watchwolf.core.entities.entities;

import dev.watchwolf.core.entities.Position;
import dev.watchwolf.core.entities.SocketHelper;

import java.util.ArrayList;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class PolarBear extends Entity {
    public PolarBear(String UUID, Position position) {
        super(UUID, position);
    }

    public PolarBear(Position position) {
        super(position);
    }

    @Override
    public void sendSocketData(ArrayList<Byte> out) {
        SocketHelper.addShort(out, EntityType.POLAR_BEAR.ordinal());
        this.position.sendSocketData(out);
        SocketHelper.addString(out, this.UUID);
    }
}
