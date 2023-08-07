package mmt007_backup.sharkfactions.menu;

import mmt007_backup.sharkfactions.menu.models.PlayerMenuUtility;
import mmt007_backup.sharkfactions.menu.models.playerOnInput;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;

public class MenuMngr implements Listener {
private static final Map<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
private static final Map<Player, playerOnInput> playerOnInputMap = new HashMap<>();

    @EventHandler
    public void onItemClick(InventoryClickEvent e){
        if(e.getClickedInventory().getHolder() instanceof Menu holder){
            e.setCancelled(true);

            holder.peform(e);
        }
    }

    public static PlayerMenuUtility getPlayerMenuUtility(Player plr){
        PlayerMenuUtility playerMenuUtility;
        if(playerMenuUtilityMap.containsKey(plr)){
            return playerMenuUtilityMap.get(plr);
        }else{
            playerMenuUtility = new PlayerMenuUtility(plr);
            playerMenuUtilityMap.put(plr, playerMenuUtility);

            return playerMenuUtility;
        }
    }

    public static playerOnInput getPlayerOnInput(Player plr){
        playerOnInput PlayerOnInput;
        if(playerOnInputMap.containsKey(plr)){
            return playerOnInputMap.get(plr);
        }else{
            PlayerOnInput = new playerOnInput(plr,null,false );
            playerOnInputMap.put(plr, PlayerOnInput);

            return PlayerOnInput;
        }
    }
    public static void setPlayerOnInput(playerOnInput poi){
        playerOnInput old = getPlayerOnInput(poi.getPlr());
        playerOnInputMap.replace(old.getPlr(), poi);
    }
}

