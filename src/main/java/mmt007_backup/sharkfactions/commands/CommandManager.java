package mmt007_backup.sharkfactions.commands;

import mmt007_backup.sharkfactions.SharkFMain;
import mmt007_backup.sharkfactions.commands.subCommands.*;
import mmt007_backup.sharkfactions.lang.languageMngr;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import mmt007_backup.sharkfactions.utils.protectedChunkUtil;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements TabExecutor {
    private static final Plugin main = SharkFMain.getPlugin();
    private final Permission permission = SharkFMain.getPermission();
    private static final ArrayList<SubCommand> subCommands = new ArrayList<>();
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
        subCommands.add(new listSubCommand());
        subCommands.add(new mapSubCommand());
        subCommands.add(new menuSubCommand());
        subCommands.add(new sethomeSubCommand());
        subCommands.add(new truceSubCommand());
        subCommands.add(new unclaimSubCommand());
        subCommands.add(new protectRegionSubCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player plr){
            if(args.length > 0) {
                if (!command.getName().equalsIgnoreCase("Factions")){return true;}

                if (args[0].equalsIgnoreCase("reload")) {
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
                if (!command.getName().equalsIgnoreCase("Factions")){return true;}

                if (args[0].equalsIgnoreCase("reload")) {
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
                String acString = sb.getAutoComplete()[0];
                if(acString.contains("admin")){
                    if(sender.isOp() || permission.has(sender,sb.getPermission())){
                        commands.add(acString);
                    }
                }else{
                    commands.add(acString);
                }
            }

            if(sender.isOp() || permission.has(sender,"sharkfactions.admin.reload")){
                commands.add("Reload");
            }

            return commands;
        } else if (args.length > 1) {
            for(SubCommand sb : subCommands){
                if(sb.getName().equalsIgnoreCase(args[0])){
                    if(args.length >= sb.getAutoComplete().length) return null;
                    String acString = sb.getAutoComplete()[args.length - 1];
                    switch (acString){
                        case "{name}" ->{
                            List<String> names = new ArrayList<>();
                            JsonTableUtil.factions.values().forEach(v -> names.add(v.getName()));

                            return names;
                        }
                        case "{region}" ->{
                            return new ArrayList<>(protectedChunkUtil.getRegions().keySet());
                        }
                        default -> {
                            return List.of(acString.split(":"));
                        }
                    }
                }
            }
        }

        return null;
    }

    public ArrayList<SubCommand> getSubCommands(){return subCommands;}

    private void reloadFiles(CommandSender sender) {
        if (sender instanceof Player plr) {
            if (plr.isOp()) {
                JsonTableUtil.loadTables();
                languageMngr.loadMessages();
                main.reloadConfig();
                plr.sendMessage(languageMngr.getMessage("config-loaded"));
            } else {
                plr.sendMessage(languageMngr.getMessage("cant-perform-action"));
            }
        } else if (sender instanceof ConsoleCommandSender) {
            main.reloadConfig();
            JsonTableUtil.loadTables();
            languageMngr.loadMessages();
        }

    }

    public static String[] removeSubCommand(String[] args){
        String[] newArgs = new String[args.length - 1];

        for(int i = 1; i < args.length; i++){
            newArgs[i-1] = args[i];
        }

        return newArgs;
    }

    static {
        for(SubCommand sb : subCommands){
            org.bukkit.permissions.Permission perm = new org.bukkit.permissions.Permission(sb.getPermission());
            main.getServer().getPluginManager().addPermission(perm);
        }

        org.bukkit.permissions.Permission perm = new org.bukkit.permissions.Permission("msharkfactions.admin.reload");
        main.getServer().getPluginManager().addPermission(perm);

    }
}
