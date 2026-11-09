package eu.codexe.visualspack.client.mixin;

import org.spongepowered.asm.mixin.Mixin;

/**
 * Placeholder hook kept for future head-render tweaks. The hat itself is
 * drawn by ChinaHatRenderer, so this mixin injects nothing yet.
 */
@Mixin(net.minecraft.client.network.AbstractClientPlayerEntity.class)
public abstract class PlayerHeadMixin {
}