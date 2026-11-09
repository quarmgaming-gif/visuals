package eu.codexe.visualspack.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.util.math.Vec3d;

/**
 * Free camera: detaches the view from the player and lets the camera
 * move independently. The player body is not moved (no server packets).
 */
public final class FreeCam {

    private static Vec3d pos = Vec3d.ZERO;
    private static float yaw, pitch;
    private static Perspective previous;
    private static boolean active;

    private FreeCam() {}

    public static void onToggle(MinecraftClient client, boolean enable) {
        if (client.player == null) return;
        if (enable) {
            previous = client.options.getPerspective();
            pos = client.player.getEyePos();
            yaw = client.player.getYaw();
            pitch = client.player.getPitch();
            client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
            active = true;
        } else {
            active = false;
            if (previous != null) client.options.setPerspective(previous);
        }
    }

    public static void tick(MinecraftClient client) {
        if (!active || client.player == null) return;
        float speed = client.player.isSprinting() ? 0.8f : 0.4f;
        double dx = 0, dy = 0, dz = 0;
        float rad = (float) Math.toRadians(yaw);
        if (client.options.forwardKey.isPressed()) { dx -= Math.sin(rad); dz += Math.cos(rad); }
        if (client.options.backKey.isPressed())    { dx += Math.sin(rad); dz -= Math.cos(rad); }
        if (client.options.leftKey.isPressed())    { dx -= Math.cos(rad); dz -= Math.sin(rad); }
        if (client.options.rightKey.isPressed())   { dx += Math.cos(rad); dz += Math.sin(rad); }
        if (client.options.jumpKey.isPressed())    dy += 1;
        if (client.options.sneakKey.isPressed())   dy -= 1;
        pos = pos.add(dx * speed, dy * speed, dz * speed);
        client.player.setPosition(client.player.getX(), client.player.getY(), client.player.getZ());
    }

    public static Vec3d getPos() { return pos; }
    public static boolean isActive() { return active; }
}