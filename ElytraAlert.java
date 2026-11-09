package eu.codexe.visualspack.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

/**
 * Shows a warning on the HUD when worn elytra durability drops below 15%.
 */
public final class ElytraAlert {

    private static final int THRESHOLD_PERCENT = 15;

    private ElytraAlert() {}

    public static void render(DrawContext ctx) {
        if (!VisualsPackClient.elytraAlertEnabled) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.options.hudHidden) return;

        ItemStack chest = mc.player.getEquippedStack(EquipmentSlot.CHEST);
        if (chest.getItem() != Items.ELYTRA) return;

        int max = chest.getMaxDamage();
        if (max <= 0) return;
        int remaining = max - chest.getDamage();
        int percent = remaining * 100 / max;
        if (percent > THRESHOLD_PERCENT) return;

        int w = mc.getWindow().getScaledWidth();
        int h = mc.getWindow().getScaledHeight();
        Text msg = Text.literal("Elytra low: " + percent + "%").formatted(net.minecraft.util.Formatting.RED);
        int tw = mc.textRenderer.getWidth(msg);
        boolean flash = (System.currentTimeMillis() / 300) % 2 == 0;
        if (flash) {
            ctx.drawTextWithShadow(mc.textRenderer, msg, (w - tw) / 2, h / 3, 0xFFFFFF);
        }
    }
}