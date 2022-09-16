package de.cuuky.backup.commands;

import de.cuuky.backup.Main;
import de.cuuky.backup.backup.Backup;
import de.cuuky.backup.inventory.BackupListInventory;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public class BackupCommand implements CommandExecutor {

	private final Main main;

	public BackupCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(Main.getPrefix() + "§7------ §aBackup §7------");
			sender.sendMessage(Main.getPrefix() + "§a/backup §7create");
			sender.sendMessage(Main.getPrefix() + "§a/backup §7list");
			sender.sendMessage(Main.getPrefix() + "§a/backup §7menu");
			sender.sendMessage(Main.getPrefix() + "§7-------------------");
			return false;
		}

		if (args[0].equalsIgnoreCase("menu") || args[0].equalsIgnoreCase("gui")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(Main.getPrefix() + "§7Nicht fuer die Konsole!");
				return false;
			}

			Player p = (Player) sender;
			p.playSound(p.getLocation(), Sound.CHEST_OPEN, 1, 1);
			new BackupListInventory(this.main, p).open();
			return false;
		} else if (args[0].equalsIgnoreCase("create")) {
			sender.sendMessage(Main.getPrefix() + "§aBackup §7wird nun erstellt...\n" + Main.getPrefix()
					+ "Das kann je nach §aLeistung §7und §aGröße §7des Servers länger dauern.");

			Backup backup = this.main.createBackup();
			if (backup == null) {
				sender.sendMessage(
						Main.getPrefix() + "§7Backup konnte nicht erstellt werden!");
				return false;
			}

			sender.sendMessage(Main.getPrefix() + "Es wurde erfolgreich ein neues BackupImpl unter '§a/plugins/Backups/"
					+ backup.getFileName() + "§7' gespeichert!");
		} else if (args[0].equalsIgnoreCase("list")) {
			if (!this.main.getBackups().findAny().isPresent()) {
				sender.sendMessage(Main.getPrefix() + "§7Keine Backups gefunden!");
				return false;
			}

			String list = "§7" + this.main.getBackups().map(Backup::getTitle).collect(Collectors.joining("§8, §7"));
			sender.sendMessage(Main.getPrefix() + "§7Hier eine Liste aller §aBackups§7: ");
			sender.sendMessage(list);
		} else
			sender.sendMessage(Main.getPrefix() + "§7Not found! §7Type /backup for help.");

		return true;
	}

}
