package net.travelerz.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.travelerz.mixin.InGameHudAccess;
import net.travelerz.network.packet.TravelerPacket;

@Environment(EnvType.CLIENT)
public class TravelerzClientPacket {
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(TravelerPacket.PACKET_ID, (payload, context) -> {
            int mobLevel = payload.mobLevel();
            context.client().execute(() -> {
                MinecraftClient client = context.client();
                if (((InGameHudAccess) client.inGameHud).getTitle() == null) {
                    client.inGameHud.setTitle(Text.translatable("hud.travelerz.title", getBiomeName(client)));
                    client.inGameHud.setSubtitle(Text.translatable("hud.travelerz.subtitle", mobLevel));
                }
            });
        });
    }

    private static Text getBiomeName(MinecraftClient client) {
        Registry<Biome> registry = client.player.getWorld().getRegistryManager().get(RegistryKeys.BIOME);
        Identifier biomeIdentifier = registry.getId(client.player.getWorld().getBiome(client.player.getBlockPos()).value());
        return Text.translatable("biome." + biomeIdentifier.getNamespace() + "." + biomeIdentifier.getPath());
    }

}
