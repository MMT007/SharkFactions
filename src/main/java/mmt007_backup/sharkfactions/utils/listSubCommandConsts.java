package mmt007_backup.sharkfactions.utils;

import mmt007_backup.sharkfactions.commands.CommandManager;
import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.models.Factions;

import java.util.ArrayList;

public class listSubCommandConsts {
    private static ArrayList<ArrayList<String>> pageList = new ArrayList<>();
    public static void loadPages(){
        ArrayList<ArrayList<String>> pageList = new ArrayList<>();
        ArrayList<String> factionsList = new ArrayList<>();

        int factionsPerPage = 8;

        for(Factions fac : JsonTableUtil.factions.values()){
            factionsList.add("§7[Factions] §b" +
                    fac.getName() +
                    " [§3" +
                    fac.getTag() +
                    "]\n");
        }

        int pages = (int)Math.ceil((double) JsonTableUtil.factions.size() / factionsPerPage);

        for (int j = 0; j < pages; j++) {
            ArrayList<String> factionsOnPage = new ArrayList<>();
            for (int i = 0; i < factionsPerPage; i++) {
                String Command =
                        i + (j * 8) < factionsList.size() ? factionsList.get(i + (j * 8)) : "";
                factionsOnPage.add(Command);
            }
            pageList.add(factionsOnPage);
        }

       listSubCommandConsts.pageList = pageList;
    }

    public static ArrayList<String> getPage(int page){
        return page - 1 < 0 || page - 1 >= pageList.size() ? pageList.get(0) : pageList.get(page - 1);
    }
    
    public static String getPageNum(int num){
        return String.valueOf(num - 1 >= pageList.size() ? 1 : num); 
    }
}
