package mmt007_backup.sharkfactions;

import java.util.ArrayList;
import java.util.Objects;

import mmt007_backup.sharkfactions.models.Factions;
import mmt007_backup.sharkfactions.models.InviteType;
import mmt007_backup.sharkfactions.models.Players;
import mmt007_backup.sharkfactions.utils.Utilitis;
import mmt007_backup.sharkfactions.lang.languageMngr;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;
import mmt007_backup.sharkfactions.models.FChunk;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;

public class GeneralEventHandler implements Listener {
    Plugin main = SharkFMain.getPlugin();

    private final Utilitis utils = new Utilitis();

    public GeneralEventHandler() {
    }

    @EventHandler
    public void playerHitPlayer(EntityDamageByEntityEvent e){
        Player attacker = null;
        Player attacked = null;

        if(e.getDamager() instanceof Player plr){attacker = plr;}
        if(e.getEntity() instanceof  Player plr){attacked = plr;}

        if(attacked == null || attacker == null ){return;}

        Factions attackerFac = JsonTableUtil.getFaction(attacker);
        Factions attackedFac = JsonTableUtil.getFaction(attacked);

        for(String id : attackerFac.getAllys()){
            if(Objects.equals(attackedFac.getUuid(), id)){
                attacker.sendMessage(languageMngr.getMessage("cant-attack-ally"));
                e.setCancelled(true);
                return;
            }
        }
        Players attackerP = JsonTableUtil.getPlayer(attacker.getUniqueId().toString());
        Players attackedP = JsonTableUtil.getPlayer(attacked.getUniqueId().toString());

        if(Objects.equals(attackerP.getFuuid(), attackedP.getFuuid())){
            attacker.sendMessage(languageMngr.getMessage("cant-attack-ally"));
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player plr = e.getPlayer();
        Factions fac = JsonTableUtil.getFaction(plr);

        if(Objects.equals(fac.getOwner(), plr.getUniqueId().toString())){
            if(fac.getInvite().getType().equals(InviteType.NONE)){
                return;
            }

            Factions PFac = JsonTableUtil.getFaction(fac.getInvite().getUUID());
            switch (fac.getInvite().getType()){

                case ALLY -> {
                    plr.sendMessage(languageMngr.getMessage("faction-ally-invite-received")
                            .replaceAll("%fac%",PFac.getName()));
                }
                case TRUCE -> {
                    plr.sendMessage(languageMngr.getMessage("faction-truce-invite-received")
                            .replaceAll("%fac%",PFac.getName()));
                }
            }

        }

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player plr = e.getPlayer();
        if (!e.getBlock().getType().isAir()) {
            Chunk chk = e.getBlock().getChunk();
            FChunk chunk = new FChunk(chk.getX(), chk.getZ(), chk.getWorld().getName());
            if (JsonTableUtil.isChunkFromPlayerFactions(plr, chunk) == -1) {
                plr.sendMessage(languageMngr.getMessage("cant-perform-action"));
                e.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void playerTeleport(PlayerTeleportEvent e) {
        Player plr = e.getPlayer();
        if (e.getCause().equals(TeleportCause.CHORUS_FRUIT)) {
            Chunk chk = Objects.requireNonNull(e.getTo()).getChunk();
            FChunk chunk = new FChunk(chk.getX(), chk.getZ(), chk.getWorld().getName());
            if (JsonTableUtil.isChunkFromPlayerFactions(plr, chunk) == -1) {
                plr.sendMessage(languageMngr.getMessage("cant-perform-action"));
                e.setCancelled(true);
            }

        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player plr = e.getPlayer();
        Chunk chk = e.getBlock().getChunk();
        FChunk chunk = new FChunk(chk.getX(), chk.getZ(), chk.getWorld().getName());
        if (JsonTableUtil.isChunkFromPlayerFactions(plr, chunk) == -1) {

            if (main.getConfig().getBoolean("use-invasion")) {
                for (String block : this.main.getConfig().getStringList("invasion-blocks")) {
                    if (Objects.equals(e.getBlock().getType().name().toLowerCase(), block.toLowerCase())) {
                        return;
                    }
                }
            }

            plr.sendMessage(languageMngr.getMessage("cant-perform-action"));
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent e) {
        Player plr = e.getPlayer();
        Block block = e.getClickedBlock();
        if (block != null) {
            if (!e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                if (block.getType().isInteractable()) {
                    Chunk chk = e.getClickedBlock().getChunk();
                    FChunk chunk = new FChunk(chk.getX(), chk.getZ(), chk.getWorld().getName());
                    if (JsonTableUtil.isChunkFromPlayerFactions(plr, chunk) == -1) {
                        plr.sendMessage(languageMngr.getMessage("cant-perform-action"));
                        e.setCancelled(true);
                    }

                }
            }
        }
    }

    @EventHandler
    public void playerMove(PlayerMoveEvent e) {
        Player plr = e.getPlayer();

        Location from =  e.getFrom();
        Location to = e.getTo();

        if((from.getBlockX() >> 4) != (to.getBlockX() >> 4) || (from.getBlockZ() >> 4) != (to.getBlockZ() >> 4)){
            FChunk lastChunk = FChunk.fromBukkit(from.getChunk());
            FChunk currentChunk = FChunk.fromBukkit(to.getChunk());
            String lastChunkDominance = JsonTableUtil.chunkInfo(plr, lastChunk);
            String currentChunkDominance = JsonTableUtil.chunkInfo(plr, currentChunk);

            if(lastChunkDominance.equals(currentChunkDominance)){
                return;
            }

            plr.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(currentChunkDominance));
        }
    }

    @EventHandler
    public void playerInteractEntity(PlayerInteractEntityEvent e){
        boolean playerInteractonEntity = main.getConfig().getBoolean("faction-player-interact-entity");
        if(playerInteractonEntity){
            return;
        }

        Player plr = e.getPlayer();
        Factions pFac = JsonTableUtil.getFaction(plr);

        Chunk chunk = e.getRightClicked().getLocation().getChunk();


        for(FChunk fChunk : pFac.getChunks()){
            if(utils.compareFChunktoChunk(chunk, fChunk)){
                return;
            }
        }
        ArrayList<String> alliesUUIDs = pFac.getAllys();
        ArrayList<Factions> allies = new ArrayList<>();

        for(String allyUUID : alliesUUIDs){
            Factions addAlly = JsonTableUtil.getFaction(allyUUID);
            if(!addAlly.getUuid().equals("")){
                allies.add(addAlly);
            }
        }
        for(Factions ally : allies){
            for(FChunk fChunk : ally.getChunks()){
                if(!utils.compareFChunktoChunk(chunk, fChunk)){
                    plr.sendMessage(languageMngr.getMessage("cant-perform-action"));
                    e.setCancelled(true);
                }
            }
        }
    }
}
