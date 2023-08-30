package net.travelerz.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;

@Mixin(InGameHud.class)
public interface InGameHudAccess {

    @Accessor("title")
    @Nullable
    Text getTitle();
}
