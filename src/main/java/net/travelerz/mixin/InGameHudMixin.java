package net.travelerz.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.InGameHud;
import net.travelerz.TravelerzMain;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class InGameHudMixin {

    @ModifyConstant(method = "render", constant = @Constant(floatValue = 4.0f))
    private float renderModifyTitleMixin(float original) {
        return original * TravelerzMain.CONFIG.titleSize;
    }

    @ModifyConstant(method = "render", constant = @Constant(floatValue = 2.0f))
    private float renderModifySubtitleMixin(float original) {
        return original * TravelerzMain.CONFIG.titleSize;
    }

    @ModifyConstant(method = "render", constant = @Constant(intValue = -10))
    private int renderModifyTitlePosYMixin(int original) {
        return original + TravelerzMain.CONFIG.titleY;
    }

    @ModifyConstant(method = "render", constant = @Constant(intValue = 5))
    private int renderModifySubtitlePosYMixin(int original) {
        return original + (int) (TravelerzMain.CONFIG.titleY * 2.0f);
    }

    @ModifyVariable(method = "render", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/font/TextRenderer;getWidth(Lnet/minecraft/text/StringVisitable;)I", ordinal = 1), ordinal = 2)
    private int renderModifyTitlePosXMixin(int original) {
        return original + TravelerzMain.CONFIG.titleX;
    }

    @ModifyVariable(method = "render", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/font/TextRenderer;getWidth(Lnet/minecraft/text/StringVisitable;)I", ordinal = 2), ordinal = 3)
    private int renderModifySubtitlePosXMixin(int original) {
        return original + (int) (TravelerzMain.CONFIG.titleX * 2.0f);
    }
}
