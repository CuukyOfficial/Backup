package de.cuuky.backup.inventory;

import de.cuuky.backup.Main;
import de.cuuky.backup.backup.Backup;
import de.varoplugin.cfw.inventory.Info;
import de.varoplugin.cfw.inventory.ItemClick;
import de.varoplugin.cfw.inventory.list.AdvancedListInventory;
import de.varoplugin.cfw.utils.item.EmptyItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BackupListInventory extends AdvancedListInventory<Backup> {

	private static final DateFormat FORMAT = new SimpleDateFormat("§7dd.MM.yyyy HH:mm:ss");
	private final Main main;

	public BackupListInventory(Main main, Player opener) {
		super(main.getInventoryManager(), opener, main.getBackups()
				.sorted(Collections.reverseOrder()).collect(Collectors.toList()));

		this.main = main;
	}

	@Override
	public String getTitle() {
		return "§aBackups";
	}

	@Override
	public void refreshContent() {
		this.addItem(this.getInfo(Info.SIZE) - 3, new EmptyItemBuilder().material(Material.EMERALD)
				.displayName("§7Create §aBackup").build(), (e) -> this.getPlayer().performCommand("backup create"));
		super.refreshContent();
	}

	@Override
	protected ItemStack getItemStack(Backup backup) {
		List<String> lore = new LinkedList<>();
		lore.add("§7Backup made on ");
		lore.add(FORMAT.format(backup.getCreationDate()));
		lore.add("§7Size: §a" + (backup.length() / 1000000) + " Megabyte");
		return new EmptyItemBuilder().displayName("§7" + backup.getTitle()).material(Material.DISPENSER)
				.lore(lore).build();
	}

	@Override
	protected ItemClick getClick(Backup backup) {
		return inventoryClickEvent -> this.openNext(new BackupInventory(this.main, this.getPlayer(), backup));
	}
}
