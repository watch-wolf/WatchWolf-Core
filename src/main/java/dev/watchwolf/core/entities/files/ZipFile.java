package dev.watchwolf.core.entities.files;

import java.io.*;
import java.nio.file.Files;
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

    public void exportToDirectory(File f) throws IOException, RuntimeException {
        Files.createDirectories(f.toPath()); // create the directory (if not exists)

        // export zip
        // @author https://www.baeldung.com/java-compress-and-uncompress
        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(this.getData()));
        ZipEntry zipEntry = zis.getNextEntry();
        byte[] buffer = new byte[1024];
        while (zipEntry != null) {
            File newFile = newFile(f, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                // write file content
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();
    }

    // @author https://www.baeldung.com/java-compress-and-uncompress
    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
}
