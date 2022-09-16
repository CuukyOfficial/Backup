package de.cuuky.backup;

import de.cuuky.backup.backup.Backup;
import de.cuuky.backup.backup.EmptyBackupBuilder;
import de.cuuky.backup.commands.BackupCommand;
import de.cuuky.backup.utils.TimeUtils;
import de.varoplugin.cfw.inventory.AdvancedInventoryManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Stream;

public class Main extends JavaPlugin {

	private static final String PREFIX = "§7[§aBackup§7] ";
	private static final String CONSOLE_PREFIX = "[BackupImpl] ";
	private static final File FOLDER = new File("plugins/Backup/");

	private AdvancedInventoryManager inventoryManager;
	private Collection<Backup> backups;

	@Override
	public void onEnable() {
		System.out.println(CONSOLE_PREFIX + "Enabling...");

		this.backups = new LinkedList<>();
		this.loadBackups();
		this.inventoryManager = new AdvancedInventoryManager(this);
		this.registerCommands();

		this.scheduleToNextHour();

		System.out.println(CONSOLE_PREFIX + "Enabled!");
		super.onEnable();
	}

	@Override
	public void onDisable() {
		System.out.println(CONSOLE_PREFIX + "Disabling...");
		
		this.getServer().getScheduler().cancelTasks(this);

		System.out.println(CONSOLE_PREFIX + "Disabled!");
		super.onDisable();
	}

	private void loadBackups() {
		for (File file : Objects.requireNonNull(FOLDER.listFiles())) {
			if (file.getName().endsWith(Backup.FILE_ENDING)) {
				this.backups.add(new EmptyBackupBuilder().fromFile(file));
			}
		}
	}

	private void scheduleToNextHour() {
		new BukkitRunnable() {
			@Override
			public void run() {
				createBackup();
				scheduleOneDay();
			}
		}.runTaskLaterAsynchronously(this, TimeUtils.getNextReset(1) * 20);
	}

	private void scheduleOneDay() {
		new BukkitRunnable() {
			@Override
			public void run() {
				createBackup();
			}
		}.runTaskTimerAsynchronously(this, TimeUtils.TWENTY_FOUR_HOURS * 20, TimeUtils.TWENTY_FOUR_HOURS * 20);
	}

	private void registerCommands() {
		this.getCommand("backup").setExecutor(new BackupCommand(this));
	}

	public AdvancedInventoryManager getInventoryManager() {
		return this.inventoryManager;
	}

	public Backup createBackup() {
		Backup backup = new EmptyBackupBuilder().build(FOLDER);
		this.backups.add(backup);
		System.out.println(CONSOLE_PREFIX + " Backup created.");
		return backup;
	}

	public void removeBackup(Backup backup) {
		backup.delete();
		this.backups.remove(backup);
	}

	public Stream<Backup> getBackups() {
		return this.backups.stream();
	}

	public static String getPrefix() {
		return PREFIX;
	}
}
