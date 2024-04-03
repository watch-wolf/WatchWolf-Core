package dev.watchwolf.core.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DockerUtilitiesShould {
    @ParameterizedTest
    @ValueSource(strings = {"1.8", "1.12.2", "1.16.5"})
    public void returnJava8ForOldMinecraftVersions(String version) throws Exception {
        int jre = DockerUtilities.getJavaVersion(version);
        assertEquals(8, jre, "Failed while comparing return of version " + version);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1.17", "1.17.1"})
    public void returnJava16ForMinecraft17(String version) throws Exception {
        int jre = DockerUtilities.getJavaVersion(version);
        assertEquals(16, jre, "Failed while comparing return of version " + version);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1.18", "1.20.4"})
    public void returnJava17ForNewMinecraftVersions(String version) throws Exception {
        int jre = DockerUtilities.getJavaVersion(version);
        assertEquals(17, jre, "Failed while comparing return of version " + version);
    }
}
