package mmt007_backup.sharkfactions.commands;

import mmt007_backup.sharkfactions.SharkFMain;
import mmt007_backup.sharkfactions.commands.SubCommands.*;
import mmt007_backup.sharkfactions.lang.languageUtil;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements TabExecutor {
    private final Plugin main = SharkFMain.getPlugin();
    private final ArrayList<SubCommand> subCommands = new ArrayList<>();
    public CommandManager(){
        subCommands.add(new helpSubCommand());
        subCommands.add(new acceptSubCommand());
        subCommands.add(new allySubCommand());
        subCommands.add(new claimSubCommand());
        subCommands.add(new createSubCommand());
        subCommands.add(new denySubCommand());
        subCommands.add(new disbandSubCommand());
        subCommands.add(new enemySubCommand());
        subCommands.add(new homeSubCommand());
        subCommands.add(new infoSubCommand());
        subCommands.add(new inviteSubCommand());
        subCommands.add(new kickSubCommand());
        subCommands.add(new leaveSubCommand());
        subCommands.add(new menuSubCommand());
        subCommands.add(new sethomeSubCommand());
        subCommands.add(new unclaimSubCommand());
        subCommands.add(new truceSubCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player plr){
            if(args.length > 0) {
                if (command.getName().equalsIgnoreCase("Factions") && args[0].equalsIgnoreCase("reload")) {
                    this.reloadFiles(sender);
                    return true;
                }

                for (SubCommand subCommand : subCommands) {
                    if (args[0].equalsIgnoreCase(subCommand.getName())) {
                        args = removeSubCommand(args);
                        subCommand.perform(plr, args);
                        return true;
                    }
                }
            }

            new menuSubCommand().perform(plr,args);
        }else if (sender instanceof ConsoleCommandSender){
            if(args.length > 0) {
                if (command.getName().equalsIgnoreCase("Factions") && args[0].equalsIgnoreCase("reload")) {
                    this.reloadFiles(sender);
                    return true;
                }
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> commands = new ArrayList<>();
            for(SubCommand sb : subCommands){
                commands.add(sb.getName());
            }
            return commands;
        }

        return null;
    }

    public ArrayList<SubCommand> getSubCommands(){return subCommands;}

    private void reloadFiles(CommandSender sender) {
        if (sender instanceof Player plr) {
            if (plr.isOp()) {
                JsonTableUtil.loadTables();
                main.reloadConfig();
                plr.sendMessage(languageUtil.getMessage("config-loaded"));
            } else {
                plr.sendMessage(languageUtil.getMessage("cant-perform-action"));
            }
        } else if (sender instanceof ConsoleCommandSender) {
            main.reloadConfig();
            JsonTableUtil.loadTables();
        }

    }

    public static String[] removeSubCommand(String[] args){
        String[] newArgs = new String[args.length - 1];

        for(int i = 1; i < args.length; i++){
            newArgs[i-1] = args[i];
        }

        return newArgs;
    }
}
