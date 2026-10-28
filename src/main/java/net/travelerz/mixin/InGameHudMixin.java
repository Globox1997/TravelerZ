package net.travelerz.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import net.travelerz.TravelerzMain;
import net.travelerz.util.BiomeTitleHandler;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Shadow
    @Nullable
    private Text title;

    @ModifyConstant(method = "renderTitleAndSubtitle", constant = @Constant(floatValue = 4.0f))
    private float renderModifyTitleMixin(float original) {
        return BiomeTitleHandler.isTravelerzTitle(this.title) ? original * TravelerzMain.CONFIG.titleSize : original;
    }

    @ModifyConstant(method = "renderTitleAndSubtitle", constant = @Constant(floatValue = 2.0f))
    private float renderModifySubtitleMixin(float original) {
        return BiomeTitleHandler.isTravelerzTitle(this.title) ? original * TravelerzMain.CONFIG.titleSize : original;
    }

    @ModifyConstant(method = "renderTitleAndSubtitle", constant = @Constant(intValue = -10))
    private int renderModifyTitlePosYMixin(int original) {
        return BiomeTitleHandler.isTravelerzTitle(this.title) ? original + TravelerzMain.CONFIG.titleY : original;
    }

    @ModifyConstant(method = "renderTitleAndSubtitle", constant = @Constant(intValue = 5))
    private int renderModifySubtitlePosYMixin(int original) {
        return BiomeTitleHandler.isTravelerzTitle(this.title) ? original + TravelerzMain.CONFIG.titleY * 2 : original;
    }

    @ModifyArg(method = "renderTitleAndSubtitle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithBackground(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIII)I", ordinal = 0), index = 2)
    private int renderModifyTitlePosXMixin(int original) {
        return BiomeTitleHandler.isTravelerzTitle(this.title) ? original + TravelerzMain.CONFIG.titleX : original;
    }

    @ModifyArg(method = "renderTitleAndSubtitle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithBackground(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIII)I", ordinal = 1), index = 2)
    private int renderModifySubtitlePosXMixin(int original) {
        return BiomeTitleHandler.isTravelerzTitle(this.title) ? original + TravelerzMain.CONFIG.titleX * 2 : original;
    }
}
