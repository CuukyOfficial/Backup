package de.cuuky.backup.backup;

import java.util.Calendar;

public interface Backup {

    String FILE_ENDING = ".zip";

    String getTitle();

    String getFileName();

    Calendar getCreationDate();

    long length();

    void delete();

}
