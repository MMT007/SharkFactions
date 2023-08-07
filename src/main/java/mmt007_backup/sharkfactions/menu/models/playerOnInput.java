package mmt007_backup.sharkfactions.menu.models;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

public class playerOnInput {
    private Player plr;
    private boolean isOnInput;
    private InputType type;

    public playerOnInput(Player plr, InputType type, boolean isOnInput) {
        this.plr = plr;
        this.type = type;
        this.isOnInput = isOnInput;
    }

    public Player getPlr() {return plr;}
    public void setPlr(Player plr) {this.plr = plr;}
    public InputType getType() {return type;}
    public void setType(InputType type) {this.type = type;}
    public boolean isOnInput() {return isOnInput;}
    public void setOnInput(boolean onInput) {isOnInput = onInput;}
}
