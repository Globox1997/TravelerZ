package net.travelerz.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.rpgdifficulty.zone.ClientZoneEvents;
import net.travelerz.TravelerzMain;

@Environment(EnvType.CLIENT)
public class TitleCompat {

    private static boolean zoneCompat = true;

    public static void init() {
        if (FabricLoader.getInstance().isModLoaded("rpgdifficulty")) {
            try {
                ClientZoneEvents.ENTER.register((zoneEntry -> {
                    if (!zoneCompat) {
                        return;
                    }
                    try {
                        if (zoneEntry != null && !zoneEntry.getDisplayName().equals("Zone")) {
                            BiomeTitleHandler.showZoneTitle(Text.literal(zoneEntry.getDisplayName()));
                        }
                    } catch (LinkageError error) {
                        zoneCompat = false;
                        TravelerzMain.LOGGER.warn("Disabled RpgDifficulty zone titles, incompatible RpgDifficulty version", error);
                    }
                }));
            } catch (LinkageError error) {
                TravelerzMain.LOGGER.warn("Failed to register RpgDifficulty zone titles, incompatible RpgDifficulty version", error);
            }
        }
    }
}
