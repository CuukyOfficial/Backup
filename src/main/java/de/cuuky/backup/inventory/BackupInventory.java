package de.cuuky.backup.inventory;

import de.cuuky.backup.Main;
import de.cuuky.backup.backup.Backup;
import de.varoplugin.cfw.inventory.AdvancedInventory;
import de.varoplugin.cfw.utils.item.EmptyItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class BackupInventory extends AdvancedInventory {

	private final Main main;
	private final Backup backup;

	public BackupInventory(Main main, Player opener, Backup backup) {
		super(main.getInventoryManager(), opener);

		this.main = main;
		this.backup = backup;
	}

	@Override
	public String getTitle() {
		return "§cBackups";
	}

	@Override
	public int getSize() {
		return 27;
	}

	@Override
	public void refreshContent() {
		this.addItem(10, new EmptyItemBuilder().material(Material.PRISMARINE_SHARD)
				.displayName("§aLoad").lore("§cFunktion leider deaktiviert.",
						"§7Server kann nicht restored werden, wenn der Server läuft").build());

		this.addItem(13, new EmptyItemBuilder().material(Material.IRON_DOOR).displayName("§cBack")
				.lore("§7Kehre zurück zu den Backups").build(), (e) -> this.close());

		this.addItem(16, new EmptyItemBuilder().displayName("§4Delete").material(Material.BARRIER)
				.lore("§7Löscht das BackupImpl").build(), (e) -> {
			this.main.removeBackup(this.backup);
			this.getPlayer().playSound(this.getPlayer().getLocation(), Sound.ANVIL_BREAK, 1L, 1L);
			this.openNext(new BackupListInventory(this.main, this.getPlayer()));
		});
	}
}
