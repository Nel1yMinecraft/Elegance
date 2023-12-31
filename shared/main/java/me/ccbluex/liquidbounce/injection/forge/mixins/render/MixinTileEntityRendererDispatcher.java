/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.injection.forge.mixins.render;

import me.ccbluex.liquidbounce.LiquidBounce;
import me.ccbluex.liquidbounce.features.module.modules.render.XRay;
import me.ccbluex.liquidbounce.injection.backend.BlockImplKt;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityRendererDispatcher.class)
public class MixinTileEntityRendererDispatcher {

    @Inject(method = "render(Lnet/minecraft/tileentity/TileEntity;FI)V", at = @At("HEAD"), cancellable = true)
    private void renderTileEntity(TileEntity tileentityIn, float partialTicks, int destroyStage, final CallbackInfo callbackInfo) {
        final XRay xray = (XRay) LiquidBounce.moduleManager.getModule(XRay.class);

        if (xray.getState() && !xray.getXrayBlocks().contains(BlockImplKt.wrap(tileentityIn.getBlockType())))
            callbackInfo.cancel();
    }
}
