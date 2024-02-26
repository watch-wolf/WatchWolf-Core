package dev.watchwolf.core.utils;

public class DockerUtilities {
    public static int getJavaVersion(String mcVersionStr) {
        Version mcVersion = new Version(mcVersionStr);
        int result = mcVersion.compareTo("1.17");
        if (result < 0) {
            // prior to 1.17
            return 8;
        }
        else if (result == 0) {
            // 1.17
            return 16;
        }
        else {
            // after 1.17
            return 17;
        }
    }
}
