package de.cuuky.backup.backup;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

class BackupImpl implements Backup {

	private final File file;
	private final String title;
	private final Calendar created;

	public BackupImpl(File file, String title) {
		this.file = file;
		this.title = title;

		try {
			BasicFileAttributes attr = Files.readAttributes(Paths.get(file.getPath()), BasicFileAttributes.class);
			FileTime fileTime = attr.creationTime();
			this.created = new GregorianCalendar();
			this.created.setTimeInMillis(fileTime.toMillis());
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static ArrayList<String> getBackups() {
		File file = new File("plugins/Backup/");
		if (!file.isDirectory())
			file.mkdir();

		ArrayList<String> temp = new ArrayList<>();
		for (File listFile : file.listFiles())
			if (listFile.getName().contains(".zip"))
				temp.add(listFile.getName());
		return temp;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public String getFileName() {
		return this.file.getName();
	}

	@Override
	public Calendar getCreationDate() {
		return this.created;
	}

	@Override
	public long length() {
		return this.file.length();
	}

	@Override
	public void delete() {
		this.file.delete();
	}
}
