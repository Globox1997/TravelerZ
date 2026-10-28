package net.travelerz;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.travelerz.config.TravelerzConfig;
import net.travelerz.network.TravelerServerPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TravelerzMain implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("travelerz");

    public static TravelerzConfig CONFIG = new TravelerzConfig();

    @Override
    public void onInitialize() {
        AutoConfig.register(TravelerzConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(TravelerzConfig.class).getConfig();

        TravelerServerPacket.init();
    }

}
