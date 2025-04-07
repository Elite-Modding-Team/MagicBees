package magicbees.util;

import magicbees.api.module.IConfigRegistry;
import magicbees.api.module.IConfiguration;
import magicbees.bees.EnumHiveGen;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.lang3.text.WordUtils;

/**
 * Created by Elec332 on 18-6-2018
 */
public class ConfigWorldGen implements IConfiguration {

    private static String CATEGORY_WORLDGEN = "worldgen";

    public static boolean postGenRedstone = true;

    public static boolean postGenNetherQuartz = true;

    public static boolean postGenQuartz = true;

    public static boolean postGenObsidianSpikes = true;

    public static boolean postGenEndstone = true;

    @Override
    public void init(IConfigRegistry configHandler) {
        configHandler.registerCategoryComment(CATEGORY_WORLDGEN, "Worldgen settings, used to enable/disable certain worldgen features.");
    }

    @Override
    public void reload(Configuration config) {
        postGenRedstone = config.getBoolean("postGenRedstone", CATEGORY_WORLDGEN, postGenRedstone, getPostGenDesc("redstone", EnumHiveGen.DEEP, "Overworld"));
        postGenNetherQuartz = config.getBoolean("postGenNetherQuartz", CATEGORY_WORLDGEN, postGenNetherQuartz, getPostGenDesc("nether quartz", EnumHiveGen.INFERNAL, "Nether"));
        postGenQuartz = config.getBoolean("postGenQuartz", CATEGORY_WORLDGEN, postGenQuartz, getPostGenDesc("quartz", EnumHiveGen.INFERNAL_OVERWORLD, "Overworld"));
        postGenObsidianSpikes = config.getBoolean("postGenObsidianSpikes", CATEGORY_WORLDGEN, postGenObsidianSpikes, "Enables/Disables the generation of Obsidian spikes under Oblivion hives in the End");
        postGenEndstone = config.getBoolean("postGenEndstone", CATEGORY_WORLDGEN, postGenEndstone, getPostGenDesc("redstone", EnumHiveGen.OBLIVION_OVERWORLD, "Overworld"));
    }

    private static String getPostGenDesc(String resource, EnumHiveGen hive, String where) {
        return "Enables/Disables the generation of " + resource + " pockets around " + WordUtils.capitalize(hive.toString().split("_")[0].toLowerCase()) + " hives in the " + where + ".";
    }

}
