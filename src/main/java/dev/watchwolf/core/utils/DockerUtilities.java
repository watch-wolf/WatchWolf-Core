package dev.watchwolf.core.utils;

public class DockerUtilities {
    public static int getJavaVersion(String mcVersionStr) {
        int result = new Version(mcVersionStr).roundTo(2).compareTo("1.17");
        if (result < 0) {
            // prior to 1.17
            return 8;
        }
        else if (result == 0) {
            // 1.17
            return 16;
        }
        else {
            // more or equal to 1.18
            result = new Version(mcVersionStr).roundTo(3).compareTo("1.20.5");
            if (result < 0) {
                // between 1.18 and 1.20.4
                return 17;
            }
            else {
                // 1.20.5 and later
                return 21;
            }
        }
    }
}
