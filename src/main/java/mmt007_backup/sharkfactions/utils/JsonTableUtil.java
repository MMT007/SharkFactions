package mmt007_backup.sharkfactions.utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

import com.google.gson.GsonBuilder;
import mmt007_backup.sharkfactions.lang.languageUtil;
import mmt007_backup.sharkfactions.models.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import mmt007_backup.sharkfactions.SharkFMain;

public class JsonTableUtil {
    private static final SharkFMain SHARK_F_MAIN = SharkFMain.getPlugin();
    public static ArrayList<Players> players = new ArrayList<>();

    public static Map<Integer, Factions> factions = new HashMap<>();
    public static Map<String,Integer> facNameMap = new HashMap<>();
    public static Map<String,Integer> facUUIDMap = new HashMap<>();

    public static HashMap<String, Integer> playerIndex = new HashMap<>();

    public JsonTableUtil() {
    }


    public static void createPlayer(Player plr) {
        for(Players ep : players) {
            if(Objects.equals(ep.getUuid(), plr.getUniqueId().toString())){
                return;
            }
        }
        Players newPlr = new Players(
                plr.getUniqueId().toString(),
                "",
                Invite.getEmpty());
        players.add(newPlr);
        savePlayerTable();
    }

    public static Players getPlayer(String id) {
        Players player = Players.getEmpty();
        for(Players plrq : players){
            if(Objects.equals(plrq.getUuid(), id)){
                player = plrq;
            }
        }

        return player;
    }
    public static Players getPlayer(Player plr){
        return getPlayer(plr.getUniqueId().toString());
    }

    public static void updatePlayer(Players plr) {
        for (Players player : players) {
            if (Objects.equals(player.getUuid(), plr.getUuid())) {
                player.setFuuid(plr.getFuuid());
                player.setInvite(plr.getInvite());
                savePlayerTable();
                break;
            }
        }
    }

    public static ArrayList<Players> getPlayersInFaction(String id) {
        ArrayList<Players> plrs = new ArrayList<>();

        for(Players plr : players){
            if(Objects.equals(plr.getFuuid(), id)){
                plrs.add(plr);
            }
        }

        return plrs;
    }



    public static int isChunkFromPlayerFactions(Player player, FChunk chunk) {
        Factions f = getFaction(player);

        for (Factions fac : factions.values()) {
            for (FChunk chk : fac.getChunks()) {
                if (chunk.getX() == chk.getX() && chunk.getY() == chk.getY()) {
                    if(f.getUuid().equals(fac.getUuid()))
                        return 1;
                    if(f.getAllys().contains(fac.getUuid()))
                        return 2;
                    if(f.getEnemies().contains(fac.getUuid()))
                        return -1;

                    return 3;
                }
            }
        }

        return 0;
    }

    public static String chunkInfo(Player plr, FChunk chk) {
        Factions PFac = getFaction(plr);

        if(chk == null){
            return "§eNull";
        }

        String colorcode = "§7";

        for (Factions fac : factions.values()) {
            for(String id : PFac.getEnemies()){
                if (Objects.equals(fac.getUuid(), id)) {
                    colorcode = "§c";
                    break;
                }
            }
            for(String id : PFac.getAllys()){
                if (Objects.equals(id, fac.getUuid())) {
                    colorcode = "§b";
                    break;
                }
            }

            for(FChunk c : PFac.getChunks()){
                if (c.equals(chk)) {
                    colorcode = "§a";
                    break;
                }
            }

            for (FChunk c : fac.getChunks()) {
                if (c.equals(chk)) {
                    String facname = fac.getName();
                    return colorcode + facname + " [" + fac.getTag().toUpperCase() + "]";
                }
            }
        }

        return languageUtil.getMessage("free-zone");
    }



    public static void createFaction(Factions fac) {
        if(!getFaction(fac.getUuid()).getUuid().equals("")){return;}

        factions.put(
                fac.getUuid().hashCode(),
                fac
        );

        saveFactionTable();
        listSubCommandConsts.loadPages();
    }

