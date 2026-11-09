package eu.codexe.visualspack.client;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.joml.Matrix4f;

/**
 * Draws a conical straw hat above the local player's head when enabled.
 * Rendered as a simple triangle fan in world space, ignoring textures.
 */
public final class ChinaHatRenderer {

    private static final int SEGMENTS = 24;
    private static final float RADIUS = 0.55f;
    private static final float HEIGHT = 0.35f;
    private static final float BASE_Y = 0.42f;

    private ChinaHatRenderer() {}

    public static void register() {
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            if (!VisualsPackClient.chinaHatEnabled) return;
            MinecraftClient mc = MinecraftClient.getInstance();
            PlayerEntity player = mc.player;
            if (player == null || mc.options.getPerspective().isFirstPerson()) return;

            MatrixStack matrices = context.matrixStack();
            VertexConsumerProvider consumers = context.consumers();
            if (matrices == null || consumers == null) return;

            Vec camPos = context.camera().getPos().toVec();
            double px = player.getX() - camPos.x;
            double py = player.getY() + player.getHeight() - camPos.y;
            double pz = player.getZ() - camPos.z;

            matrices.push();
            matrices.translate(px, py, pz);
            matrices.multiply(new org.joml.Quaternionf().rotationY((float) Math.toRadians(-player.getYaw())));

            Matrix4f mat = matrices.peek().getPositionMatrix();
            VertexConsumer vc = consumers.getBuffer(net.minecraft.client.render.RenderLayer.getDebugQuads());

            float apexY = BASE_Y + HEIGHT;
            for (int i = 0; i < SEGMENTS; i++) {
                double a0 = (Math.PI * 2 * i) / SEGMENTS;
                double a1 = (Math.PI * 2 * (i + 1)) / SEGMENTS;
                float x0 = (float) Math.cos(a0) * RADIUS, z0 = (float) Math.sin(a0) * RADIUS;
                float x1 = (float) Math.cos(a1) * RADIUS, z1 = (float) Math.sin(a1) * RADIUS;
                vc.vertex(mat, 0f, apexY, 0f).color(230, 205, 120, 255).next();
                vc.vertex(mat, x0, BASE_Y, z0).color(200, 170, 90, 255).next();
                vc.vertex(mat, x1, BASE_Y, z1).color(200, 170, 90, 255).next();
                vc.vertex(mat, x1, BASE_Y, z1).color(200, 170, 90, 255).next();
            }
            matrices.pop();
        });
    }

    private static final class Vec {
        final double x, y, z;
        Vec(double x, double y, double z) { this.x = x; this.y = y; this.z = z; }
    }
}