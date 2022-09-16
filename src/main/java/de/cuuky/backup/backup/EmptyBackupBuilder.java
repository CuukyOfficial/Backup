package de.cuuky.backup.backup;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class EmptyBackupBuilder implements BackupBuilder {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    private String title;

    private String getCurrentDate() {
        return DATE_FORMAT.format(new Date());
    }

    public void zip(File file) {
        Path sourceDir = Paths.get("");
        try {
            if (!file.exists()) {
                file.mkdir();
                file.createNewFile();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String zipFileName = file.getPath();
        try {
            ZipOutputStream outputStream = new ZipOutputStream(Files.newOutputStream(Paths.get(zipFileName)));
            Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
                    try {
                        if (file.getFileName().toString().endsWith(Backup.FILE_ENDING))
                            return FileVisitResult.CONTINUE;

                        Path targetFile = sourceDir.relativize(file);
                        outputStream.putNextEntry(new ZipEntry(targetFile.toString()));
                        byte[] bytes = Files.readAllBytes(file);
                        outputStream.write(bytes, 0, bytes.length);
                        outputStream.closeEntry();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BackupBuilder title(String title) {
        this.title = title;
        return this;
    }

    @Override
    public Backup build(File folder) {
        String title = this.title != null ? this.title : this.getCurrentDate();
        File backupFile = new File(folder, title + Backup.FILE_ENDING);
        this.zip(backupFile);
        return new BackupImpl(backupFile, title);
    }

    @Override
    public Backup fromFile(File backup) {
        return new BackupImpl(backup, backup.getName().replace(Backup.FILE_ENDING, ""));
    }
}
