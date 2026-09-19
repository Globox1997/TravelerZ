package net.travelerz.util;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.rpgdifficulty.zone.ClientZoneEvents;

public class TitleCompat {

    public static void init() {
        if (FabricLoader.getInstance().isModLoaded("rpgdifficulty")) {
            ClientZoneEvents.ENTER.register((zoneEntry -> {
                if (zoneEntry != null && !zoneEntry.getDisplayName().equals("Zone")) {
                    MinecraftClient.getInstance().inGameHud.setTitle(Text.literal(zoneEntry.getDisplayName()));
                    MinecraftClient.getInstance().inGameHud.setSubtitle(Text.literal(""));
                }
            }));
        }
    }
}
