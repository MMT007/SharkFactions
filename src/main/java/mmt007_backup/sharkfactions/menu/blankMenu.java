package mmt007_backup.sharkfactions.menu;

import mmt007_backup.sharkfactions.menu.utils.MenuCreationUtil;
import mmt007_backup.sharkfactions.menu.models.InputType;
import mmt007_backup.sharkfactions.menu.models.PlayerMenuUtility;
import mmt007_backup.sharkfactions.menu.models.playerOnInput;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class blankMenu extends Menu {
    public blankMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public @NotNull String getName() {return "";}

    @Override
    public int getSize() {return 0;}

    @Override
    public void peform(InventoryClickEvent e) {
        if(e.getCurrentItem() == null){return;}
        Player plr = (Player) e.getWhoClicked();
        playerOnInput poi = new playerOnInput(plr, InputType.NONE,true);
        switch (e.getCurrentItem().getType()) {

        }
    }

    @Override
    public void setMenuItems() {
        ItemStack[] items = MenuCreationUtil.createBackGround(
                getSize(), Material.LIGHT_BLUE_STAINED_GLASS_PANE);

        inventory.setContents(items);
    }
}
