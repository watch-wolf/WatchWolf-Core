package dev.watchwolf.core.utils;

/**
 * @see <a href="https://stackoverflow.com/a/11024200/9178470">https://stackoverflow.com/a/11024200/9178470</a>
 */
public class Version implements Comparable<Version> {
    private String version;

    public final String get() {
        return this.version;
    }

    public Version(String version) {
        if(version == null) throw new IllegalArgumentException("Version can not be null");
        if (version.startsWith("v")) version = version.substring(1); // remove the 'v' from the version
        if(!version.matches("[0-9]+(\\.[0-9]+)*")) throw new IllegalArgumentException("'" + version + "' is an invalid version");
        this.version = version;
    }

    /**
     * Will discard the extra places (if present).
     * It will modify the object itself.
     * @param places How many places should it keep
     * @return This
     */
    public Version roundTo(int places) {
        if (places == 0) throw new IllegalArgumentException("At least 1 digit must be present.");
        String []segments = this.get().split("\\.");

        this.version = segments[0];
        for (int digit = 1; digit < places; digit++) this.version += "." + segments[digit];

        return this;
    }

    @Override
    public int compareTo(Version that) {
        if(that == null) return 1;

        String[] thisParts = this.get().split("\\.");
        String[] thatParts = that.get().split("\\.");
        int length = Math.max(thisParts.length, thatParts.length);
        for(int i = 0; i < length; i++) {
            int thisPart = i < thisParts.length ? Integer.parseInt(thisParts[i]) : 0;
            int thatPart = i < thatParts.length ? Integer.parseInt(thatParts[i]) : 0;
            if(thisPart < thatPart) return -1;
            if(thisPart > thatPart) return 1;
        }

        return 0;
    }

    public int compareTo(String that) {
        if(that == null) return 1;
        return this.compareTo(new Version(that));
    }

    @Override
    public boolean equals(Object that) {
        if(this == that) return true;
        if(that == null) return false;
        if(this.getClass() != that.getClass()) return false;
        return this.compareTo((Version) that) == 0;
    }
}