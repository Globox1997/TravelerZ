package net.travelerz.util;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.travelerz.TravelerzMain;
import net.travelerz.mixin.InGameHudAccess;
import net.travelerz.network.packet.TravelerPacket;

@Environment(EnvType.CLIENT)
public class BiomeTitleHandler {

    private static final Map<RegistryKey<Biome>, Integer> lastShownTicks = new HashMap<>();
    private static final Deque<RegistryKey<Biome>> awaitingLevel = new ArrayDeque<>();

    @Nullable
    private static RegistryKey<Biome> candidateBiome;
    @Nullable
    private static RegistryKey<Biome> currentBiome;
    @Nullable
    private static RegistryKey<Biome> pendingBiome;
    private static int pendingLevel = -1;
    @Nullable
    private static Text travelerzTitle;
    private static int stableTicks;
    private static int ticks;

    public static void init() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> reset());
    }

    public static void tick(World world, BlockPos pos) {
        ticks++;
        Optional<RegistryKey<Biome>> biome = world.getBiome(pos).getKey();
        if (biome.isPresent()) {
            updateBiome(biome.get());
        }
        InGameHud inGameHud = MinecraftClient.getInstance().inGameHud;
        if (pendingBiome != null && ((InGameHudAccess) inGameHud).getTitle() == null) {
            travelerzTitle = Text.translatable("hud.travelerz.title", getBiomeName(pendingBiome));
            inGameHud.setTitle(travelerzTitle);
            inGameHud.setSubtitle(pendingLevel >= 0 ? Text.translatable("hud.travelerz.subtitle", pendingLevel) : null);
            lastShownTicks.put(pendingBiome, ticks);
            pendingBiome = null;
        }
    }

    public static void onLevelReceived(int mobLevel) {
        RegistryKey<Biome> biome = awaitingLevel.poll();
        if (biome != null && biome.equals(currentBiome)) {
            queueTitle(biome, mobLevel);
        }
    }

    public static void showZoneTitle(Text title) {
        InGameHud inGameHud = MinecraftClient.getInstance().inGameHud;
        travelerzTitle = title;
        inGameHud.setTitle(title);
        inGameHud.setSubtitle(null);
    }

    public static boolean isTravelerzTitle(@Nullable Text title) {
        return title != null && title == travelerzTitle;
    }

    private static void updateBiome(RegistryKey<Biome> biome) {
        if (!biome.equals(candidateBiome)) {
            candidateBiome = biome;
            stableTicks = 0;
        } else if (stableTicks < TravelerzMain.CONFIG.biomeChangeDelay) {
            stableTicks++;
        }
        if (stableTicks < TravelerzMain.CONFIG.biomeChangeDelay || biome.equals(currentBiome)) {
            return;
        }
        currentBiome = biome;
        pendingBiome = null;
        if (TravelerzMain.CONFIG.excludedBiomes.contains(biome.getValue().toString()) || isOnCooldown(biome)) {
            return;
        }
        if (ClientPlayNetworking.canSend(TravelerPacket.PACKET_ID)) {
            awaitingLevel.add(biome);
            ClientPlayNetworking.send(new TravelerPacket(0));
        } else {
            queueTitle(biome, -1);
        }
    }

    private static boolean isOnCooldown(RegistryKey<Biome> biome) {
        Integer lastShown = lastShownTicks.get(biome);
        return lastShown != null && ticks - lastShown < TravelerzMain.CONFIG.titleCooldown * 20;
    }

    private static void queueTitle(RegistryKey<Biome> biome, int mobLevel) {
        pendingBiome = biome;
        pendingLevel = mobLevel;
    }

    private static Text getBiomeName(RegistryKey<Biome> biome) {
        Identifier identifier = biome.getValue();
        String path = identifier.getPath().substring(identifier.getPath().lastIndexOf('/') + 1);
        String fallback = Arrays.stream(path.split("_")).filter(word -> !word.isEmpty()).map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining(" "));
        return Text.translatableWithFallback(Util.createTranslationKey("biome", identifier), fallback);
    }

    private static void reset() {
        lastShownTicks.clear();
        awaitingLevel.clear();
        candidateBiome = null;
        currentBiome = null;
        pendingBiome = null;
        pendingLevel = -1;
        travelerzTitle = null;
        stableTicks = 0;
        ticks = 0;
    }
}
