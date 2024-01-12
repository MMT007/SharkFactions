package mmt007_backup.sharkfactions.commands.subCommands;

import mmt007_backup.sharkfactions.SharkFMain;
import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.models.FChunk;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class mapSubCommand extends SubCommand {
    Plugin main = SharkFMain.getPlugin();
    int mapSize = main.getConfig().getInt("faction-map-size");
    @Override
    public String getName() {
        return "map";
    }

    @Override
    public String getDescription() {
        return "Mostra Mapa De Chunks";
    }

    @Override
    public String getSyntax() {
        return "Map";
    }

    @Override
    public void perform(Player plr, String[] args) {
        Chunk bukkitChunk = plr.getLocation().getChunk();
        FChunk chunk =  FChunk.fromBukkit(bukkitChunk);
        FChunk tempChunk = FChunk.fromBukkit(bukkitChunk);

        StringBuilder builder = new StringBuilder();

        int halfSize = (int)Math.floor(mapSize * .5);

        for(int z = -halfSize;z < halfSize +1; z++){
            builder.append("§7[Factions] ");

            for(int x = -halfSize; x < halfSize +1; x++){

                tempChunk.setX(chunk.getX() + x);
                tempChunk.setY(chunk.getY() + z);

                String colorCode = "";
                int status = JsonTableUtil.isChunkFromPlayerFactions(plr,tempChunk);

                if(x == 0 && z == 0) status = 2;

                switch(status){
                    case 0 -> colorCode = "§7";
                    case -1 -> colorCode = "§c";
                    case 1 -> colorCode = "§a";
                    case 2 -> colorCode = "§b";
                    case 3 -> colorCode = "§f";
                }
                builder.append(colorCode).append("█");
            }

            builder.append("\n");
        }


        plr.sendMessage("""
                §7[Factions] §b--------------§3Facing : §a%f§b----------------
                %sqrs
                §7[Factions] §b-----------------------------------------------
                """.replaceAll("%f",getPlayerDirection(plr))
                .replaceAll("%sqrs",builder.toString()));
    }

    private String getPlayerDirection(Player plr){
        double yaw = plr.getLocation().getYaw() -180;

        if(yaw >= -135 && yaw < -45) return "W";
        if(yaw >= 135 || yaw < -135) return "S";
        if(yaw >= -45 && yaw < 45) return "N";
        if(yaw >= 45) return "E";

        return "ERROR";
    }
}
