package dev.watchwolf.core.entities.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class ConfigFile {
    private String name;
    private String extension;

    private String offsetPath;

    private byte []data;

    public ConfigFile(String name, File f, String offset) throws IOException {
        this(name, ConfigFile.getDataFromFile(f), offset);
    }

    public ConfigFile(String name, byte []file, String offset) {
        String []nameAndExtension = name.split("\\.(?!.*\\.)"); // split by the last '.'
        nameAndExtension[0] = (nameAndExtension[0].split("\\/(?!.*\\/)").length == 1) ? nameAndExtension[0] : nameAndExtension[0].split("\\/(?!.*\\/)")[1]; // remove folders
        this.name = nameAndExtension[0];
        this.extension = (nameAndExtension.length > 1) ? nameAndExtension[1] : null;
        this.offsetPath = (offset == null || offset.length() == 0) ? "./" : offset;
        if (this.offsetPath.charAt(offsetPath.length() - 1) != '/') this.offsetPath += "/"; // add '/' termination
        this.data = file;
    }

    public ConfigFile(String name, String offset) throws IOException {
        this(name, new File(name), offset);
    }

    private static byte []getDataFromFile(File f) throws IOException {
        FileInputStream fl = new FileInputStream(f);

        byte []r = new byte[(int)f.length()];
        fl.read(r);

        fl.close();
        return r;
    }

    public ConfigFile(String name, File f) throws IOException {
        this(name, f, "./");
    }

    public ConfigFile(String name) throws IOException {
        this(name, new File(name));
    }

    public String getName() {
        return name;
    }

    public String getExtension() {
        return extension;
    }

    public String getOffsetPath() {
        return offsetPath;
    }

    public byte[] getData() {
        return data;
    }

    public void saveToFile(File f) throws IOException, RuntimeException {
        if (f.isDirectory()) throw new RuntimeException("Can't save a directory");
        Files.createDirectories(f.getParentFile().toPath()); // create the directory (if not exists)
        Files.write(f.toPath(), this.data);
    }
}
