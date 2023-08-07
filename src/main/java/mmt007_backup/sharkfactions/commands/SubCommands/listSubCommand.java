package mmt007_backup.sharkfactions.commands.SubCommands;

import mmt007_backup.sharkfactions.commands.CommandManager;
import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.lang.languageUtil;
import mmt007_backup.sharkfactions.models.Factions;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import mmt007_backup.sharkfactions.utils.helpSubCommandConsts;
import mmt007_backup.sharkfactions.utils.listSubCommandConsts;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class listSubCommand extends SubCommand {
    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "Mostra Uma Lista de Facções";
    }

    @Override
    public String getSyntax() {
        return "List";
    }

    @Override
    public void perform(Player plr, String[] args) {

        if(args.length > 0){
            if(isNumeric(args[0])) {
                plr.sendMessage(getMessage(Integer.parseInt(args[0])));
            }else {
                plr.sendMessage(getMessage(1));
            }
        }else{
            plr.sendMessage(getMessage(1));
        }
    }

    private static String getMessage(int pageNum){
        ArrayList<String> page = listSubCommandConsts.getPage(pageNum);
        StringBuilder builder = new StringBuilder();

        for(String faction : page){
            builder.append(faction);
        }

        return """
                §7[Factions] §b------------------------------------------------------
                %factions%
                §7[Factions]
                §7[Factions] §b Página : %page
                §7[Factions] §b------------------------------------------------------
                """.replace("%factions%", builder.toString())
                .replace("%page", listSubCommandConsts.getPageNum(pageNum));
    }

    private static boolean isNumeric(String str){
        return str != null && str.matches("[0-99]+");
    }
}