    public static Factions getFaction(String id_name) {
        Integer hash = facUUIDMap.get(id_name) == null ?
                facNameMap.get(id_name) : facUUIDMap.get(id_name);

        if(factions.get(hash) == null){return Factions.getEmpty();}

        return factions.get(hash);
    }
    public static Factions getFaction(Player plr){
        return getFaction(getPlayer(plr.getUniqueId().toString()).getFuuid());
    }

    public static void updateFaction(Factions fac) {

        if (fac == null){return;}

        if(getFaction(fac.getUuid()).getUuid().equals("")){return;}

        int key = facUUIDMap.get(fac.getUuid());
        factions.replace(key,fac);

        saveFactionTable();

    }

    public static void deleteFaction(String id) {
        Factions fac = getFaction(id);

        if(fac.getUuid().equals("")){return;}

        factions.remove(facUUIDMap.get(fac.getUuid()));

        listSubCommandConsts.loadPages();
        saveFactionTable();
    }



    public static void savePlayerTable() {
        Gson gson = new Gson();
        File file = new File(SHARK_F_MAIN.getDataFolder().getAbsolutePath() + "/PlayerTable.json");
        file.getParentFile().mkdir();

        try {
            file.createNewFile();
            Writer writer = new FileWriter(file, false);
            gson.toJson(players, writer);
            writer.flush();
            writer.close();
            Bukkit.getLogger().info("[[ SFactions ]] Database Player Salva Com Sucesso");
        } catch (IOException e) {
            Bukkit.getLogger().warning("[[ SFactions ]] Não Foi Possivel Escrever No Arquivo JSON.");
        }

    }

    public static void saveFactionTable() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(SHARK_F_MAIN.getDataFolder().getAbsolutePath() + "/FactionTable.json");
        file.getParentFile().mkdir();

        try {
            file.createNewFile();
            Writer writer = new FileWriter(file, false);

            Type type = new TypeToken<Map<Tuple<String,String>,Factions>>(){}.getType();
            Map<String,Factions> temp = new HashMap<>();
            factions.forEach((k,v) -> temp.put(k.toString(),v));

            gson.toJson(temp,type,writer);

            writer.flush();
            writer.close();

            Bukkit.getLogger().info("[[ SFactions ]] Database Faction Salva Com Sucesso");
        } catch (IOException e) {
            Bukkit.getLogger().warning("[[ SFactions ]] Não Foi Possivel Escrever No Arquivo JSON.");
        }

    }

    public static void loadTables() {
        Gson gson = new Gson();
        File pfile = new File(SHARK_F_MAIN.getDataFolder().getAbsolutePath() + "/PlayerTable.json");
        File ffile = new File(SHARK_F_MAIN.getDataFolder().getAbsolutePath() + "/FactionTable.json");
        FileReader reader;
        if (pfile.exists()) {
            try {
                reader = new FileReader(pfile);
                Players[] p = gson.fromJson(reader, Players[].class);
                players = new ArrayList<>(Arrays.asList(p));
                Bukkit.getLogger().info("[[ SFactions ]] Database Player Carregada.");
            } catch (Exception e) {
                Bukkit.getLogger().warning("[[ SFactions ]] Não Foi Passivel Ler Arquivo PlayerTable.JSON.");
                Bukkit.getLogger().warning("[[ SFactions ]] " + e.getCause() + " " + Arrays.toString(e.getStackTrace()));
            }
        }

        if (ffile.exists()) {
            try {
                reader = new FileReader(ffile);

                Type type = new TypeToken<Map<Integer,Factions>>(){}.getType();
                Map<Integer,Factions> temp = gson.fromJson(reader, type);

                temp.forEach((k,v) -> {
                    facUUIDMap.put(v.getUuid(),k);
                    facNameMap.put(v.getName(),k);
                    factions.put(k, v);
                });

                Bukkit.getLogger().info("[[ SFactions ]] Database Factions Carregada.");
            } catch (Exception e) {
                Bukkit.getLogger().warning("[[ SFactions ]] Não Foi Passivel Ler Arquivo FactionTable.JSON.");
                Bukkit.getLogger().warning("[[ SFactions ]] " + e.getCause() + " " + Arrays.toString(e.getStackTrace()));
            }
        }

    }
}
