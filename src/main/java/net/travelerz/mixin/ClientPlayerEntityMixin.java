package net.travelerz.mixin;

import com.mojang.authlib.GameProfile;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.biome.Biome;
import net.travelerz.network.TravelerzClientPacket;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Nullable
    private Biome travelerBiome;

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickMixin(CallbackInfo info) {
        Biome biome = this.getWorld().getBiomeAccess().getBiome(this.getBlockPos()).value();
        if (this.travelerBiome != biome) {
            TravelerzClientPacket.writeC2STravelerPacket();
            this.travelerBiome = biome;
        }

    }

}
