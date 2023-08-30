package net.travelerz.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "travelerz")
@Config.Gui.Background("minecraft:textures/block/stone.png")
public class TravelerzConfig implements ConfigData {

    public float titleSize = 1.0f;
    public int titleX = 0;
    public int titleY = 0;
}
