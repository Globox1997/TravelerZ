package net.travelerz.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.travelerz.network.packet.TravelerPacket;
import net.travelerz.util.BiomeTitleHandler;

@Environment(EnvType.CLIENT)
public class TravelerzClientPacket {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(TravelerPacket.PACKET_ID, (payload, context) -> BiomeTitleHandler.onLevelReceived(payload.mobLevel()));
    }

}
