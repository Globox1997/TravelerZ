package net.travelerz.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SkeletonEntity;
import net.nameplate.util.NameplateTracker;
import net.rpgdifficulty.api.MobStrengthener;
import net.travelerz.network.packet.TravelerPacket;

public class TravelerServerPacket {

    private static final boolean isRpgDifficultyLoaded = FabricLoader.getInstance().isModLoaded("rpgdifficulty");

    public static void init() {
        PayloadTypeRegistry.playS2C().register(TravelerPacket.PACKET_ID, TravelerPacket.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(TravelerPacket.PACKET_ID, TravelerPacket.PACKET_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(TravelerPacket.PACKET_ID, (payload, context) -> {
            payload.mobLevel();
            context.player().server.execute(() -> {
                SkeletonEntity skeletonEntity = EntityType.SKELETON.create(context.player().getWorld());
                skeletonEntity.refreshPositionAndAngles(context.player().getX(), context.player().getY(), context.player().getZ(), 0.0f, 0.0f);
                if (isRpgDifficultyLoaded) {
                    MobStrengthener.changeAttributes(skeletonEntity, context.player().getWorld());
                }
                ServerPlayNetworking.send(context.player(), new TravelerPacket(NameplateTracker.getMobLevel(skeletonEntity)));
                skeletonEntity.discard();
            });
        });
    }

}
