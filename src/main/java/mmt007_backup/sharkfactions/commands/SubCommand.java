package mmt007_backup.sharkfactions.commands;

import org.bukkit.entity.Player;

public abstract class SubCommand {
    public abstract String getName();
    public abstract String getDescription();
    public abstract String getPermission();
    public abstract String getSyntax();
    public abstract String[] getAutoComplete();
    public abstract void perform(Player plr, String[] args);

    protected String capitalize(String str){
        return str.substring(0,1).toUpperCase() + str.substring(1);
    }
}
