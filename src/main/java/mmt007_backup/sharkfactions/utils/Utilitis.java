package mmt007_backup.sharkfactions.utils;

import mmt007_backup.sharkfactions.models.FChunk;
import mmt007_backup.sharkfactions.models.Players;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class Utilitis {
    public boolean compareFChunktoChunk(Chunk chunk, FChunk fChunk){
        return fChunk.getY() == chunk.getZ() && fChunk.getX() == chunk.getX() && Objects.equals(fChunk.getWorld(), chunk.getWorld().getName());
    }

    public static Player getBukkitPlayer(Players plr){
        return Bukkit.getPlayer(UUID.fromString(plr.getUuid()));
    }
    public static OfflinePlayer getOfflineBukkitPlayer(Players plr){
        return Bukkit.getOfflinePlayer(UUID.fromString(plr.getUuid()));
    }

    public static boolean getIfChunksIsOnArray(ArrayList<FChunk> chunkA, FChunk chunk){
        for(FChunk chunkIA : chunkA){
            if((chunkIA.getX() == chunk.getX()) &&
                (chunkIA.getY() == chunk.getY()) &&
                    chunkIA.getWorld().equalsIgnoreCase(chunk.getWorld())){
                return true;
            }
        }

        return false;
    }
}
