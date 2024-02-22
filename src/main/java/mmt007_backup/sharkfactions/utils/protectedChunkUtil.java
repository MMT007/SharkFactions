package mmt007_backup.sharkfactions.utils;

import mmt007_backup.sharkfactions.SharkFMain;
import mmt007_backup.sharkfactions.models.FChunk;
import mmt007_backup.sharkfactions.models.Tuple;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class protectedChunkUtil {
    private static final Plugin main = SharkFMain.getPlugin();
    static File file;
    static FileConfiguration chunksFile;
    static HashMap<String, Tuple<FChunk,FChunk>> chunks = new HashMap<>();

    public static void setChunk(String region,Tuple<FChunk,FChunk> chunk){
        if(chunks.containsKey(region)) {
            chunks.replace(region, chunk);
        }else{
            chunks.put(region,chunk);
        }
        saveToFile();
    }

    public static HashMap<String,Tuple<FChunk,FChunk>> getRegions(){
        return chunks;
    }

    public static boolean removeRegion(String region){
        Object result = chunks.remove(region);
        saveToFile();
        return result != null;
    }

    private static void saveToFile(){
        chunks.forEach(protectedChunkUtil::serializeToYML);
        try {chunksFile.save(file);} catch (IOException ignored){}
    }

    public static void load(){
        for(String region : chunksFile.getKeys(false)){
            ConfigurationSection regionSec = chunksFile.getConfigurationSection(region);
            if(regionSec == null) continue;

            ConfigurationSection uC = regionSec.getConfigurationSection("upper");
            ConfigurationSection lC = regionSec.getConfigurationSection("lower");
            if(uC == null || lC == null) continue;

            FChunk upper = new FChunk(
                    uC.getInt("x"),
                    uC.getInt("y"),
                    uC.getString("w")
            );
            FChunk lower = new FChunk(
                    lC.getInt("x"),
                    lC.getInt("y"),
                    lC.getString("w")
            );

            Tuple<FChunk,FChunk> bounds = new Tuple<>(upper,lower);

            chunks.put(region,bounds);
        }
    }

    private static void serializeToYML(String region,Tuple<FChunk,FChunk> chunk){
        ConfigurationSection section = chunksFile.createSection(region);
        ConfigurationSection upper = section.createSection("upper");
        ConfigurationSection lower = section.createSection("lower");

        upper.set("x",chunk.x().getX());
        upper.set("y",chunk.x().getY());
        upper.set("w",chunk.x().getWorld());
        lower.set("x",chunk.y().getX());
        lower.set("y",chunk.y().getY());
        lower.set("w",chunk.y().getWorld());
    }

    static {
        file = new File(main.getDataFolder(),"protectedChunks.yml");

        if(!file.exists()){try{file.createNewFile();}catch (IOException ignored){}}

        chunksFile = YamlConfiguration.loadConfiguration(file);
    }
}
