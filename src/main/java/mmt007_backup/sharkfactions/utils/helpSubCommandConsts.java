package mmt007_backup.sharkfactions.utils;

import mmt007_backup.sharkfactions.commands.CommandManager;
import mmt007_backup.sharkfactions.commands.SubCommand;

import java.util.ArrayList;

public class helpSubCommandConsts {
    private static ArrayList<ArrayList<String>> pageList = new ArrayList<>();
    public static void loadPages(){
        ArrayList<ArrayList<String>> tpageList = new ArrayList<>();
        ArrayList<String> commandList = new ArrayList<>();

        ArrayList<SubCommand> subCommands = new CommandManager().getSubCommands();
        int commandsPerPage = 5;

        for(SubCommand sb : subCommands){
            commandList.add("§7[Factions] §b" +
                    sb.getSyntax() +
                    " : §3" +
                    sb.getDescription() +
                    "\n");
        }

        int pages = (int)Math.ceil((double) subCommands.size() / commandsPerPage);

        for (int j = 0; j < pages; j++) {
            ArrayList<String> commandsOnPage = new ArrayList<>();
            for (int i = 0; i < commandsPerPage; i++) {
                String Command =
                        i + (j * 5) < commandList.size() ? commandList.get(i + (j * 5)) : "";
                commandsOnPage.add(Command);
            }
            tpageList.add(commandsOnPage);
        }

       pageList = tpageList;
    }

    public static ArrayList<String> getPage(int page){
        return page - 1 < 0 || page - 1 >= pageList.size() ? pageList.get(0) : pageList.get(page - 1);
    }
    
    public static String getPageNum(int num){
        return String.valueOf(num - 1 >= pageList.size() ? 1 : num); 
    }
}
