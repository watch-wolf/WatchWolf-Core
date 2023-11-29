package dev.watchwolf.core.entities.blocks;

/**
 * group_count
 */
public interface Groupable extends BlockModifier {
    Groupable setGroupAmount(int amount) throws IllegalArgumentException;
    int getGroupAmount();
    int getMaxGroupAmount();
}
