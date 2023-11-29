package dev.watchwolf.server;

import dev.watchwolf.core.entities.Position;

import java.io.IOException;

public interface WorldGuardServerPetition {
    void createRegion(String name, Position firstCoordinate, Position secondCoordinate) throws IOException;
    String []getRegions() throws IOException;
    String []getRegions(Position pos) throws IOException;
}
