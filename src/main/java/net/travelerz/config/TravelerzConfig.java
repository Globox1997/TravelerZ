package net.travelerz.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.ArrayList;

@Config(name = "travelerz")
@Config.Gui.Background("minecraft:textures/block/stone.png")
public class TravelerzConfig implements ConfigData {

    public float titleSize = 1.0f;
    public int titleX = 0;
    public int titleY = 0;
    @Comment("Entry example: minecraft:plains")
    public ArrayList<String> excludedBiomes = new ArrayList<>();

}
