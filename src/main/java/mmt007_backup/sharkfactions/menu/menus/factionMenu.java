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

public class factionMenu extends Menu {
    public factionMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public @NotNull String getName() {return "Facção";}


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
        playerOnInput poi = new playerOnInput(plr, InputType.NONE,true);
        switch (e.getCurrentItem().getType()) {
            case OAK_DOOR -> {
                plr.closeInventory();
                new homeSubCommand().perform(plr, new String[0]);
            }
            case PAPER -> {
                plr.closeInventory();
                plr.closeInventory();
                plr.sendMessage(languageMngr.getMessage("menuItem-getFactionName-usage"));
                poi.setType(InputType.INFO);
                MenuMngr.setPlayerOnInput(poi);
            }
            case BARRIER -> {
                plr.closeInventory();
                new leaveSubCommand().perform(plr, new String[0]);
            }
        }
    }
    @Override
    public void setMenuItems () {
        ItemStack[] items = MenuCreationUtil.createBackGround(
                getSize(), Material.LIGHT_BLUE_STAINED_GLASS_PANE
                ,getBorder(),getBorderColor());

        items[16] =  MenuCreationUtil.createItem(
                languageMngr.getMessage("menuItem-leave"),
                Material.BARRIER,
                new ArrayList<>());
        items[13] =  MenuCreationUtil.createItem(
                languageMngr.getMessage("menuItem-teleport"),
                Material.OAK_DOOR,
                new ArrayList<>());
        items[10] =  MenuCreationUtil.createItem(
                languageMngr.getMessage("menuItem-info"),
                Material.PAPER,
                new ArrayList<>());

        inventory.setContents(items);
    }
}
