package net.travelerz.network;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.nameplate.util.NameplateTracker;
import net.rpgdifficulty.api.MobStrengthener;
import net.travelerz.TravelerzMain;
import net.travelerz.network.packet.TravelerPacket;

public class TravelerServerPacket {

    private static final int REQUEST_COOLDOWN_TICKS = 10;
    private static final Map<UUID, Integer> lastRequestTicks = new ConcurrentHashMap<>();
    private static boolean rpgDifficultyCompat = FabricLoader.getInstance().isModLoaded("rpgdifficulty");

    public static void init() {
        PayloadTypeRegistry.playS2C().register(TravelerPacket.PACKET_ID, TravelerPacket.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(TravelerPacket.PACKET_ID, TravelerPacket.PACKET_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(TravelerPacket.PACKET_ID, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            int serverTicks = context.server().getTicks();
            Integer lastTicks = lastRequestTicks.put(player.getUuid(), serverTicks);
            int mobLevel = lastTicks != null && serverTicks - lastTicks < REQUEST_COOLDOWN_TICKS ? -1 : getMobLevel(player);
            ServerPlayNetworking.send(player, new TravelerPacket(mobLevel));
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> lastRequestTicks.remove(handler.getPlayer().getUuid()));
    }

    private static int getMobLevel(ServerPlayerEntity player) {
        SkeletonEntity skeletonEntity = EntityType.SKELETON.create(player.getServerWorld());
        if (skeletonEntity == null) {
            return -1;
        }
        skeletonEntity.refreshPositionAndAngles(player.getX(), player.getY(), player.getZ(), 0.0f, 0.0f);
        if (rpgDifficultyCompat) {
            try {
                MobStrengthener.changeAttributes(skeletonEntity, player.getServerWorld(), null, false);
            } catch (LinkageError error) {
                rpgDifficultyCompat = false;
                TravelerzMain.LOGGER.warn("Disabled RpgDifficulty mob levels, incompatible RpgDifficulty version", error);
            }
        }
        int mobLevel = NameplateTracker.getMobLevel(skeletonEntity);
        skeletonEntity.discard();
        return mobLevel;
    }

}
