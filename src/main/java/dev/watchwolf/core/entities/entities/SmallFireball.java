package dev.watchwolf.core.entities.entities;

import dev.watchwolf.core.entities.Position;
import dev.watchwolf.core.entities.SocketHelper;

import java.util.ArrayList;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class SmallFireball extends Entity {
    public SmallFireball(String UUID, Position position) {
        super(UUID, position);
    }

    public SmallFireball(Position position) {
        super(position);
    }

    @Override
    public void sendSocketData(ArrayList<Byte> out) {
        SocketHelper.addShort(out, EntityType.SMALL_FIREBALL.ordinal());
        this.position.sendSocketData(out);
        SocketHelper.addString(out, this.UUID);
    }
}
