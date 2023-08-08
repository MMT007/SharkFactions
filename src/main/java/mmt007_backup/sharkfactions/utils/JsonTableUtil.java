package mmt007_backup.sharkfactions.utils;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import mmt007_backup.sharkfactions.lang.languageUtil;
import mmt007_backup.sharkfactions.models.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import mmt007_backup.sharkfactions.SharkFMain;

public class JsonTableUtil {
    private static final SharkFMain SHARK_F_MAIN = SharkFMain.getPlugin();
    public static ArrayList<Players> players = new ArrayList<>();
    public static ArrayList<Factions> factions = new ArrayList<>();

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

    public static void updatePlayer(Players plr) {
        for (Players player : players) {
            if (Objects.equals(player.getUuid(), plr.getUuid())) {
                player.setFuuid(plr.getFuuid());
                player.setInvite(plr.getInvite());
                savePlayerTable();
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
        Players plr = getPlayer(player.getUniqueId().toString());

        for (Factions fac : factions) {
            for (FChunk chk : fac.getChunks()) {
                if (chunk.getX() == chk.getX() && chunk.getY() == chk.getY()) {
                    return fac.getUuid().equals(plr.getFuuid()) ? 1 : -1;
                }
            }
        }

        return 0;
    }

    public static String chunkInfo(Player plr, FChunk chk) {
        Factions PFac = getFactionByPlayer(plr);

        if(chk == null){
            return "§eNull";
        }

        String colorcode = "§7";

        for (Factions fac : factions) {
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
                if (c.getX() == chk.getX() && c.getY() == chk.getY() && Objects.equals(c.getWorld(), chk.getWorld())) {
                    colorcode = "§a";
                    break;
                }
            }

            for (FChunk c : fac.getChunks()) {
                if (c.getX() == chk.getX() && c.getY() == chk.getY() && Objects.equals(c.getWorld(), chk.getWorld())) {
                    String facname = fac.getName();
                    return colorcode + facname + " [" + fac.getTag().toUpperCase() + "]";
                }
            }
        }

        return languageUtil.getMessage("free-zone");
    }

    public static void createFaction(Factions fac) {
        for(Factions f : factions) {
            if(Objects.equals(f.getUuid(), fac.getUuid())){
                return;
            }
        }

        factions.add(fac);
        saveFactionTable();
        listSubCommandConsts.loadPages();
    }

    public static Factions getFaction(String id) {
        Factions fac = Factions.getEmpty();
        for(Factions f : factions){
            if(Objects.equals(f.getUuid(), id)){
                fac = f;
            }
        }
        return fac;
    }

    public static Factions getFactionByPlayer(Player plr) {
        return getFaction(getPlayer(plr.getUniqueId().toString()).getFuuid());
    }

    public static Factions getFactionByName(String name){
        for(Factions f : factions){
            if(f.getName().equalsIgnoreCase(name)){
                return f;
            }
        }

        return Factions.getEmpty();
    }

    public static void updateFaction(Factions fac) {

        if (fac == null){return;}

        for (Factions faction : factions) {
            if (Objects.equals(faction.getUuid(), fac.getUuid())) {
                faction.setOwner(fac.getOwner());
                faction.setName(fac.getName());
                faction.setTag(fac.getTag());
                faction.setMembers(fac.getMembers());
                faction.setChunks(fac.getChunks());
                saveFactionTable();
                break;
            }
        }

    }

    public static void deleteFaction(String id) {

        for (Factions faction : factions) {
            if (Objects.equals(faction.getUuid(), id)) {
                factions.remove(faction);
                listSubCommandConsts.loadPages();
                saveFactionTable();
                break;
            }
        }

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
        Gson gson = new Gson();
        File file = new File(SHARK_F_MAIN.getDataFolder().getAbsolutePath() + "/FactionTable.json");
        file.getParentFile().mkdir();

        try {
            file.createNewFile();
            Writer writer = new FileWriter(file, false);
            gson.toJson(factions, writer);
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
                players = new ArrayList(Arrays.asList(p));
                Bukkit.getLogger().info("[[ SFactions ]] Database Player Carregada.");
            } catch (IOException var6) {
                Bukkit.getLogger().warning("[[ SFactions ]] Não Foi Passivel Ler Arquivo PlayerTable.JSON.");
            }
        }

        if (ffile.exists()) {
            try {
                reader = new FileReader(ffile);
                Factions[] f = gson.fromJson(reader, Factions[].class);
                factions = new ArrayList(Arrays.asList(f));
                Bukkit.getLogger().info("[[ SFactions ]] Database Factions Carregada.");
            } catch (IOException var5) {
                Bukkit.getLogger().warning("[[ SFactions ]] Não Foi Passivel Ler Arquivo FActionTable.JSON.");
            }
        }

    }
}
