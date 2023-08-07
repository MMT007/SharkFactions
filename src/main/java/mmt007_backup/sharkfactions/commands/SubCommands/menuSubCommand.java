package mmt007_backup.sharkfactions.commands.SubCommands;

import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.menu.MenuMngr;
import mmt007_backup.sharkfactions.menu.Menus.createFactionMenu;
import mmt007_backup.sharkfactions.menu.Menus.factionMenu;
import mmt007_backup.sharkfactions.menu.Menus.factionOwnerMenu;
import mmt007_backup.sharkfactions.menu.models.PlayerMenuUtility;
import mmt007_backup.sharkfactions.models.Factions;
import mmt007_backup.sharkfactions.models.Players;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
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
        Factions fac = JsonTableUtil.getFaction(p.getFuuid());

        if(Objects.equals(p.getFuuid(), "")){
            new createFactionMenu(playerMenuUtility).openInventory();
        }else if(Objects.equals(fac.getOwner(), p.getUuid())){
            new factionOwnerMenu(playerMenuUtility).openInventory();
        }else{
            new factionMenu(playerMenuUtility).openInventory();
        }
    }
}
