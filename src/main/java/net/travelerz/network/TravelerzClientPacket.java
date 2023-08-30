package net.travelerz.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.travelerz.mixin.InGameHudAccess;

@Environment(EnvType.CLIENT)
public class TravelerzClientPacket {
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(TravelerServerPacket.TRAVELER_SC_PACKET, (client, handler, buffer, responseSender) -> {
            int mobLevel = buffer.readInt();
            client.execute(() -> {
                if (((InGameHudAccess) client.inGameHud).getTitle() == null) {
                    client.inGameHud.setTitle(Text.translatable("hud.travelerz.title", getBiomeName(client)));
                    client.inGameHud.setSubtitle(Text.translatable("hud.travelerz.subtitle", mobLevel));
                }
            });
        });
    }

    public static void writeC2STravelerPacket() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        CustomPayloadC2SPacket packet = new CustomPayloadC2SPacket(TravelerServerPacket.TRAVELER_CS_PACKET, buf);
        MinecraftClient.getInstance().getNetworkHandler().sendPacket(packet);
    }

    private static Text getBiomeName(MinecraftClient client) {
        Registry<Biome> registry = client.player.getWorld().getRegistryManager().get(RegistryKeys.BIOME);
        Identifier biomeIdentifier = registry.getId(client.player.getWorld().getBiome(client.player.getBlockPos()).value());
        return Text.translatable("biome." + biomeIdentifier.getNamespace() + "." + biomeIdentifier.getPath());
    }

}
