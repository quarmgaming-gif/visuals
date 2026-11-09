package eu.codexe.visualspack.client.mixin;

import eu.codexe.visualspack.client.FreeCam;
import eu.codexe.visualspack.client.VisualsPackClient;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Inject(method = "getPos", at = @At("HEAD"), cancellable = true, require = 0)
    private void visualspack$freeCamPos(CallbackInfoReturnable<net.minecraft.util.math.Vec3d> cir) {
        if (VisualsPackClient.freeCamEnabled && FreeCam.isActive()) {
            cir.setReturnValue(FreeCam.getPos());
        }
    }
}