package net.travelerz.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.nameplate.util.NameplateTracker;
import net.rpgdifficulty.api.MobStrengthener;

public class TravelerServerPacket {

    public static final Identifier TRAVELER_CS_PACKET = new Identifier("travelerz", "title_cs_compat");
    public static final Identifier TRAVELER_SC_PACKET = new Identifier("travelerz", "title_sc_compat");

    private static final boolean isRpgDifficultyLoaded = FabricLoader.getInstance().isModLoaded("rpgdifficulty");

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(TRAVELER_CS_PACKET, (server, player, handler, buffer, sender) -> {
            server.execute(() -> {
                SkeletonEntity skeletonEntity = EntityType.SKELETON.create(player.getWorld());
                skeletonEntity.refreshPositionAndAngles(player.getX(), player.getY(), player.getZ(), 0.0f, 0.0f);
                if (isRpgDifficultyLoaded) {
                    MobStrengthener.changeAttributes(skeletonEntity, player.getWorld());
                }
                writeS2TravelerPacket(player, NameplateTracker.getMobLevel(skeletonEntity));
                skeletonEntity.discard();
            });
        });
    }

    public static void writeS2TravelerPacket(ServerPlayerEntity serverPlayerEntity, int level) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(level);
        CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(TRAVELER_SC_PACKET, buf);
        serverPlayerEntity.networkHandler.sendPacket(packet);
    }

}
