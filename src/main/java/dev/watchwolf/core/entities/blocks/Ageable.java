package dev.watchwolf.core.entities.blocks;

/**
 * Age
 */
public interface Ageable extends BlockModifier {
    Ageable setAge(int age) throws IllegalArgumentException;
    int getAge();
    int getMaxAge();
}
