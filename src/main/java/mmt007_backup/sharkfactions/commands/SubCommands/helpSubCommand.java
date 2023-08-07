package mmt007_backup.sharkfactions.commands.SubCommands;

import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.lang.languageUtil;
import mmt007_backup.sharkfactions.utils.helpSubCommandConsts;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class helpSubCommand extends SubCommand {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Mostra Lista De Commandos";
    }

    @Override
    public String getSyntax() {
        return "Help";
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
        ArrayList<String> page = helpSubCommandConsts.getPage(pageNum);
        StringBuilder builder = new StringBuilder();

        for(String command : page){
            builder.append(command);
        }

        return """
                §7[Factions] §b------------------------------------------------------
                %commands
                §7[Factions]
                §7[Factions] %facchat%
                §7[Factions] §b Página : %page
                §7[Factions] §b------------------------------------------------------
                """.replace("%commands", builder.toString())
                .replace("%page", helpSubCommandConsts.getPageNum(pageNum))
                .replace("%facchat%", languageUtil.getMessage("help-factionChat"));
    }

    private static boolean isNumeric(String str){
        return str != null && str.matches("[0-99]+");
    }
}
