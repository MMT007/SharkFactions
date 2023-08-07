package mmt007_backup.sharkfactions.menu.Utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class MenuCreationUtil {
    public static ItemStack createItem(String name, Material material,ArrayList<String> lore){
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);

        return item;
    }

    public static ItemStack[] createBackGround(int Size, Material material){
        ItemStack[] items = new ItemStack[Size];
        ItemStack backGroundItem = createItem(
                " ",
                material,
                new ArrayList<>());

        for(int i = 0; i < Size; i++){
            items[i] = backGroundItem;
        }

        return items;
    }
}
