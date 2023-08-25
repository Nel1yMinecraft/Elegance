/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.item;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.player.Animations;
import net.ccbluex.liquidbounce.features.module.modules.render.AntiBlind;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.*;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.client.renderer.GlStateManager.translate;

@Mixin(ItemRenderer.class)
@SideOnly(Side.CLIENT)
public abstract class MixinItemRenderer {

    @Shadow
    @Final
    private Minecraft mc;
    @Shadow
    private ItemStack itemStackOffHand;
    @Unique
    float delay = 0.0F;

    @Unique
    MSTimer rotateTimer = new MSTimer();
    @Shadow
    protected abstract void renderMapFirstPerson(float p_187463_1_, float p_187463_2_, float p_187463_3_);

    @Shadow
    protected abstract void transformFirstPerson(EnumHandSide hand, float swingProgress);

    @Shadow
    protected abstract void transformEatFirstPerson(float p_187454_1_, EnumHandSide hand, ItemStack stack);

    @Shadow
    protected abstract void renderArmFirstPerson(float p_187456_1_, float p_187456_2_, EnumHandSide p_187456_3_);

    @Shadow
    protected abstract void renderMapFirstPersonSide(float p_187465_1_, EnumHandSide hand, float p_187465_3_, ItemStack stack);

    @Shadow
    protected abstract void transformSideFirstPerson(EnumHandSide hand, float p_187459_2_);

    @Shadow
    public abstract void renderItemSide(EntityLivingBase entitylivingbaseIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform, boolean leftHanded);

    /**
     * @author 123
     * @reason 123
     */
    @Overwrite
    public void renderItemInFirstPerson(AbstractClientPlayer player, float p_187457_2_, float p_187457_3_, EnumHand hand, float partialTicks, ItemStack stack, float p_187457_7_) {
        float f = -0.4F * MathHelper.sin(MathHelper.sqrt(partialTicks) * (float) Math.PI);
        float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt(partialTicks) * ((float) Math.PI * 2F));
        float f8 = player.getSwingProgress(partialTicks);
        float f2 = -0.2F * MathHelper.sin(partialTicks * (float) Math.PI);
        EntityPlayerSP abstractclientplayer = Minecraft.getMinecraft().player;
        float f0 = abstractclientplayer.getSwingProgress(partialTicks);
        boolean flag = hand == EnumHand.MAIN_HAND;
        EnumHandSide enumhandside = flag ? player.getPrimaryHand() : player.getPrimaryHand().opposite();
        GlStateManager.pushMatrix();

