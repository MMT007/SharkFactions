package mmt007_backup.sharkfactions.utils;

import mmt007_backup.sharkfactions.menu.models.MenuBorderType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class MenuCreationUtil {
    public static ItemStack createItem(String name, Material material,ArrayList<String> lore){
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);

        return item;
    }

    public static ItemStack[] createBackGround(int Size, Material material, MenuBorderType border,String borderColor){
        ItemStack[] items = new ItemStack[Size];
        ItemStack backGroundItem = createItem(
                " ",
                material,
                new ArrayList<>());

        Arrays.fill(items, backGroundItem);

        ItemStack borderItem = createItem(
                " ",
                Material.getMaterial((borderColor+"_stained_glass_pane").toUpperCase()),
                new ArrayList<>());

        switch (border){
            case DOT -> {
                items[0] = borderItem;
                items[8] = borderItem;
                items[Size - 1] = borderItem;
                items[Size - 9] = borderItem;
            }
            case HALFCROSS -> {
                items = createCross(0,items,borderItem);
                items = createCross(8,items,borderItem);
                items = createCross(Size - 1,items,borderItem);
                items = createCross(Size - 9,items,borderItem);
            }
        }

        return items;
    }

    private static ItemStack[] createCross(int pos,ItemStack[] items,ItemStack item){
        ItemStack[] inst = items.clone();

        int point2 = pos + 1;
        int point3 = pos - 1;
        int point4 = pos + 9;
        int point5 = pos - 9;

        for (int point : new int[]{point2, point3, point4, point5}){
            if(point < 0 || point >= items.length) continue;

            inst[point] = item;
        }

        return inst;
    }
}
