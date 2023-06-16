package Ieonerd04.ToExistingStacks.config;

import Ieonerd04.ToExistingStacks.ToExistingStacks;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.SimpleOption;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

// Handles config, including storage, for this mod
// I figured some of this code by looking at the implementation in Mod Menu
// Credit to TerraformersMC, though I didn't use their code verbatim
public class ToExistingStacksConfig {
    public static SimpleOption<Boolean> ignoreHotBar;

    // An array with all the configs, specifically for rendering them in the settings screen
    public static ArrayList<SimpleOption<Boolean>> OPTIONS;

    // Hashmaps storing the config values
    private static HashMap<String, SimpleOption<Boolean>> BOOLEAN_MAP = new HashMap<>();

    private final static Type MAP_TYPE = new TypeToken<HashMap<String, Boolean>>() {}.getType();
    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File file;
    private static final Logger LOGGER = LogManager.getLogger();

    // Checks if the config file has the correct path
    private static void prepareConfigFile(){
        if(file != null) return;
        file = new File(FabricLoader.getInstance().getConfigDir().toFile(), ToExistingStacks.MOD_ID + ".json");
    }

    // Tries to load a config file; reverts to default values if not found
    public static void load(){
        OPTIONS = new ArrayList<>();

        // Default values in case loading fails
        ignoreHotBar = createBoolOption("shouldIgnoreHotbar","simplehud.config.hotbar", false);

        LOGGER.info("Loading ToExistingStacks configuration file");
        prepareConfigFile();

        try{
            if(!file.exists()) {
                save();
            }
            FileReader reader = new FileReader(file);
            HashMap<String, Boolean> map = GSON.fromJson(reader, MAP_TYPE);
            for(String key : map.keySet()){
                BOOLEAN_MAP.get(key).setValue(map.get(key));
            }

        } catch (FileNotFoundException | JsonSyntaxException e) {
            LOGGER.error("Config file failed to load; Reverting to default values");
            e.printStackTrace();
        }
    }

    // Tries to save the config file
    public static void save(){
        prepareConfigFile();

        String json = GSON.toJson(getValues(BOOLEAN_MAP), MAP_TYPE);

        LOGGER.info("Saving config file");

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(json);
        } catch (IOException e) {
            LOGGER.error("Failed to save config file");
            e.printStackTrace();
        }
    }

    //Maps a hashmap of SimpleOptions to a hashmap of values
    private static <T> HashMap<String, T> getValues(HashMap<String, SimpleOption<T>> optionMap){
        HashMap<String, T> valMap = new HashMap<>();
        for(String key : optionMap.keySet()){
            valMap.put(key, optionMap.get(key).getValue());
        }

        return valMap;
    }

    public static boolean getBoolConfigValue(String key){
        return BOOLEAN_MAP.get(key).getValue();
    }

    private static SimpleOption<Boolean> createBoolOption(String hashKey, String translationKey, boolean defaultVal){
        SimpleOption<Boolean> option = SimpleOption.ofBoolean(translationKey, defaultVal);

        BOOLEAN_MAP.put(hashKey, option);
        OPTIONS.add(option);
        return option;
    }
}
