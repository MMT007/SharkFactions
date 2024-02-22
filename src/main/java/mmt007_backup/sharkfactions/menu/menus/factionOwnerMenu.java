package mmt007_backup.sharkfactions.menu.menus;

import mmt007_backup.sharkfactions.commands.subCommands.homeSubCommand;
import mmt007_backup.sharkfactions.commands.subCommands.leaveSubCommand;
import mmt007_backup.sharkfactions.lang.languageMngr;
import mmt007_backup.sharkfactions.menu.Menu;
import mmt007_backup.sharkfactions.menu.MenuMngr;
import mmt007_backup.sharkfactions.menu.models.MenuBorderType;
import mmt007_backup.sharkfactions.utils.MenuCreationUtil;
import mmt007_backup.sharkfactions.menu.models.InputType;
import mmt007_backup.sharkfactions.menu.models.PlayerMenuUtility;
import mmt007_backup.sharkfactions.menu.models.playerOnInput;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class factionOwnerMenu extends Menu {
    public factionOwnerMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public @NotNull String getName() {return "Faction";}

    @Override
    public int getSize() {return 36;}

    @Override
    public MenuBorderType getBorder() {return MenuBorderType.HALFCROSS;}

    @Override
    public String getBorderColor() {return "blue";}

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
            case OAK_DOOR -> {
                plr.closeInventory();
                new homeSubCommand().perform(plr, new String[0]);
            }
            case PAPER -> {
                plr.closeInventory();
                plr.sendMessage(languageMngr.getMessage("menuItem-info-usage"));
                poi.setType(InputType.INFO);
                MenuMngr.setPlayerOnInput(poi);
            }
            case BARRIER -> {
                plr.closeInventory();
                new leaveSubCommand().perform(plr, new String[0]);
            }
            case DIAMOND -> {
                plr.closeInventory();
                plr.sendMessage(languageMngr.getMessage("menuItem-getFactionName-usage"));
                poi.setType(InputType.ALLY);
                MenuMngr.setPlayerOnInput(poi);
            }
            case IRON_SWORD -> {
                plr.closeInventory();
                plr.sendMessage(languageMngr.getMessage("menuItem-getFactionName-usage"));
                poi.setType(InputType.ENEMY);
                MenuMngr.setPlayerOnInput(poi);
            }
            case WRITABLE_BOOK -> {
                plr.closeInventory();
                plr.sendMessage(languageMngr.getMessage("menuItem-getPlayerName-usage"));
                poi.setType(InputType.INVITE);
                MenuMngr.setPlayerOnInput(poi);
            }
            case WHITE_BANNER ->{
                plr.closeInventory();
                plr.sendMessage(languageMngr.getMessage("menuItem-getFactionName-usage"));
                poi.setType(InputType.TRUCE);
                MenuMngr.setPlayerOnInput(poi);
            }
        }
    }
    @Override
    public void setMenuItems () {
        ItemStack[] items = MenuCreationUtil.createBackGround(
                getSize(), Material.LIGHT_BLUE_STAINED_GLASS_PANE
                ,getBorder(),getBorderColor());

        items[16] = MenuCreationUtil.createItem(
                languageMngr.getMessage("menuItem-disband"),
                Material.BARRIER,
                new ArrayList<>());
        items[12] = MenuCreationUtil.createItem(
                languageMngr.getMessage("menuItem-teleport"),
                Material.OAK_DOOR,
                new ArrayList<>());
        items[10] = MenuCreationUtil.createItem(
                languageMngr.getMessage("menuItem-info"),
                Material.PAPER,
                new ArrayList<>());
        items[15] = MenuCreationUtil.createItem(
                languageMngr.getMessage("menuItem-enemy"),
                Material.IRON_SWORD,
                new ArrayList<>());
        items[14] = MenuCreationUtil.createItem(
                languageMngr.getMessage("menuItem-ally"),
                Material.DIAMOND,
                new ArrayList<>());
        items[13] = MenuCreationUtil.createItem(
                languageMngr.getMessage("menuItem-invite"),
                Material.WRITABLE_BOOK,
                new ArrayList<>());
        items[24] = MenuCreationUtil.createItem(
                languageMngr.getMessage("menuItem-truce"),
                Material.WHITE_BANNER,
                new ArrayList<>());


        inventory.setContents(items);
    }
}
