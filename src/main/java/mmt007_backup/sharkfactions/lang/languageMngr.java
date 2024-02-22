package mmt007_backup.sharkfactions.lang;

import mmt007_backup.sharkfactions.SharkFMain;
import mmt007_backup.sharkfactions.lang.langs.en;
import mmt007_backup.sharkfactions.lang.langs.ptBR;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class languageMngr {
    private static final Plugin main = SharkFMain.getPlugin();
    private static final Map<String, Map<String,String>> messages = new HashMap<>();
    private static final ArrayList<language> languages = new ArrayList<>();

    public static String getMessage(String message){
        String lang = main.getConfig().getString("plugin-lang");
        String Message = messages.get(lang).get(message);
        return Message == null ? "§4\"{0}\" Message Does Not Exist".replace("{0}",message) : Message;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void loadMessages(){
        Bukkit.getLogger().info("[[ SFactions ]] Carregando Idiomas...");
        loadLanguagesNames();

        File langFolder = new File(main.getDataFolder() + "/langs");
        if(!langFolder.exists()){
            langFolder.mkdir();
        }

        for(language lang : languages) {
            String name = lang.getName();
            File langFile = new File(langFolder, name + ".yml");

            InputStream in = main.getResource("lang/" + name + ".yml");

            try {
                if (!langFile.exists()) {
                    Files.copy(in, langFile.toPath());
                }

            } catch (NullPointerException | IOException e) {
                Bukkit.getLogger().warning("[[ SFactions ]] Não Foi Possivel Criar Arquivo ["+name+"]");
                e.printStackTrace();
                continue;
            }

            Bukkit.getLogger().info("[[ SFactions ]] ["+name+"] Carregado");
        }

        if (langFolder.listFiles().length == 0) {
            Bukkit.getLogger().warning("[[ SFactions ]] Lista De Idiomas Vazia");
            return;
        }

        for (File file : langFolder.listFiles()) {
            Map<String,String> localmessages = new HashMap<>();

            FileConfiguration lang = YamlConfiguration.loadConfiguration(file);

            for(String messageName : lang.getKeys(false)){
                String message =
                        ChatColor.translateAlternateColorCodes('&',lang.getString(messageName));
                localmessages.put(messageName,message);
            }

            String langName = file.getName().replace(".yml","");

            messages.put(langName,localmessages);
        }
        Bukkit.getLogger().info("[[ SFactions ]] Idiomas Carregado!");
    }

    private static void loadLanguagesNames(){
        languages.add(new ptBR());
        languages.add(new en());
    }
}
