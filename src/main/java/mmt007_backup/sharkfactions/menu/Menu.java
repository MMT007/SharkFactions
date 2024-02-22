package mmt007_backup.sharkfactions.menu;

import mmt007_backup.sharkfactions.menu.models.MenuBorderType;
import mmt007_backup.sharkfactions.menu.models.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public abstract class Menu implements InventoryHolder {

    protected Inventory inventory;
    protected PlayerMenuUtility playerMenuUtility;

    public Menu(PlayerMenuUtility playerMenuUtility){
        this.playerMenuUtility = playerMenuUtility;
    }

    @NotNull
    public abstract String getName();
    public abstract int getSize();
    public abstract MenuBorderType getBorder();
    public abstract String getBorderColor();
    public void openInventory(){
            inventory = Bukkit.createInventory(this, getSize(), getName());
            this.setMenuItems();
            playerMenuUtility.getOwner().openInventory(inventory);
    }
    public abstract void peform(InventoryClickEvent e);
    public abstract void setMenuItems();

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