        if (stack.isEmpty()) {
            if (flag && !player.isInvisible()) {
                renderArmFirstPerson(p_187457_7_, partialTicks, enumhandside);
            }
        } else if (stack.getItem() instanceof ItemMap) {
            if (flag && itemStackOffHand.isEmpty()) {
                renderMapFirstPerson(p_187457_3_, p_187457_7_, partialTicks);
            } else {
                renderMapFirstPersonSide(p_187457_7_, enumhandside, partialTicks, stack);
            }
        } else {
            if (!(stack.getItem() instanceof ItemShield)) {

                final KillAura killAura = (KillAura) LiquidBounce.moduleManager.getModule(KillAura.class);

                boolean flag1 = enumhandside == EnumHandSide.RIGHT;

                if (player.isHandActive() && player.getItemInUseCount() > 0 && player.getActiveHand() == hand) {
                    int j = flag1 ? 1 : -1;

                    EnumAction enumaction = killAura.getBlockingStatus() ? EnumAction.BLOCK : stack.getItemUseAction();

                    switch (enumaction) {
                        case NONE:
                            transformSideFirstPerson(enumhandside, 0F);
                            break;
                        case BLOCK:
                            transformSideFirstPersonBlock(enumhandside, p_187457_7_, partialTicks);
                            break;
                        case EAT:
                        case DRINK:
                            transformEatFirstPerson(p_187457_2_, enumhandside, stack);
                            transformSideFirstPerson(enumhandside, p_187457_7_);
                            if (Animations.INSTANCE.getRotateItems() && Animations.INSTANCE.getRotateItemsWhenEatingOrDrinkingValue().get())
                                rotateItemAnim();
                            break;
                        case BOW:
                            transformSideFirstPerson(enumhandside, p_187457_7_);
                            translate((float) j * -0.2785682F, 0.18344387F, 0.15731531F);
                            GlStateManager.rotate(-13.935F, 1.0F, 0.0F, 0.0F);
                            GlStateManager.rotate((float) j * 35.3F, 0.0F, 1.0F, 0.0F);
                            GlStateManager.rotate((float) j * -9.785F, 0.0F, 0.0F, 1.0F);
                            float f5 = (float) stack.getMaxItemUseDuration() - ((float) mc.player.getItemInUseCount() - p_187457_2_ + 1.0F);
                            float f6 = f5 / 20.0F;
                            f6 = (f6 * f6 + f6 * 2.0F) / 3.0F;

                            if (f6 > 1.0F) {
                                f6 = 1.0F;
                            }

                            if (f6 > 0.1F) {
                                float f7 = MathHelper.sin((f5 - 0.1F) * 1.3F);
                                float f3 = f6 - 0.1F;
                                float f4 = f7 * f3;
                                translate(f4 * 0.0F, f4 * 0.004F, f4 * 0.0F);
                            }

                            translate(f6 * 0.0F, f6 * 0.0F, f6 * 0.04F);
                            GlStateManager.scale(1.0F, 1.0F, 1.0F + f6 * 0.2F);
                            GlStateManager.rotate((float) j * 45.0F, 0.0F, -1.0F, 0.0F);
                            break;
                    }
                } else {
                    if ((mc.player.getHeldItemMainhand().getItem() instanceof ItemSword || !Animations.INSTANCE.getOnlySword())
                            && Animations.INSTANCE.getBlockValue().get()
                            && ((Animations.INSTANCE.getRightClickBlocking().get() && mc.gameSettings.keyBindUseItem.pressed)
                            || ((killAura.getTarget() != null && killAura.getBlockingStatus())))) {
                        translate(Animations.blockItemPosX(), Animations.blockItemPosY(), Animations.blockItemPosZ());
                        float breakDownAnimation = (Animations.INSTANCE.getBreakDownAnimation().get() ? p_187457_7_ : 0);
                        switch (Animations.INSTANCE.getModeValue().get().toLowerCase()) {
                            case "1.7": {
                                oldAnimation(enumhandside, breakDownAnimation, partialTicks);
                                if (Animations.INSTANCE.getRotateItems() && Animations.INSTANCE.getRotateItemWhenBlockingValue().get())
                                    rotateItemAnim();
                                break;
                            }
                            case "old": {
                                transformSideFirstPersonBlock(enumhandside, breakDownAnimation, partialTicks);
                                if (Animations.INSTANCE.getRotateItems() && Animations.INSTANCE.getRotateItemWhenBlockingValue().get())
                                    rotateItemAnim();
                                break;
                            }
                            case "push": {
                                Push(enumhandside, breakDownAnimation, partialTicks);
                                if (Animations.INSTANCE.getRotateItems() && Animations.INSTANCE.getRotateItemWhenBlockingValue().get())
                                    rotateItemAnim();
                                break;
                            }
                            case "windmill": {
                                WindMill(enumhandside, -0.2F + breakDownAnimation, partialTicks);
                                if (Animations.INSTANCE.getRotateItems() && Animations.INSTANCE.getRotateItemWhenBlockingValue().get())
                                    rotateItemAnim();
                                break;
                            }
                            case "flux": {
                                flux(enumhandside, breakDownAnimation, partialTicks);
                                if (Animations.INSTANCE.getRotateItems() && Animations.INSTANCE.getRotateItemWhenBlockingValue().get())
                                    rotateItemAnim();
                                break;
                            }
                            case "sigmaold": {
                                sigmaold(enumhandside, breakDownAnimation, partialTicks);
                                if (Animations.INSTANCE.getRotateItems() && Animations.INSTANCE.getRotateItemWhenBlockingValue().get())
                                    rotateItemAnim();
                                break;
                            }
                            case "zoom": {
                                Zoom(enumhandside, breakDownAnimation, partialTicks);
                                if (Animations.INSTANCE.getRotateItems() && Animations.INSTANCE.getRotateItemWhenBlockingValue().get())
                                    rotateItemAnim();
                                break;
                            }
                            case "jello": {
                                jello(enumhandside, partialTicks);
                                if (Animations.INSTANCE.getRotateItems() && Animations.INSTANCE.getRotateItemWhenBlockingValue().get())
                                    rotateItemAnim();
                                break;
                            }
                            case "exhibition": {
                                transformSideFirstPersonBlock(enumhandside, p_187457_7_, 0.0f);
                                float f4 = MathHelper.sin(MathHelper.sqrt(partialTicks) * 3.83F);
                                GlStateManager.rotate(-f4 * 0.0F, 0.0F, 0.0F, 0.0F);
                                GlStateManager.rotate(-f4 * 43.0F, 58.0F, 23.0F, 45.0F);
                                break;
                            }
                            case "rotate360": {
                                rotate360();
                                if (Animations.INSTANCE.getRotateItems() && Animations.INSTANCE.getRotateItemWhenBlockingValue().get())
                                    rotateItemAnim();

                                strange();
                                GlStateManager.rotate(delay, 1.0F, 0.0F, 2.0F);
                                if (rotateTimer.hasTimePassed(2)) {
                                    ++delay;
                                    delay = delay + Animations.INSTANCE.getRotate360speed().get();
                                    rotateTimer.reset();
                                }
                                if (delay > 360.0F) {
                                    delay = 0.0F;
                                }
                                if (Animations.INSTANCE.getRotateItems() && Animations.INSTANCE.getRotateItemWhenBlockingValue().get())
                                    rotateItemAnim();
                                break;
                            }
                            case "remix": {
                                genCustom(f);
                                if (Animations.INSTANCE.getRotateItems() && Animations.INSTANCE.getRotateItemWhenBlockingValue().get())
                                    rotateItemAnim();
                                strange();
                                float f4 = MathHelper.sin(MathHelper.sqrt(f0) * 3.83f);
                                translate(-0.5f, 0.2f, 0.2f);
                                GlStateManager.rotate(-f4 * 0.0f, 0.0f, 0.0f, 0.0f);
                                GlStateManager.rotate(-f4 * 43.0f, 58.0f, 23.0f, 45.0f);
                                if (Animations.INSTANCE.getRotateItems() && Animations.INSTANCE.getRotateItemWhenBlockingValue().get())
                                    rotateItemAnim();
                                break;
                            }
                            case "jello2": {
                                jello2();
                                strange();
                                int alpha = (int) Math.min(255, ((System.currentTimeMillis() % 255) > 255 / 2 ? (Math.abs(Math.abs(System.currentTimeMillis()) % 255 - 255)) : System.currentTimeMillis() % 255) * 2);
                                float f5 = (f8 > 0.5 ? 1 - f8 : f8);
                                translate(0.3f, -0.0f, 0.40f);
                                GlStateManager.rotate(0.0f, 0.0f, 0.0f, 1.0f);
                                translate(0, 0.5f, 0);

                                GlStateManager.rotate(90, 1.0f, 0.0f, -1.0f);
                                translate(0.6f, 0.5f, 0);
                                GlStateManager.rotate(-90, 1.0f, 0.0f, -1.0f);


                                GlStateManager.rotate(-10, 1.0f, 0.0f, -1.0f);
                                GlStateManager.rotate((-f5) * 10.0f, 10.0f, 10.0f, -9.0f);
                                GlStateManager.rotate(10.0f, -1.0f, 0.0f, 0.0f);

                                translate(0, 0, -0.5);
                                GlStateManager.rotate(mc.player.isSwingInProgress ? -alpha / 5f : 1, 1.0f, -0.0f, 1.0f);
                                translate(0, 0, 0.5);
                                if (Animations.INSTANCE.getRotateItems() && Animations.INSTANCE.getRotateItemWhenBlockingValue().get())
                                    rotateItemAnim();
                                break;
                            }

                            case "sigmanew": {
                                transformSideFirstPersonBlock(enumhandside, p_187457_7_, partialTicks);
                                GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                                float var12 = MathHelper.sin(MathHelper.sqrt(partialTicks) * 3.1415927F);
                                GlStateManager.rotate(var12 * -5.0F, 1.0F, 0.0F, 0.0F);
                                GlStateManager.rotate(var12 * 0.0F, 0.0F, 0.0F, 1.0F);
                                GlStateManager.rotate(var12 * 25.0F, 0.0F, 1.0F, 0.0F);
                                if (Animations.INSTANCE.getRotateItems() && Animations.INSTANCE.getRotateItemWhenBlockingValue().get())
                                    rotateItemAnim();
                                break;
                            }
                        }
                        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
                    } else {
                        translate(Animations.HeldItemPosX.get(), Animations.HeldItemPosY.get(), Animations.HeldItemPosZ.get());
                        int i = flag1 ? 1 : -1;
                        translate((float) i * f, f1, f2);
                        transformSideFirstPerson(enumhandside, p_187457_7_);
                        transformFirstPerson(enumhandside, partialTicks);
                        rotateItemAnim();
                        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
                    }
                }

                renderItemSide(player, stack, flag1 ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !flag1);
            }
        }

