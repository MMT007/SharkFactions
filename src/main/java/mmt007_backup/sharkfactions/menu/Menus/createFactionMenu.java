package mmt007_backup.sharkfactions.menu.Menus;

import mmt007_backup.sharkfactions.lang.languageUtil;
import mmt007_backup.sharkfactions.menu.Menu;
import mmt007_backup.sharkfactions.menu.MenuMngr;
import mmt007_backup.sharkfactions.menu.Utils.MenuCreationUtil;
import mmt007_backup.sharkfactions.menu.models.InputType;
import mmt007_backup.sharkfactions.menu.models.PlayerMenuUtility;
import mmt007_backup.sharkfactions.menu.models.playerOnInput;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class createFactionMenu extends Menu {
    public createFactionMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public @NotNull String getName() {return "Criar Facção";}
    @Override
    public int getSize() {return 9;}
    public void openInventory(){
        inventory = Bukkit.createInventory(this, getSize(), getName());
        this.setMenuItems();
        playerMenuUtility.getOwner().openInventory(inventory);
    }
    @Override
    public void peform(InventoryClickEvent e) {
        if(e.getCurrentItem() == null){return;}
        Player plr = (Player) e.getWhoClicked();
        playerOnInput poi = new playerOnInput(plr, InputType.NONE, true);

        switch (e.getCurrentItem().getType()) {
            case OAK_SIGN -> {
                plr.closeInventory();
                plr.sendMessage(languageUtil.getMessage("menuItem-createFaction-usage"));
                poi.setType(InputType.CREATE);
                MenuMngr.setPlayerOnInput(poi);
            }
            case PAPER -> {
                plr.closeInventory();
                plr.sendMessage(languageUtil.getMessage("menuItem-getFactionName-usage"));
                poi.setType(InputType.INFO);
                MenuMngr.setPlayerOnInput(poi);
            }
        }
    }
    @Override
    public void setMenuItems() {
        ItemStack[] items = MenuCreationUtil.createBackGround(
                getSize(), Material.LIGHT_BLUE_STAINED_GLASS_PANE);

        items[2] = MenuCreationUtil.createItem(
                languageUtil.getMessage("menuItem-create"),
                Material.OAK_SIGN,
                new ArrayList<>());
        items[6] =  MenuCreationUtil.createItem(
                languageUtil.getMessage("menuItem-info"),
                Material.PAPER,
                new ArrayList<>());

        inventory.setContents(items);
    }
}
