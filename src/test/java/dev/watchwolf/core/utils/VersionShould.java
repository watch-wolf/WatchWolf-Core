package dev.watchwolf.core.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VersionShould {
    @Test
    public void expectLessThanOnSmallerVersions() throws Exception {
        Version checking = new Version("1.13"),
                comparedTo = new Version("1.19");

        assertTrue(checking.compareTo(comparedTo) < 0);
    }

    @Test
    public void expectGreaterThanOnGreaterVersions() throws Exception {
        Version checking = new Version("1.20"),
                comparedTo = new Version("1.19");

        assertTrue(checking.compareTo(comparedTo) > 0);
    }

    @Test
    public void expectEqualsOnSameVersions() throws Exception {
        Version checking = new Version("1.19"),
                comparedTo = new Version("1.19");

        assertEquals(0, checking.compareTo(comparedTo));
    }

    @Test
    public void expectGreaterThanOnGreaterSubversion() throws Exception {
        Version checking = new Version("1.19.1"),
                comparedTo = new Version("1.19");

        assertTrue(checking.compareTo(comparedTo) > 0);
    }

    @Test
    public void expectEqualsOnSameSubversionWithLowerPrecision() throws Exception {
        Version checking = new Version("1.19.1").roundTo(2),
                comparedTo = new Version("1.19");

        assertEquals(0, checking.compareTo(comparedTo));
    }

    @Test
    public void returnInputVersionAsString() throws Exception {
        String checking = "1.19";

        String got = new Version(checking).toString();

        assertEquals(checking, got);
    }
}
