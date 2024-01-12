package mmt007_backup.sharkfactions.commands.subCommands;

import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.menu.MenuMngr;
import mmt007_backup.sharkfactions.menu.menus.createFactionMenu;
import mmt007_backup.sharkfactions.menu.menus.factionMenu;
import mmt007_backup.sharkfactions.menu.menus.factionOwnerMenu;
import mmt007_backup.sharkfactions.menu.models.PlayerMenuUtility;
import mmt007_backup.sharkfactions.models.Factions;
import mmt007_backup.sharkfactions.models.Players;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import mmt007_backup.sharkfactions.utils.Utilitis;
import org.bukkit.entity.Player;

import java.util.Objects;

public class menuSubCommand extends SubCommand {
    @Override
    public String getName() {
        return "menu";
    }

    @Override
    public String getDescription() {
        return "Abri O Menu da Facção";
    }

    @Override
    public String getSyntax() {return "Menu";}

    @Override
    public void perform(Player plr, String[] args) {
        PlayerMenuUtility playerMenuUtility = MenuMngr.getPlayerMenuUtility(plr);
        Players p = JsonTableUtil.getPlayer(plr.getUniqueId().toString());

        if(Objects.equals(p.getFuuid(), "")){
            new createFactionMenu(playerMenuUtility).openInventory();
            return;
        }

        if(Utilitis.isFactionOwner(plr)){
            new factionOwnerMenu(playerMenuUtility).openInventory();
            return;
        }

        new factionMenu(playerMenuUtility).openInventory();
    }
}
