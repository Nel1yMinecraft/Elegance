/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.injection.forge.mixins.entity;

import me.ccbluex.liquidbounce.LiquidBounce;
import me.ccbluex.liquidbounce.cape.CapeAPI;
import me.ccbluex.liquidbounce.cape.CapeInfo;
import me.ccbluex.liquidbounce.features.module.modules.render.NoFOV;
import me.ccbluex.liquidbounce.injection.backend.ResourceLocationImplKt;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(AbstractClientPlayer.class)
@SideOnly(Side.CLIENT)
public abstract class MixinAbstractClientPlayer extends MixinEntityPlayer {

    private CapeInfo capeInfo;

    @Inject(method = "getLocationCape", at = @At("HEAD"), cancellable = true)
    private void getCape(CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        if (!CapeAPI.INSTANCE.hasCapeService())
            return;

        if (capeInfo == null)
            capeInfo = CapeAPI.INSTANCE.loadCape(getUniqueID());

        if (capeInfo != null && capeInfo.isCapeAvailable())
            callbackInfoReturnable.setReturnValue(ResourceLocationImplKt.unwrap(capeInfo.getResourceLocation()));
    }

    @Inject(method = "getFovModifier", at = @At("HEAD"), cancellable = true)
    private void getFovModifier(CallbackInfoReturnable<Float> callbackInfoReturnable) {
        final NoFOV fovModule = (NoFOV) LiquidBounce.moduleManager.getModule(NoFOV.class);

        if (Objects.requireNonNull(fovModule).getState()) {
            float newFOV = fovModule.getFovValue().get();

            if (!this.isHandActive()) {
                callbackInfoReturnable.setReturnValue(newFOV);
                return;
            }

            if (this.getActiveItemStack().getItem() != Items.BOW) {
                callbackInfoReturnable.setReturnValue(newFOV);
                return;
            }

            int i = this.getItemInUseCount();
            float f1 = (float) i / 20.0f;
            f1 = f1 > 1.0f ? 1.0f : f1 * f1;
            newFOV *= 1.0f - f1 * 0.15f;
            callbackInfoReturnable.setReturnValue(newFOV);
        }
    }
}
