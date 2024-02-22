package mmt007_backup.sharkfactions.commands;

import org.bukkit.entity.Player;

public class blankSubCommand extends SubCommand {
    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getPermission() {return "sharkfactions." + getName();}

    @Override
    public String getSyntax() {
        return "";
    }

    @Override
    public String[] getAutoComplete() {return new String[]{capitalize(getName())};}

    @Override
    public void perform(Player plr, String[] args) {

    }
}
