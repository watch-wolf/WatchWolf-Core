package dev.watchwolf.core.entities.files;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipFile extends ConfigFile {
    public ZipFile(String name, File f, String offset) throws IOException {
        super(name, f, offset);
        if (!this.getExtension().equals("zip")) throw new IllegalArgumentException("Only zip files are allowed to be this type!");
    }

    public ZipFile(String name, byte []file, String offset) {
        super(name, file, offset);
        if (!this.getExtension().equals("zip")) throw new IllegalArgumentException("Only zip files are allowed to be this type!");
    }

    public ZipFile(String name, String offset) throws IOException {
        super(name, offset);
        if (!this.getExtension().equals("zip")) throw new IllegalArgumentException("Only zip files are allowed to be this type!");
    }

    public void exportToDirectory(Path f) throws IOException, RuntimeException {
        Files.createDirectories(f); // create the directory (if not exists)

        // export zip
        // @author https://www.baeldung.com/java-compress-and-uncompress
        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(this.getData()));
        ZipEntry zipEntry = zis.getNextEntry();
        byte[] buffer = new byte[1024];
        char[] charBuffer;
        while (zipEntry != null) {
            Path newFile = newFile(f, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!Files.isDirectory(newFile)) {
                    Files.createDirectories(newFile);
                }
            } else {
                // fix for Windows-created archives
                Path parent = newFile.getParent();
                if (!Files.isDirectory(parent)) {
                    Files.createDirectories(parent);
                }

                // write file content
                try(BufferedWriter fos = Files.newBufferedWriter(newFile)) {
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        charBuffer = new String(buffer, StandardCharsets.UTF_8).toCharArray();
                        fos.write(charBuffer, 0, len);
                    }
                }
            }
            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();
    }

    // @author https://www.baeldung.com/java-compress-and-uncompress
    private static Path newFile(Path destinationDir, ZipEntry zipEntry) throws IOException {
        Path destFile = destinationDir.resolve(zipEntry.getName());

        if (!destFile.startsWith(destinationDir)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
}
