package net.travelerz;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.travelerz.network.TravelerzClientPacket;
import net.travelerz.util.BiomeTitleHandler;
import net.travelerz.util.TitleCompat;

@Environment(EnvType.CLIENT)
public class TravelerzClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        TravelerzClientPacket.init();
        BiomeTitleHandler.init();
        TitleCompat.init();
    }

}
