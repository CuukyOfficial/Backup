package de.cuuky.backup.backup;

import java.io.File;

public interface BackupBuilder {

    BackupBuilder title(String title);

    Backup build(File folder);

    Backup fromFile(File backup);

}