        GlStateManager.popMatrix();
    }

    /**
     * @author CCBlueX
     */


    @Inject(method = "renderFireInFirstPerson", at = @At("HEAD"), cancellable = true)
    private void renderFireInFirstPerson(final CallbackInfo callbackInfo) {
        final AntiBlind antiBlind = (AntiBlind) LiquidBounce.moduleManager.getModule(AntiBlind.class);

        if (antiBlind.getState() && antiBlind.getFireEffect().get()) callbackInfo.cancel();
    }
    @Unique
    private void rotateItemAnim() {
        if (Animations.INSTANCE.getRotateItems()) {
            if (Animations.INSTANCE.getTransformFirstPersonRotate().get().equalsIgnoreCase("RotateY")) {
                GlStateManager.rotate(delay, 0.0F, 1.0F, 0.0F);
            }
            if (Animations.INSTANCE.getTransformFirstPersonRotate().get().equalsIgnoreCase("RotateXY")) {
                GlStateManager.rotate(delay, 1.0F, 1.0F, 0.0F);
            }

            if (Animations.INSTANCE.getTransformFirstPersonRotate().get().equalsIgnoreCase("Custom")) {
                GlStateManager.rotate(delay, Animations.INSTANCE.getRotateX().get(), Animations.INSTANCE.getRotateY().get(), Animations.INSTANCE.getRotateZ().get());
            }

            if (rotateTimer.hasTimePassed(1)) {
                ++delay;
                delay = delay + Animations.INSTANCE.getSpeedRotate().get();
                rotateTimer.reset();
            }
            if (delay > 360.0F) {
                delay = 0.0F;
            }
        }
    }
    @Unique
    private void rotate360() {
        translate(0.56F, -0.52F, -0.71999997F);
        translate(0.0F, (float) 0.0 * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin((float) 0.95 * (float) 0.95 * (float) Math.PI);
        float var4 = MathHelper.sin(MathHelper.sqrt((float) 0.95) * (float) Math.PI);
        RyFRotate5(var3, var4);
    }

    @Unique
    private void jello2() {
        translate(0.56F, -0.52F, -0.71999997F);
        translate(0.0F, (float) 0 * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin((float) 0.0 * (float) 0.0 * (float) Math.PI);
        float var4 = MathHelper.sin(MathHelper.sqrt((float) 0.0) * (float) Math.PI);
        RyFRotate5(var3, var4);
    }

    @Unique
    private void strange() {
        translate(-0.5F, 0.2F, 0.0F);
        GlStateManager.rotate(30.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
    }

    @Unique
    private void genCustom(float p_178096_1_) {
        translate(0.56F, -0.52F, -0.71999997F);
        translate(0.0F, p_178096_1_ * -0.6F, 0.0F);
        GlStateManager.rotate(25F, 0.0F, 1.0F, 0.0F);
        RyFRotate3((float) 0.83);
        GlStateManager.scale(0.4F, 0.4F, 0.4F);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    @Unique
    private static void transformSideFirstPersonBlock(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        double f = Math.sin(swingProgress * swingProgress * Math.PI);
        double f1 = Math.sin(Math.sqrt(swingProgress) * Math.PI);
        RyFRotate(p_187459_1_, equippedProg, f, f1);
        GlStateManager.rotate((float) (f1 * -80.0F), 1, 0, 0);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    @Unique
    private static void Zoom(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        RyFRotate2(p_187459_1_, swingProgress);
        float var3 = MathHelper.sin(equippedProg * equippedProg * (float) Math.PI);
        float var4 = MathHelper.sin(MathHelper.sqrt(equippedProg) * (float) Math.PI);
        GlStateManager.rotate(var3 * -20.0F, 0.0F, 0.0F, 0.0F);
        GlStateManager.rotate(var4 * -20.0F, 0.0F, 0.0F, 0.0F);
        GlStateManager.rotate(var4 * -20.0F, 0.0F, 0.0F, 0.0F);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    @Unique
    private static void jello(EnumHandSide p_187459_1_, float swingProgress) {
        int side = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        translate(0.56f, -0.52f, -0.71999997f);
        GlStateManager.rotate(-102.25f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate((float) side * 13.365f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate((float) side * 78.05f, 0.0f, 0.0f, 1.0f);
        float var13 = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
        float var14 = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
        GlStateManager.rotate(var13 * -35.0f, 0.0f, 0.0f, 0.0f);
        GlStateManager.rotate(var14 * 0.0f, 0.0f, 0.0f, 0.0f);
        GlStateManager.rotate(var14 * 20.0f, 1.0f, 1.0f, 1.0f);
    }

    @Unique
    private static void sigmaold(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        RyFRotate2(p_187459_1_, equippedProg);
        RyFRotate3(swingProgress);
    }

    @Unique
    private static void WindMill(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        double f = Math.sin(swingProgress * swingProgress * Math.PI);
        double f1 = Math.sin(Math.sqrt(swingProgress) * Math.PI);
        RyFRotate(p_187459_1_, equippedProg, f, f1);
        GlStateManager.rotate((float) (f1 * -50.0F), 1, 0, 0);

    }

    @Unique
    private static void Push(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        double f = Math.sin(swingProgress * swingProgress * Math.PI);
        double f1 = Math.sin(Math.sqrt(swingProgress) * Math.PI);
        RyFRotate4(p_187459_1_, equippedProg);
        GlStateManager.rotate((float) (f * -10.0F), 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate((float) (f1 * -10.0F), 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate((float) (f1 * -10.0F), 1.0F, 1.0F, 1.0F);
    }

    @Unique
    private static void flux(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        double f = Math.sin(swingProgress * swingProgress * Math.PI);
        double f1 = Math.sin(Math.sqrt(swingProgress) * Math.PI);
        RyFRotate(p_187459_1_, equippedProg, f, f1);
        GlStateManager.rotate((float) (f1 * -30.0F), 1, 0, 0);
    }

    @Unique
    private static void RyFRotate(EnumHandSide p_187459_1_, float equippedProg, double f, double f1) {
        RyFRotate4(p_187459_1_, equippedProg);
        GlStateManager.rotate((float) (f * -20.0F), 0, 1, 0);
        GlStateManager.rotate((float) (f1 * -20.0F), 0, 0, 1);
    }

    @Unique
    private static void RyFRotate2(EnumHandSide p_187459_1_, float swingProgress) {
        int side = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        translate(0.56F, -0.52F, -0.71999997F);
        translate(0.0F, swingProgress * -0.6F, 0.0F);
        GlStateManager.rotate(-102.25F, 1, 0, 0);
        GlStateManager.rotate(side * 13.365F, 0, 1, 0);
        GlStateManager.rotate(side * 78.050003F, 0, 0, 1);
    }

    @Unique
    private static void RyFRotate3(float p_178096_2_) {
        float var3 = MathHelper.sin(p_178096_2_ * p_178096_2_ * 3.1415927F);
        float var4 = MathHelper.sin(MathHelper.sqrt(p_178096_2_) * 3.1415927F);
        GlStateManager.rotate(var3 * -15F, 0.0F, 1.0F, 0.2F);
        GlStateManager.rotate(var4 * -10F, 0.2F, 0.1F, 1.0F);
        GlStateManager.rotate(var4 * -30F, 1.3F, 0.1F, 0.2F);
    }

    @Unique
    private static void RyFRotate4(EnumHandSide p_187459_1_, float equippedProg) {
        int side = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        translate(side * 0.56, -0.52 + equippedProg * -0.6, -0.72);
        translate(side * -0.1414214, 0.08, 0.1414214);
        GlStateManager.rotate(-102.25F, 1, 0, 0);
        GlStateManager.rotate(side * 13.365F, 0, 1, 0);
        GlStateManager.rotate(side * 78.050003F, 0, 0, 1);
    }

    @Unique
    private void RyFRotate5(float var3, float var4) {
        GlStateManager.rotate(var3 * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(var4 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(var4 * -80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    @Unique
    private static void oldAnimation(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        int side = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.translate(side * 0.56F, -0.52F + equippedProg * -0.6F, -0.71999997F);
        float f = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
        float f1 = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
        GlStateManager.rotate(side * (45.0F + f * -20.0F), 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(side * f1 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(side * -45.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(0.9F, 0.9F, 0.9F);
        GlStateManager.translate(-0.2F, 0.126F, 0.2F);
        GlStateManager.rotate(-102.25F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(side * 15.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(side * 80.0F, 0.0F, 0.0F, 1.0F);
    }
    @Unique
    abstract void oldanim();
}