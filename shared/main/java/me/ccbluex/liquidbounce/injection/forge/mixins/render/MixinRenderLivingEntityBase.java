/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.injection.forge.mixins.render;

import me.ccbluex.liquidbounce.utils.OutlineUtils;
import me.ccbluex.liquidbounce.LiquidBounce;
import me.ccbluex.liquidbounce.features.module.modules.render.Chams;
import me.ccbluex.liquidbounce.features.module.modules.render.ESP;
import me.paimon.NameTags;
import me.ccbluex.liquidbounce.features.module.modules.render.TrueSight;
import me.ccbluex.liquidbounce.injection.backend.EntityLivingBaseImplKt;
import me.ccbluex.liquidbounce.utils.ClientUtils;
import me.ccbluex.liquidbounce.utils.EntityUtils;
import me.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(value = RenderLivingBase.class, priority = 1001)
@SideOnly(Side.CLIENT)
public abstract class MixinRenderLivingEntityBase extends MixinRender {

    @Shadow
    protected ModelBase mainModel;

    @Inject(method = "doRender*", at = @At("HEAD"))
    private <T extends EntityLivingBase> void injectChamsPre(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo callbackInfo) {
        final Chams chams = (Chams) LiquidBounce.moduleManager.getModule(Chams.class);

        if (chams.getState() && chams.getTargetsValue().get() && EntityUtils.isSelected(EntityLivingBaseImplKt.wrap(entity), false)) {
            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPolygonOffset(1.0F, -1000000F);
        }
    }

    @Inject(method = "doRender*", at = @At("RETURN"))
    private <T extends EntityLivingBase> void injectChamsPost(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo callbackInfo) {
        final Chams chams = (Chams) LiquidBounce.moduleManager.getModule(Chams.class);

        if (chams.getState() && chams.getTargetsValue().get() && EntityUtils.isSelected(EntityLivingBaseImplKt.wrap(entity), false)) {
            GL11.glPolygonOffset(1.0F, 1000000F);
            GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        }
    }

    @Inject(method = "canRenderName*", at = @At("HEAD"), cancellable = true)
    private <T extends EntityLivingBase> void canRenderName(T entity, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (!ESP.renderNameTags || (LiquidBounce.moduleManager.getModule(NameTags.class).getState() && EntityUtils.isSelected(EntityLivingBaseImplKt.wrap(entity), false)))
            callbackInfoReturnable.setReturnValue(false);
    }
}