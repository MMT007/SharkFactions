package mmt007_backup.sharkfactions.commands;

import mmt007_backup.sharkfactions.commands.CommandManager;
import mmt007_backup.sharkfactions.commands.SubCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;

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
    public String getSyntax() {
        return "";
    }

    @Override
    public void perform(Player plr, String[] args) {

    }
}
