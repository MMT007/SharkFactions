package mmt007_backup.sharkfactions.utils;
import mmt007_backup.sharkfactions.SharkFMain;
import mmt007_backup.sharkfactions.lang.languageMngr;
import mmt007_backup.sharkfactions.models.FChunk;
import mmt007_backup.sharkfactions.models.Players;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Objects;
import java.util.UUID;

public class Utilitis {
    private static final Plugin main = SharkFMain.getPlugin();
    private static final Economy economy = SharkFMain.getEconomy();
    public boolean compareFChunktoChunk(Chunk chunk, FChunk fChunk){
        return fChunk.getY() == chunk.getZ() && fChunk.getX() == chunk.getX() && Objects.equals(fChunk.getWorld(), chunk.getWorld().getName());
    }

    public static Player getBukkitPlayer(Players plr){
        return Bukkit.getPlayer(UUID.fromString(plr.getUuid()));
    }
    public static OfflinePlayer getOfflineBukkitPlayer(Players plr){
        return Bukkit.getOfflinePlayer(UUID.fromString(plr.getUuid()));
    }

    public static boolean addBalance(Player plr, double cost){
        if(economy == null) return true;

        EconomyResponse response = economy.depositPlayer(plr,0 - cost);

        if(response.transactionSuccess()) {
            plr.sendMessage(languageMngr.getMessage("new-balance")
                    .replaceAll("%bal%", String.valueOf(response.balance)));
            return true;
        }
        else{
            plr.sendMessage(languageMngr.getMessage("error"));
            return false;
        }
    }
    public static boolean removeBalance(Player plr, double cost){
        if(economy == null) return true;

        double balance = economy.getBalance(plr);

        if(cost < 0) return addBalance(plr,cost);

        if(balance < cost){
            plr.sendMessage(languageMngr.getMessage("insufficient-balance")
                    .replaceAll("%cost%", String.valueOf(cost)));
            return false;
        }

        EconomyResponse response = economy.withdrawPlayer(plr,cost);

        if(response.transactionSuccess()) {
            plr.sendMessage(languageMngr.getMessage("new-balance")
                    .replaceAll("%bal%", String.valueOf(response.balance)));
            return true;
        }
        else{
            plr.sendMessage(languageMngr.getMessage("error"));
            return false;
        }
    }
    public static boolean removeBalance(Player plr, String cost,String addedCost,int multiplier){
        return removeBalance(
                plr,
                main.getConfig().getDouble(cost) + (main.getConfig().getDouble(addedCost) * (multiplier - 1)));
    }
    public static boolean removeBalance(Player plr, String cost){
        return removeBalance(plr, main.getConfig().getDouble(cost));}


    public static boolean isFactionOwner(Player plr){
        return JsonTableUtil.getFaction(plr).getOwner().equals(plr.getUniqueId().toString());
    }
}
