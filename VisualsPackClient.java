package eu.codexe.visualspack.client;

import eu.codexe.visualspack.VisualsPack;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class VisualsPackClient implements ClientModInitializer {

    public static boolean chinaHatEnabled = false;
    public static boolean freeCamEnabled = false;
    public static boolean elytraAlertEnabled = true;

    private static KeyBinding toggleHat;
    private static KeyBinding toggleFreeCam;
    private static KeyBinding toggleElytraAlert;

    @Override
    public void onInitializeClient() {
        toggleHat = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.visualspack.hat", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_H, "category.visualspack"));
        toggleFreeCam = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.visualspack.freecam", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F6, "category.visualspack"));
        toggleElytraAlert = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.visualspack.elytra", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "category.visualspack"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleHat.wasPressed()) chinaHatEnabled = !chinaHatEnabled;
            while (toggleFreeCam.wasPressed()) {
                freeCamEnabled = !freeCamEnabled;
                FreeCam.onToggle(client, freeCamEnabled);
            }
            while (toggleElytraAlert.wasPressed()) elytraAlertEnabled = !elytraAlertEnabled;
            if (freeCamEnabled) FreeCam.tick(client);
        });

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            ElytraAlert.render(drawContext);
        });

        ChinaHatRenderer.register();
    }
}