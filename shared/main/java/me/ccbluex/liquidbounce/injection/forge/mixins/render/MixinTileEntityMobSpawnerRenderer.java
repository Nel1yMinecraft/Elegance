package me.ccbluex.liquidbounce.injection.forge.mixins.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.renderer.tileentity.TileEntityMobSpawnerRenderer.class)
public class MixinTileEntityMobSpawnerRenderer {

    @Inject(method = "renderMob", cancellable = true, at = @At("HEAD"))
    private static void injectPaintingSpawnerFix(MobSpawnerBaseLogic mobSpawnerLogic, double posX, double posY, double posZ, float partialTicks, CallbackInfo ci) {
        Entity entity = mobSpawnerLogic.getCachedEntity();

        if (entity == null || entity instanceof EntityPainting)
            ci.cancel();
    }

}
