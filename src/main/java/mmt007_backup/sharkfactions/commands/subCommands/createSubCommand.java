package mmt007_backup.sharkfactions.commands.subCommands;

import mmt007_backup.sharkfactions.SharkFMain;
import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.lang.languageMngr;
import mmt007_backup.sharkfactions.models.*;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import mmt007_backup.sharkfactions.utils.Utilitis;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.UUID;

public class createSubCommand extends SubCommand {

    Plugin main = SharkFMain.getPlugin();
    private int factionNameMaxChar;
    private int factionTagMaxChar;


    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getDescription() {
        return "Cria Uma Facção";
    }

    @Override
    public String getPermission() {return "sharkfactions." + getName();}

    @Override
    public String[] getAutoComplete() {return new String[]{capitalize(getName())};}

    @Override
    public String getSyntax() {
        return "Create <Nome da Facção> <Tag da Facção>";
    }

    @Override
    public void perform(Player plr, String[] args) {
        setVars();

        if(args.length < 2) {
            plr.sendMessage("§7[Factions] /f" + getSyntax());
            return;
        }

        if (!JsonTableUtil.getPlayer(plr).getFuuid().equals("")) {
            plr.sendMessage(languageMngr.getMessage("faction-alreadyOn"));
        }

        if (args[0].replaceAll("&.{1}","").length() >= this.factionNameMaxChar + 1) {
            plr.sendMessage(languageMngr.getMessage("faction-nameLimit")
                    .replaceAll("%limit%", String.valueOf(this.factionNameMaxChar)));
        }

        if (args[1].length() >= this.factionTagMaxChar + 1) {
            plr.sendMessage(languageMngr.getMessage("faction-tagLimit")
                    .replaceAll("%limit%", String.valueOf(this.factionTagMaxChar)));
            return;
        }

        //-- checks If Player Has Enough Money
        if(!Utilitis.removeBalance(plr,"faction-create-cost")) return;

        String facName = args[0];
        String facTag = args[1];

        UUID id = UUID.randomUUID();
        Players dplr = new Players(
                plr.getUniqueId().toString()
                , id.toString(),
                new Invite("", InviteType.NONE));
        FLocation location = new FLocation(0.0, 0.0, 0.0, plr.getWorld().getName());
        Factions fac = new Factions(
                id.toString(),
                plr.getUniqueId().toString(),
                facName, facTag, 1,
                new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(),
                new Invite("", InviteType.NONE),
                location);

        JsonTableUtil.createFaction(fac);
        JsonTableUtil.updatePlayer(dplr);

        plr.sendMessage(languageMngr.getMessage("faction-created")
                .replaceAll("%fac%",facName)
                .replaceAll("%tag%",facTag));

    }

    public void setVars() {
        this.factionNameMaxChar = this.main.getConfig().getInt("faction-max-name-char");
        this.factionTagMaxChar = this.main.getConfig().getInt("faction-max-tag-char");
    }

}
